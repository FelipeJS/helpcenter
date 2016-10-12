package helpcenter.web.rest;

import com.codahale.metrics.annotation.Timed;
import helpcenter.domain.Chamado;

import helpcenter.repository.ChamadoRepository;
import helpcenter.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Chamado.
 */
@RestController
@RequestMapping("/api")
public class ChamadoResource {

    private final Logger log = LoggerFactory.getLogger(ChamadoResource.class);
        
    @Inject
    private ChamadoRepository chamadoRepository;

    /**
     * POST  /chamados : Create a new chamado.
     *
     * @param chamado the chamado to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chamado, or with status 400 (Bad Request) if the chamado has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/chamados",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Chamado> createChamado(@Valid @RequestBody Chamado chamado) throws URISyntaxException {
        log.debug("REST request to save Chamado : {}", chamado);
        if (chamado.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("chamado", "idexists", "A new chamado cannot already have an ID")).body(null);
        }
        Chamado result = chamadoRepository.save(chamado);
        return ResponseEntity.created(new URI("/api/chamados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("chamado", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chamados : Updates an existing chamado.
     *
     * @param chamado the chamado to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chamado,
     * or with status 400 (Bad Request) if the chamado is not valid,
     * or with status 500 (Internal Server Error) if the chamado couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/chamados",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Chamado> updateChamado(@Valid @RequestBody Chamado chamado) throws URISyntaxException {
        log.debug("REST request to update Chamado : {}", chamado);
        if (chamado.getId() == null) {
            return createChamado(chamado);
        }
        Chamado result = chamadoRepository.save(chamado);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("chamado", chamado.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chamados : get all the chamados.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of chamados in body
     */
    @RequestMapping(value = "/chamados",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Chamado> getAllChamados() {
        log.debug("REST request to get all Chamados");
        List<Chamado> chamados = chamadoRepository.findAll();
        return chamados;
    }

    /**
     * GET  /chamados/:id : get the "id" chamado.
     *
     * @param id the id of the chamado to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chamado, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/chamados/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Chamado> getChamado(@PathVariable Long id) {
        log.debug("REST request to get Chamado : {}", id);
        Chamado chamado = chamadoRepository.findOne(id);
        return Optional.ofNullable(chamado)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /chamados/:id : delete the "id" chamado.
     *
     * @param id the id of the chamado to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/chamados/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChamado(@PathVariable Long id) {
        log.debug("REST request to delete Chamado : {}", id);
        chamadoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("chamado", id.toString())).build();
    }

}
