package com.onboarding.user.onboardinguser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onboarding.user.onboardinguser.utils.Response;

@RestController
public class Health {

	@GetMapping("/health")
    ResponseEntity<Response> index() {

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        String up = "UP: ";
        
        return Response.result(Response.success(200, up.concat(nowAsISO)));
    }
    

}