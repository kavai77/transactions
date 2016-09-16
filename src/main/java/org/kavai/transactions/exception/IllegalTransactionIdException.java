package org.kavai.transactions.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Kávai on 2016.09.16..
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Illegal Parent Id")
public class IllegalTransactionIdException extends RuntimeException{
}
