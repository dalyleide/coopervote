package br.com.sousa.coopervote.rest.client;

import br.com.sousa.coopervote.rest.client.utils.UserClientException;
import br.com.sousa.coopervote.rest.client.utils.UserStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class UserClient {

    private final Logger LOOGER = LoggerFactory.getLogger(UserClient.class);

    @Value("${rest.user}")
    String url;

    public boolean userAbleToVote(String cpf) throws UserClientException {

        try{
            // Query parameters
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).path(cpf);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<UserStatusResponse> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,null, UserStatusResponse.class);

            return UserStatusEnum.ABLE_TO_VOTE.name().equals(response.getBody().getStatus());
        }catch (Exception e) {
            LOOGER.error(e.getMessage(), e);
            throw new UserClientException("Erro inesperado ao validar cpf.");
        }
    }

}
