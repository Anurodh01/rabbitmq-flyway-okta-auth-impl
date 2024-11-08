package com.amount.orders.config;

import com.amount.orders.util.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class OAuthClientConfiguration {
    @Value("${CUSTOMER_SERVICE_URL}")
    private String CUSTOMER_SERVICE_URL;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf((csrf) -> csrf.disable()).authorizeHttpRequests(auth -> {
            auth.requestMatchers("/api/v1/**")
                    .permitAll();
        });
        return httpSecurity.build();
    }

    //WebClient based configuration
    @Bean
    ReactiveClientRegistrationRepository clientRegistrations(
            @Value("${spring.security.oauth2.client.provider.okta.token-uri}") String token_uri,
            @Value("${spring.security.oauth2.client.registration.okta.client-id}") String client_id,
            @Value("${spring.security.oauth2.client.registration.okta.client-secret}") String client_secret,
            @Value("${spring.security.oauth2.client.registration.okta.scope}") String scope,
            @Value("${spring.security.oauth2.client.registration.okta.authorization-grant-type}") String authorizationGrantType

    ) {
        ClientRegistration registration = ClientRegistration
                .withRegistrationId("okta")
                .tokenUri(token_uri)
                .clientId(client_id)
                .clientSecret(client_secret)
                .scope(scope)
                .authorizationGrantType(new AuthorizationGrantType(authorizationGrantType))
                .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }

    @Bean
    WebClient webClient(ReactiveClientRegistrationRepository clientRegistrations) {
        InMemoryReactiveOAuth2AuthorizedClientService clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrations);
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrations, clientService);
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth.setDefaultClientRegistrationId("okta");
        return WebClient.builder()
                .baseUrl(CUSTOMER_SERVICE_URL)
                .filter(oauth)
                .build();
    }


    //REST TEMPlATE based configuration
//    @Bean
//    ClientRegistration oktaClientRegistration(
//            @Value("${spring.security.oauth2.client.provider.okta.token-uri}") String token_uri,
//            @Value("${spring.security.oauth2.client.registration.okta.client-id}") String client_id,
//            @Value("${spring.security.oauth2.client.registration.okta.client-secret}") String client_secret,
//            @Value("${spring.security.oauth2.client.registration.okta.scope}") String scope,
//            @Value("${spring.security.oauth2.client.registration.okta.authorization-grant-type}") String authorizationGrantType
//    ){
//       return ClientRegistration
//                .withRegistrationId("okta")
//                .tokenUri(token_uri)
//                .clientId(client_id)
//                .clientSecret(client_secret)
//                .scope(scope)
//                .authorizationGrantType(new AuthorizationGrantType(authorizationGrantType))
//                .build();
//    }
//    // Create the client registration repository
//    @Bean
//    public ClientRegistrationRepository registrationRepository(ClientRegistration oktaClientRegistration){
//        return new InMemoryClientRegistrationRepository(oktaClientRegistration);
//    }
//
//    // Create the authorized client service
//    @Bean
//    public OAuth2AuthorizedClientService auth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository){
//        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
//    }
//
//    //Create authorized client manager and service manager using the above created bean
//    @Bean
//    public AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager (
//            ClientRegistrationRepository clientRegistrationRepository,
//            OAuth2AuthorizedClientService clientService
//    ){
//        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build();
//        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, clientService);
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//        return authorizedClientManager;
//    }


}
