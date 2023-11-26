package com.tiger.braziltours.services.validations;


import com.tiger.braziltours.controllers.exceptions.FieldMessage;
import com.tiger.braziltours.dtos.CityUpdateDto;
import com.tiger.braziltours.entities.City;
import com.tiger.braziltours.repositories.CityRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CityUpdateValidator implements ConstraintValidator<CityUpdateValid, CityUpdateDto> {

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Override
	public void initialize(CityUpdateValid ann) {
	}

	@Override
	public boolean isValid(CityUpdateDto cityUpdateDto, ConstraintValidatorContext context) {
		@SuppressWarnings("unchecked")
		var uriVars =(Map<String, String>)httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		long cityId = Long.parseLong(uriVars.get("id"));
		List<FieldMessage> list = new ArrayList<>();
		City city = cityRepository.findByName(cityUpdateDto.getName());
		if (city != null && cityId != city.getId()) {
			list.add(new FieldMessage(
					"name",
					"A Cidade "
							+ city.getName()
							+ " já está cadastrada."
							+ " Não é possível atualizar uma cidade com o nome de outra já cadastrada."));
		}
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
