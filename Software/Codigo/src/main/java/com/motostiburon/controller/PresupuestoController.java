package com.motostiburon.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
	private static final String AUTO_SCROLL_LABEL = "autoScrollEnabled";


	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView show(HttpServletRequest request, HttpSession session) {
		ModelAndView view = new ModelAndView("index");
		view.addObject("contenedor", CONTENEDOR);
		view.addObject("fragmento", FRAGMENTO);
		view.addObject(AUTO_SCROLL_LABEL, true);


		String origen = (String) session.getAttribute("PresupuestoOrigen");
		String destino = (String) session.getAttribute("PresupuestoDestino");
		request.getSession().setAttribute("PresupuestoOrigen", null);
		request.getSession().setAttribute("PresupuestoDestino", null);

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

		request.getSession().setAttribute("PresupuestoOrigen", origen);
		request.getSession().setAttribute("PresupuestoDestino", destino);
		return new ModelAndView("redirect:/motostiburon/presupuesto");
	}

}
