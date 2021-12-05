package ru.romanow.inst.services.store.web

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.romanow.inst.services.store.model.TokenDetails

@Tag(name = "Authorization API")
@RestController
@RequestMapping("/api/v1/authorization")
class AuthenticationController {

    @GetMapping(produces = ["application/json"])
    fun authorize(@RegisteredOAuth2AuthorizedClient client: OAuth2AuthorizedClient): TokenDetails {
        return TokenDetails(
            token = client.accessToken.tokenValue,
            refreshToken = client.refreshToken?.tokenValue,
            tokenType = client.accessToken.tokenType.value
        )
    }
}