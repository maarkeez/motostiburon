package com.motostiburon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/motostiburon")
public class IndexController {

	// REFERENCIAS A CONTENEDORES
	private static final String CONTENEDOR = "views/motostiburon/home/inicial";
	private static final String FRAGMENTO = "fragment";

	@RequestMapping("/home")
	public ModelAndView show() {
		ModelAndView view = new ModelAndView("index");
		
		view.addObject("contenedor", CONTENEDOR);
		view.addObject("fragmento", FRAGMENTO);
		
		return view;
	}
	
	
}
