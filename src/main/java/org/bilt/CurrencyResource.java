package org.bilt;

import io.smallrye.mutiny.Uni;
import org.bilt.models.Currency;
import org.bilt.models.CurrencyPair;
import org.bilt.service.CurrencyPairService;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/currency")
@Produces(MediaType.APPLICATION_JSON)
public class CurrencyResource {
    @Inject
    Validator validator;

    @Inject
    CurrencyPairService currencyService;

    @GET
    public Uni<Response> getAllCurrencies() {
       return Uni.createFrom()
                .item(currencyService.getAllCurrencies())
                .onItem()
                .transform(Response::ok)
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @GET
    @Path("/{curr: [a-zA-Z]{3,4}}")
    public Uni<Response> getAllforSpecificCurrency(@PathParam("curr") String curr) {
        if(curr.isBlank()){
            return Uni.createFrom().item("Your currency must not be 3 to 4 characters")
                    .onItem().transform(n -> Response.status(400).entity(n)).onItem().transform(Response.ResponseBuilder::build);

        }
       return Uni.createFrom()
                .item(currencyService.getAllforSpecificCurrency(curr.toUpperCase()))
                .onItem()
                .transform(Response::ok)
                .onItem().transform(Response.ResponseBuilder::build);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> addCurrency(CurrencyPair curr) {
        Set<ConstraintViolation<CurrencyPair>> violations = validator.validate(curr);
        if(!violations.isEmpty()){
            return Uni.createFrom().item(violations.stream()
                    .map(cv -> cv.getMessage())
                    .collect(Collectors.joining(", ")))
                    .onItem().transform(n -> Response.status(400).entity(n)).onItem().transform(Response.ResponseBuilder::build);
        }

        curr.base = curr.base.toUpperCase();
        curr.quote = curr.quote.toUpperCase();
        //Want to keep currency rate at least two decimal places
        curr.rate = new BigDecimal(curr.rate).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        currencyService.save(curr);
        CurrencyPair reverse = new CurrencyPair();
        reverse.base = curr.quote;
        reverse.quote = curr.base;
        reverse.rate = new BigDecimal(1/curr.rate).setScale(2, RoundingMode.UP).doubleValue();
        currencyService.save(reverse);

        return Uni.createFrom()
                .item("Have fun and try some conversions")
                .onItem()
                .transform(n -> Response.status(201).entity(n))
                .onItem().transform(Response.ResponseBuilder::build);
    }


    @Path("/convert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> convert(Currency currency) {
        Set<ConstraintViolation<Currency>> violations = validator.validate(currency);
        if(!violations.isEmpty()){
            return Uni.createFrom().item(violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")))
                    .onItem().transform(n -> Response.status(400).entity(n)).onItem().transform(Response.ResponseBuilder::build);

        }
        currency.from = currency.from.toUpperCase();
        currency.to = currency.to.toUpperCase();

        if(!currencyService.checkIfPairingExists(currency.from, currency.to)){
            return Uni.createFrom().item("One of your currencies does not exist")
                    .onItem().transform(n -> Response.status(400).entity(n)).onItem().transform(Response.ResponseBuilder::build);

        }

        currency.rate  = currencyService.getRate(currency.from, currency.to);
        double res = currency.amount * currency.rate;
        double current_result = new BigDecimal(res).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        currency.result = current_result;
        currency.time = LocalDateTime.now();

        return Uni.createFrom()
                .item(currency)
                .onItem()
                .transform(Response::ok)
                .onItem().transform(Response.ResponseBuilder::build);
    }
}
