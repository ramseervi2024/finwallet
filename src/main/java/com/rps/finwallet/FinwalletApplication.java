package com.rps.finwallet;

import com.rps.finwallet.model.*;
import com.rps.finwallet.repository.*;
import com.rps.finwallet.service.*;
import com.rps.finwallet.controller.*;
import com.rps.finwallet.dto.*;
import com.rps.finwallet.exception.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinwalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinwalletApplication.class, args);
	}

}
