package com.motostiburon.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.motostiburon.services.mail.EmailService;

@Controller
@RequestMapping("/motostiburon/contacto")
public class ContactoController {

	// REFERENCIAS A CONTENEDORES
	private static final String CONTENEDOR = "views/motostiburon/home/contacto";
	private static final String FRAGMENTO = "fragment";
	private static final String AUTO_SCROLL_LABEL = "autoScrollEnabled";

	@Autowired
	private EmailService emailService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView show(HttpServletRequest request, HttpSession session) {
		ModelAndView view = new ModelAndView("index");
		view.addObject("contenedor", CONTENEDOR);
		view.addObject("fragmento", FRAGMENTO);
		view.addObject(AUTO_SCROLL_LABEL, true);

		return view;
	}

	@RequestMapping(value = "/email", method = RequestMethod.POST)
	public ModelAndView emailConsultaPresupuesto(HttpServletRequest request, @RequestParam(value = "email", required = true) String email, @RequestParam(value = "mensaje", required = true) String mensaje) {

		mensaje = "Este mensaje ha sido enviado automáticamente desde <a href='wwww.motostiburon.es'>wwww.motostiburon.es</a><br/><br/><pre>" + mensaje+"</pre>";

		emailService.sendMail(email, "Consultas generales", mensaje);
		return new ModelAndView("redirect:/motostiburon/contacto");
	}
}
