package com.lumen.employeeRelations.repository;


import com.lumen.employeeRelations.dto.AddressDTO;
import com.lumen.employeeRelations.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {}