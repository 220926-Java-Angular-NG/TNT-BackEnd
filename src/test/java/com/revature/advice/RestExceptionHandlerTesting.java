package com.revature.advice;

import com.revature.exceptions.NotLoggedInException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

@SpringBootTest
public class RestExceptionHandlerTesting {

    @InjectMocks
    private RestExceptionHandler restExceptionHandler;

    private MockHttpServletRequest httpServletRequest;

    private NotLoggedInException notLoggedInException;
    private String errorMessage = "Must be logged in to perform this action";


    @Test
    public void givenServletRequestAndException_handleNotLoggedInException_returnsResponseEntity(){
        ResponseEntity<Object> responseEntity = restExceptionHandler.handleNotLoggedInException(httpServletRequest,notLoggedInException);

        Assertions.assertEquals(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage), responseEntity);
    }

}
