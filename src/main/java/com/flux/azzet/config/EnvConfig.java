package com.flux.azzet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvConfig {

	@Value("${app.env}")
	private String env;

	public boolean isDev() {
		return "dev".equalsIgnoreCase(env);
	}

	public boolean isProd() {
		return "pro".equalsIgnoreCase(env);
	}

	public String getEnv() {
		return env;
	}
}