package edu.ctb.upm.midas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ctb.upm.midas.common.util.Common;
import edu.ctb.upm.midas.common.util.TimeProvider;
import edu.ctb.upm.midas.model.wikipediaApi.Disease;
import edu.ctb.upm.midas.model.wikipediaApi.Page;
import edu.ctb.upm.midas.service.WikipediaApiService;
import edu.ctb.upm.midas.service.jpa.DocumentService;
import edu.ctb.upm.midas.service.jpa.impl.DocumentServiceImpl;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
//@EnableCircuitBreaker
//@EnableHystrix
//@EnableDiscoveryClient
@EnableScheduling
@EntityScan(basePackages = "edu.ctb.upm.midas.model.jpa")
public class DsvpApplication  implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(DsvpApplication.class);

	@Autowired
	private DataSource dataSource;


	public static void main(String[] args) {
		SpringApplication.run(DsvpApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//cerateEmployee();
		//getEmployeeById();
		//employeeDAO.updateEmployeeEmailById(3, "kishan.cs2111@gamil.com");
		//employeeDAO.deleteEmployeeById(3);

//		System.out.println("==>> " + "/wiki/PubMed_Identifier".toLowerCase().contains("pubmed"));
		System.out.println("DataSource==="+dataSource);
	}

	@Value("${spring.datasource.url}")
	private String spring_datasource_url;
	@Value("${spring.datasource.username}")
	private String spring_datasource_username;
	@Value("${spring.datasource.password}")
	private String spring_datasource_password;
	@Value("${my.service.client.mcte.url}")
	private String mtc_service_client_url;
	@Value("${my.service.client.wte.url}")
	private String wtc_service_client_url;


	@PostConstruct
	public void setup(){
		System.out.println(
				"MySQL Variables: URL: " + this.spring_datasource_url + "\n" +
						"USERNAME: " + this.spring_datasource_username + "\n" +
						"PASS: " + this.spring_datasource_password);
		System.out.println("MayoClinic REST API connection info: \n" +
				"url: " + mtc_service_client_url);
		System.out.println("Wikipedia REST API connection info: \n" +
				"url: " + wtc_service_client_url);
	}
}
