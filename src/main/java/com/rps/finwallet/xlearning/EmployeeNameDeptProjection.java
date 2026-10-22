package com.rps.finwallet.xlearning;

import java.math.BigDecimal;

/**
 * Interface-based Projection:
 * Spring Data JPA creates a dynamic proxy for this interface.
 * Only the getter fields are fetched from database columns!
 */
public interface EmployeeNameDeptProjection {
    String getName();
    String getEmail();
    BigDecimal getBaseSalary();
}
