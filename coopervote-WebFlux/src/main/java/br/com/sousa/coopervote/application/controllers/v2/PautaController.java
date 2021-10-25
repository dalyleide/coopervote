package br.com.sousa.coopervote.application.controllers.v2;

import br.com.sousa.coopervote.domain.Pauta;
import br.com.sousa.coopervote.domain.Voto;
import br.com.sousa.coopervote.service.PautaService;
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
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v2/pauta")
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
    Mono<String> createPauta(@RequestParam final String description) {
        LOOGER.info("Receive request POST createPauta");
        return pautaService.createPauta(description).map(p->
                        "Pauta criada com id: ".concat(p.getId())
                );
    }

    @PostMapping("/open/{id}")
    Mono<Pauta> openPauta(@PathVariable final String id, @RequestParam(required = false) final Integer minutes) {
        LOOGER.info("Receive request POST openPauta");
        Mono<Pauta> pauta = pautaService.abrirPauta(id, minutes);
        pauta.subscribe(p->
                sendMessage.sendPautaCloseMessage(id, p.getDuracao()));
        return pauta;
    }

    @PostMapping("/vote/{id}")
    ResponseEntity<String> vote(@PathVariable final String id, @RequestParam final String document, @RequestParam final VotoEnum vote) {
        LOOGER.info(String.format("Receive request POST vote/{%s}.", id));
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
        LOOGER.info("Receive request POST closePautas");
        try {
            pautaService.finalizarPautas();
            return ResponseEntity.status(OK).body("Pautas fechadas.");
        } catch (Exception e) {
            LOOGER.error(e.getMessage(), e);
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/result/{id}")
    Mono<String> result(@PathVariable final String id) {
        LOOGER.info(String.format("Receive request GET result id: %s.", id));
        return pautaService.resultado(id);
    }

    @GetMapping
    Mono<Map<String, String>> getPautas() {
        LOOGER.info("Receive request GET getPautas");
        return pautaService.getPautas();
    }
}
