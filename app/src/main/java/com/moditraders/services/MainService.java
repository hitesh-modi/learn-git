package com.moditraders.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.moditraders.types.ProductType;

@Service("mainService")
public class MainService implements IMainService{
	
	private static final Logger LOGGER = Logger.getLogger(MainService.class);
	
	public String[] getProductTypes() {
		LOGGER.info("Getting Product types");
		List<String> listOfTypes = new ArrayList<String>();
		for(ProductType type : ProductType.values()) {
			listOfTypes.add(type.getType());
		}
		return listOfTypes.toArray(new String[listOfTypes.size()]);
	}
	
}
