package com.zaca.stillstanding.api.spring;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import com.zaca.stillstanding.api.StillStandingApiFeignConfiguration;

import feign.Response;






@FeignClient(
        name = "prepareApiFeignClient",
        url = "http://localhost:10100/api/prepare",
        configuration = StillStandingApiFeignConfiguration.class
)
@Component
public interface PrepareApiFeignClient extends IPrepareApi {
    
    
}
