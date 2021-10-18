package br.com.sousa.coopervote.application.controllers.v1;

import br.com.sousa.coopervote.domain.Pauta;
import br.com.sousa.coopervote.domain.Voto;
import br.com.sousa.coopervote.domain.service.PautaService;
import br.com.sousa.coopervote.domain.utils.VotoEnum;
import br.com.sousa.coopervote.publisher.SendMessage;
import br.com.sousa.coopervote.rest.client.UserClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1/pauta")
public class PautaController {

    private final PautaService pautaService;
    private final SendMessage sendMessage;
    private final UserClient userClient;

    private final Logger LOOGER = LoggerFactory.getLogger(PautaController.class);

    @Autowired
    public PautaController(PautaService pautaService, SendMessage sendMessage, UserClient userClient)
    {
        this.pautaService = pautaService;
        this.sendMessage = sendMessage;
        this.userClient = userClient;
    }

    @PostMapping
    ResponseEntity<String> createPauta(@RequestParam final String description) {
        try {
            final String id = pautaService.createPauta(description).getId();
            return ResponseEntity.status(OK).body("Pauta criada com id: ".concat(id));
        } catch (Exception e) {
            LOOGER.error(e.getMessage(), e);
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/open/{id}")
    ResponseEntity<String> openPauta(@PathVariable final String id, @RequestParam(required = false) final Integer minutes) {
        try {
            Pauta pauta = pautaService.abrirPauta(id, minutes);
            sendMessage.sendPautaCloseMessage(id, pauta.getDuracao());
            return ResponseEntity.status(OK).body("Pauta aberta.");
        } catch (Exception e) {
            LOOGER.error(e.getMessage(), e);
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/vote/{id}")
    ResponseEntity<String> vote(@PathVariable final String id, @RequestParam final String document, @RequestParam final VotoEnum vote) {

        try {
            if (!userClient.userAbleToVote(document))
                return ResponseEntity.status(BAD_REQUEST).body("CPF UNABLE_TO_VOTE.");

            pautaService.addVoto(id, vote, document);
            return ResponseEntity.status(OK).body("Voto registrado.");
        } catch (Exception e) {
            LOOGER.error(e.getMessage(), e);
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/close")
    ResponseEntity<String> closePautas() {
        try {
            pautaService.finalizarPautas();
            return ResponseEntity.status(OK).body("Pautas fechadas.");
        } catch (Exception e) {
            LOOGER.error(e.getMessage(), e);
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/result/{id}")
    ResponseEntity<String> result(@PathVariable final String id) {
        try {
            final Map<VotoEnum, List<Voto>> response = pautaService.resultado(id);
            String result = response.keySet().stream().map(
                    key -> key.name().concat(" : " + response.get(key).size())
            ).collect(Collectors.joining(","));

            if(result.isEmpty())
                result = "Não houve votos registrados.";

            return ResponseEntity.status(OK).body(result);
        } catch (Exception e) {
            LOOGER.error(e.getMessage(), e);
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    ResponseEntity<Map<String, String>> getPautas() {
        try {
            final Map<String, String> response = pautaService.getPautas();
            return ResponseEntity.status(OK).body(response);
        } catch (Exception e) {
            LOOGER.error(e.getMessage(), e);
            return (ResponseEntity<Map<String, String>>) ResponseEntity.status(BAD_REQUEST).body((Map<String, String>)null);
        }
    }
}
