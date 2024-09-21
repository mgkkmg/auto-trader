package com.mgkkmg.trader.core.infra.jwt;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mgkkmg.trader.common.exception.JwtErrorException;

@Component
public class JwtTokenProvider {
	@Value("${upbit.access-key}")
	private String accessKey;

	@Value("${upbit.secret-key}")
	private String secretKey;

	public String createToken() {
		final Algorithm algorithm = Algorithm.HMAC256(secretKey);

		return "Bearer " + JWT.create()
			.withClaim("access_key", accessKey)
			.withClaim("nonce", UUID.randomUUID().toString())
			.sign(algorithm);
	}

	public String createToken(List<String> queryElements) {
		final Algorithm algorithm = Algorithm.HMAC256(secretKey);

		String queryString = String.join("&", queryElements.toArray(new String[0]));

		final MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-512");
			md.update(queryString.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			throw new JwtErrorException(e.getMessage());
		}

		return "Bearer " + JWT.create()
			.withClaim("access_key", accessKey)
			.withClaim("nonce", UUID.randomUUID().toString())
			.withClaim("query_hash", String.format("%0128x", new BigInteger(1, md.digest())))
			.withClaim("query_hash_alg", "SHA512")
			.sign(algorithm);
	}
}
