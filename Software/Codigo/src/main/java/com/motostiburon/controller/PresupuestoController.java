package com.motostiburon.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.motostiburon.utils.StringUtils;

@Controller
@RequestMapping("/motostiburon/presupuesto")
public class PresupuestoController {

	// REFERENCIAS A CONTENEDORES
	private static final String CONTENEDOR = "views/motostiburon/home/presupuesto";
	private static final String FRAGMENTO = "fragment";

	public ModelAndView show(String origen, String destino) {
		ModelAndView view = new ModelAndView("index");
		view.addObject("contenedor", CONTENEDOR);
		view.addObject("fragmento", FRAGMENTO);

		if (StringUtils.isNotNullOrWhiteSpaces(origen)) {
			view.addObject("origen", origen);
		}

		if (StringUtils.isNotNullOrWhiteSpaces(origen)) {
			view.addObject("destino", destino);
		}

		return view;
	}

	@RequestMapping(value = "/consultar", method = RequestMethod.POST)
	public ModelAndView consultar(HttpServletRequest request, @RequestParam(value = "origen", required = false) String origen, @RequestParam(value = "destino", required = false) String destino) {

		return show(origen, destino);
	}

}
