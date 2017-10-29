package com.motostiburon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/motostiburon")
public class IndexController {

	// REFERENCIAS A CONTENEDORES
	private static final String CONTENEDOR_INICIAL = "views/motostiburon/home/inicial";
	private static final String CONTENEDOR_EMPRESA = "views/motostiburon/home/empresa";
	private static final String CONTENEDOR_SERVICIOS = "views/motostiburon/home/servicios";
	private static final String CONTENEDOR_CLIENTES = "views/motostiburon/home/clientes";
	
	private static final String FRAGMENTO = "fragment";

	@RequestMapping("/home")
	public ModelAndView showHome() {
		ModelAndView view = new ModelAndView("index");
		
		view.addObject("contenedor", CONTENEDOR_INICIAL);
		view.addObject("fragmento", FRAGMENTO);
		
		return view;
	}
	
	@RequestMapping("/empresa")
	public ModelAndView showEmpresa() {
		ModelAndView view = new ModelAndView("index");
		
		view.addObject("contenedor", CONTENEDOR_EMPRESA);
		view.addObject("fragmento", FRAGMENTO);
		
		return view;
	}
	
	@RequestMapping("/servicios")
	public ModelAndView showServicios() {
		ModelAndView view = new ModelAndView("index");
		
		view.addObject("contenedor", CONTENEDOR_SERVICIOS);
		view.addObject("fragmento", FRAGMENTO);
		
		return view;
	}
	
	
	@RequestMapping("/clientes")
	public ModelAndView showClientes() {
		ModelAndView view = new ModelAndView("index");
		
		view.addObject("contenedor", CONTENEDOR_CLIENTES);
		view.addObject("fragmento", FRAGMENTO);
		
		return view;
	}
	
	
	
	
}
