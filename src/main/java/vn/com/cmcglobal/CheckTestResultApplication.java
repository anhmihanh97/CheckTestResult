package vn.com.cmcglobal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import vn.com.cmcglobal.service.TestResultServiceV2;

@SpringBootApplication
public class CheckTestResultApplication implements CommandLineRunner{
	@Autowired 
	TestResultServiceV2 resultServiceV2;
	public static void main(String[] args) {
//		SpringApplication.run(CheckTestResultApplication.class, args);
		SpringApplication app = new SpringApplication(CheckTestResultApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);

	}

	@Override
	public void run(String... args) throws Exception {
		resultServiceV2.writeWrongResult(); 	
	}

}
