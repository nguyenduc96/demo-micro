package com.example.authserver.helper;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "test.jwt.value")
public class ConfigProperties {
	private String secretKey;

	private Long expiredTime;

	private Long refreshTime;
}
