package com.nagarro.digitalMart.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class BlobStorageService {

	@Value("${blob.storage.account.name}")
	private String accountName;

	@Value("${blob.storage.container.name}")
	private String containerName;

	public String getBlobImageUrl(String imageName) {
		if (imageName == null || imageName.isEmpty() || accountName == null || containerName == null) {
			return null;
		}
		String normalized = imageName.startsWith("/") ? imageName.substring(1) : imageName;
		String encoded;
		try {
			encoded = URLEncoder.encode(normalized, StandardCharsets.UTF_8.name()).replace("+", "%20");
		} catch (java.io.UnsupportedEncodingException e) {
			encoded = normalized;
		}
		return String.format("https://%s.blob.core.windows.net/%s/%s", accountName, containerName, encoded);
	}

}
