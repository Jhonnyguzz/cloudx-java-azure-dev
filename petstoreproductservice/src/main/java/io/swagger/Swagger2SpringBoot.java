package io.swagger;

import com.chtrembl.petstore.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.chtrembl.petstore.product.model.ContainerEnvironment;
import com.chtrembl.petstore.product.model.DataPreload;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = { "io.swagger", "com.chtrembl.petstore.product.api", "io.swagger.configuration" })
@EnableJpaRepositories(basePackages = "com.chtrembl.petstore.product.repository")
@EntityScan(basePackages = "com.chtrembl.petstore.product.model")
public class Swagger2SpringBoot implements CommandLineRunner {

	@Bean
	public ContainerEnvironment containerEnvvironment() {
		return new ContainerEnvironment();
	}

	@Bean
	public DataPreload dataPreload() {
		return new DataPreload();
	}

	@Override
	public void run(String... arg0) throws Exception {
		if (arg0.length > 0 && arg0[0].equals("exitcode")) {
			throw new ExitException();
		}
	}

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext cac = new SpringApplication(Swagger2SpringBoot.class).run(args);
		//ProductRepository productRepository = cac.getBean(ProductRepository.class);
		//productRepository.findAll().forEach(System.err::println);
	}

	class ExitException extends RuntimeException implements ExitCodeGenerator {
		private static final long serialVersionUID = 1L;

		@Override
		public int getExitCode() {
			return 10;
		}

	}
}
