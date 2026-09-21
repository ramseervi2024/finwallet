package com.rps.finwallet.repository;

import com.rps.finwallet.model.*;
import com.rps.finwallet.repository.*;
import com.rps.finwallet.service.*;
import com.rps.finwallet.controller.*;
import com.rps.finwallet.dto.*;
import com.rps.finwallet.exception.*;

import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeRepository extends  JpaRepository<Employee, Long>{

}