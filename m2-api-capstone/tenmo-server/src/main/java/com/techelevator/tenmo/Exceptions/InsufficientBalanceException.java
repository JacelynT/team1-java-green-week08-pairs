package com.techelevator.tenmo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Insufficient Funds")
public class InsufficientBalanceException extends Exception {

    public InsufficientBalanceException() {
        super("Insufficient funds for transfer!");
    }


}
