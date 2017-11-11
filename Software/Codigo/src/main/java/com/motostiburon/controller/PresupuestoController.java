package com.motostiburon.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.motostiburon.services.mail.EmailService;
import com.motostiburon.utils.DateUtils;
import com.motostiburon.utils.StringUtils;

@Controller
@RequestMapping("/motostiburon/presupuesto")
public class PresupuestoController {

	// REFERENCIAS A CONTENEDORES
	private static final String CONTENEDOR = "views/motostiburon/home/presupuesto";
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

		String origen = (String) session.getAttribute("PresupuestoOrigen");
		String destino = (String) session.getAttribute("PresupuestoDestino");
		Date fechaEntrega = (Date) session.getAttribute("PresupuestoFechaEntrega");
		Date fechaRecogida = (Date) session.getAttribute("PresupuestoFechaRecogida");

		if (StringUtils.isNotNullOrWhiteSpaces(origen)) {
			view.addObject("origen", origen);
		}

		if (StringUtils.isNotNullOrWhiteSpaces(origen)) {
			view.addObject("destino", destino);
		}

		if (fechaEntrega != null) {
			view.addObject("fechaEntrega", DateUtils.formatDate(fechaEntrega));
		}

		if (fechaRecogida != null) {
			view.addObject("fechaRecogida", DateUtils.formatDate(fechaRecogida));
		}

		return view;
	}

	@RequestMapping(value = "/consultar", method = RequestMethod.POST)
	public ModelAndView consultar(HttpServletRequest request, @RequestParam(value = "origen", required = false) String origen, @RequestParam(value = "destino", required = false) String destino,
			@RequestParam(value = "fechaEntrega") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fechaEntrega, @RequestParam(value = "fechaRecogida") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fechaRecogida) {

		request.getSession().setAttribute("PresupuestoOrigen", origen);
		request.getSession().setAttribute("PresupuestoDestino", destino);
		request.getSession().setAttribute("PresupuestoFechaEntrega", fechaEntrega);
		request.getSession().setAttribute("PresupuestoFechaRecogida", fechaRecogida);

		return new ModelAndView("redirect:/motostiburon/presupuesto");
	}
	
	@RequestMapping(value = "/email/consultar", method = RequestMethod.POST)
	public ModelAndView emailConsultaPresupuesto(HttpServletRequest request,
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "origen", required = true) String origen,
			@RequestParam(value = "destino", required = true) String destino,
			@RequestParam(value = "fechaEntrega", required = true) String fechaEntrega,
			@RequestParam(value = "fechaRecogida", required = true) String fechaRecogida
			) {

		
		String mensaje = "Este mensaje ha sido enviado automáticamente desde <a href='wwww.motostiburon.es'>wwww.motostiburon.es</a>";
		mensaje += "<br/>";
		mensaje += "Recogida de moto en "+origen+" el día "+fechaEntrega + ".<br/>";
		mensaje += "Llevar la moto a "+destino+" el día "+ fechaRecogida + ".<br/>";
		mensaje += "<br/>";
		
		emailService.sendMail(email, "Consulta de presupuesto", mensaje);
		return new ModelAndView("redirect:/motostiburon/presupuesto");
	}
	
	

}
