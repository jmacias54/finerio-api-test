package com.finerio.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finerio.api.model.entity.BinnacleAccountFinerio;
import com.finerio.api.model.in.UserIn;
import com.finerio.api.model.out.LoginOut;
import com.finerio.api.service.FinerioService;
import com.finerio.api.service.MovementsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class FinerioController {
	
	@Autowired
	private FinerioService finerioService;

	@Autowired
	private MovementsService movementsService;

	@PostMapping("/login")
	public LoginOut login(@RequestBody UserIn in) {
		log.info("--- login --- ", in);

		LoginOut out = finerioService.login(in);
		return out;

	}

	@GetMapping("/runTest")
	public List<BinnacleAccountFinerio> runTest() {
		log.info("--- runTest --- ");
		return movementsService.getAll();

	}

}
