package org.bilt.models;


import javax.validation.constraints.*;

public class CurrencyPair {
    @NotBlank(message="Base Currency may not be blank")
    @Pattern(regexp = "[a-zA-Z]{3,4}", message = "Base Currency must be between 3 to 4 characters")
    public String base;
    @NotBlank(message="Quote Currency may not be blank")
    @Pattern(regexp = "[a-zA-Z]{3,4}", message = "Quote Currency must be between 3 to 4 characters")
    public String quote;
    @DecimalMin(message = "Your rate needs to be bigger than zero", value = "0.01", inclusive = false)
    public double rate;

}
