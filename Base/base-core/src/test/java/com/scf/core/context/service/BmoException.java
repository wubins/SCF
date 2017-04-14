package com.scf.core.context.service;

import com.scf.core.exception.AppException;
import com.scf.core.exception.ExDetail;

public class BmoException extends AppException {
	private static final long serialVersionUID = 1L;

	public BmoException(AppException e) {
		super(e.getDetail(), e);
	}
	
	public BmoException(ExDetail detail, Throwable cause) {
		super(detail, cause);
	}

	public BmoException(ExDetail detail) {
		super(detail);
	}

}
