package com.example.edgeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.example.edgeservice.model.*;

@EnableFeignClients
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class EdgeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdgeServiceApplication.class, args);
	}

}

@FeignClient("item-catalog-service")
interface ItemClient {

	@GetMapping("/items")
	Resources<Item> readItems();

	@PostMapping("/items")
	Item saveItems(Item item);
}

@FeignClient("mail-service")
interface MailService {

	@PostMapping("/mail")
	Mail sendEmail(Mail mail);
}

@RestController
class GoodItemApiAdapterRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final ItemClient itemClient;

	private final MailService mailService;

	public GoodItemApiAdapterRestController(ItemClient itemClient, MailService mailService) {
		this.itemClient = itemClient;
		this.mailService = mailService;

	}

	// @HystrixCommand(fallbackMethod="fallback")
	@GetMapping(value = "/find/item")
	// @CrossOrigin(origins="*")
	public Object goodItems() {
		log.info("");
		return itemClient.readItems().getContent();
	}

	@PostMapping(value = "/save/item")
	// @CrossOrigin(origins="*")
	public Object save(@RequestBody Item item) {
		log.info("");
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		item.setPassword(passwordEncoder.encode(item.getPassword()));

		Item i = itemClient.saveItems(item);
		if (i != null) {
			final String uri = "http://localhost:9090/email";
			Mail mail = new Mail();
			mail.setMailFrom("hotelhaldiram@gmail.com");
			mail.setMailTo(i.getEmail());
			mail.setMailSubject("Spring Boot - Email Example");
			mail.setMailContent(" Email service using Spring Boot!!!\n\nThanks\nwww.raushan.com");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Mail> entity = new HttpEntity<>(mail, headers);
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.postForObject(uri, entity, Mail.class);
		}
		log.info("");
		return i;
	}

}