package com.finerio.api.model.out;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOut {

	private String[] roles;
	private String username;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("expires_in")
	private String expiresIn;

	@JsonProperty("refresh_token")
	private String refreshToken;

}
