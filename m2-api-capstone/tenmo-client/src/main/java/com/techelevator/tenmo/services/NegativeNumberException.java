package com.techelevator.tenmo.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Negative number")
public class NegativeNumberException extends Exception {

    public NegativeNumberException() {
        super("Cannot enter a negative number");
    }


}
