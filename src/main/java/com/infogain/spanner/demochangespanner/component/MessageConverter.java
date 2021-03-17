package com.infogain.spanner.demochangespanner.component;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageConverter {

	@Autowired
	private Gson gson;

	public String convert() {
		String result = StringUtils.EMPTY;
		try{
			log.info("going to convert object into json {}");
			//result = gson.toJson(pnr);
			log.info("converted json {}", result);
		}catch (Exception e){
			log.error("Exception while converting record to json", e);
		}
		return result;
	}

}