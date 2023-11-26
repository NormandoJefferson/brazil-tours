package com.tiger.braziltours.services.validations;


import com.tiger.braziltours.controllers.exceptions.FieldMessage;
import com.tiger.braziltours.dtos.CityInsertDto;
import com.tiger.braziltours.entities.City;
import com.tiger.braziltours.repositories.CityRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CityInsertValidator implements ConstraintValidator<CityInsertValid, CityInsertDto> {
	
	@Autowired
	private CityRepository cityRepository;

	@Override
	public void initialize(CityInsertValid ann) {
	}

	@Override
	public boolean isValid(CityInsertDto cityInsertDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		City city = cityRepository.findByName(cityInsertDto.getName());
		if (city != null) {
			list.add(new FieldMessage(
					"name",
					"A cidade "
							+ city.getName()
							+ " já está cadastrada."
							+ " Não é possível inserir uma cidade já cadastrada."));
		}
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
