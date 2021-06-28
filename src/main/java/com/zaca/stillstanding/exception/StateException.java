package com.zaca.stillstanding.exception;

public class StateException extends StillStandingException {

    private static final long serialVersionUID = 2799953340358311729L;

    public StateException(String stateName, String operationName) {
		super("state = " + stateName + " 时不应 " + operationName, -1);

	}

}
