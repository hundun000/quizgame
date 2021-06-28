package com.zaca.stillstanding.api.spring;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import com.zaca.stillstanding.api.StillStandingApiFeignConfiguration;

import feign.Response;






@FeignClient(
        name = "gameApiFeignClient",
        url = "http://localhost:10100/api/game",
        configuration = StillStandingApiFeignConfiguration.class
)
@Component
public interface GameApiFeignClient extends IGameApi {
    
    
    @RequestMapping(
            value = "/pictures", 
            method = RequestMethod.GET
    )
    Response pictures(@RequestParam("id") String imageResourceId);
    
}
