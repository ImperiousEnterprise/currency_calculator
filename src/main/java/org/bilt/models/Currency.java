package org.bilt.models;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class Currency {

    @NotBlank(message="Your From Currency may not be blank")
    public String from;
    @NotBlank(message="Your To Currency may not be blank")
    public String to;
    @DecimalMin(message = "Your rate needs to be bigger than zero", value = "0.01", inclusive = false)
    public double amount;
    public double rate;
    public double result;
    public LocalDateTime time;

}
