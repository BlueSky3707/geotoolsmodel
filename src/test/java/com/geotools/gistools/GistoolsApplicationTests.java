package com.geotools.gistools;

import com.geotools.gistools.service.CityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GistoolsApplicationTests {
    @Autowired
    private CityService cityService;
    @Test
    void getlist() {
        System.out.println(cityService.getList());
    }
}
