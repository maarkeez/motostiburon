package com.motostiburon.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorControllerCustom implements ErrorController {

	// REFERENCIAS A CONTENEDORES
	private static final String CONTENEDOR = "views/motostiburon/error";
	private static final String FRAGMENTO = "fragment";
	private static final String AUTO_SCROLL_LABEL = "autoScrollEnabled";
	private static final String ERROR_LABEL = "errorStr";

	@RequestMapping(produces = "text/html")
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status = getStatus(request);
		ModelAndView view = new ModelAndView("index");
		view.addObject("contenedor", CONTENEDOR);
		view.addObject("fragmento", FRAGMENTO);
		view.addObject(AUTO_SCROLL_LABEL, true);
		view.addObject(ERROR_LABEL, getErrrorStr(status.value()));

		return view;

	}

	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		try {
			return HttpStatus.valueOf(statusCode);
		} catch (Exception ex) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

	private String getErrrorStr(int httpErrorCode) {
		String errorMsg = "";
		final String ERROR_PREFIX = "(Error " + httpErrorCode + ") ";

		switch (httpErrorCode) {
		case 400: {
			errorMsg = ERROR_PREFIX + "Bad Request";
			break;
		}
		case 401: {
			errorMsg = ERROR_PREFIX + "No tiene permisos para acceder a esta dirección";
			break;
		}
		case 404: {
			errorMsg = ERROR_PREFIX + "Lo sentimos, la página que busca no se encuentra disponible.";
			break;
		}
		case 500: {
			errorMsg = ERROR_PREFIX + "Se ha producido un error en el servidor.";
			break;
		}
		}

		return errorMsg;
	}

	@Override
	public String getErrorPath() {
		return CONTENEDOR;
	}

}
