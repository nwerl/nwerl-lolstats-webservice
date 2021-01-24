package com.nwerl.lolstats.service;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;

@RestClientTest(value = RiotApiRequestService.class)
public class RiotApiRequestServiceTest {

}