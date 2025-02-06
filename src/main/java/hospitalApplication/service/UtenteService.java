package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Utente;
import hospitalApplication.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final AuthenticationService authenticationService;

    public UtenteService(UtenteRepository utenteRepository, AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        this.utenteRepository = utenteRepository;
    }


    @Transactional
    public Utente getUtenteByEmail(String email) {
        Utente utente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        return utenteRepository.findByEmail(email);
    }


    @Transactional
    public Utente updateUtente(Long id, Utente utenteDetails) {
        Utente authenticatedUtente = utenteRepository.findByUsername(authenticationService.getUsername());
        if (authenticatedUtente == null) {
            throw new IllegalArgumentException("Utente autenticato non trovato");
        }
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        utente.setFirstName(utenteDetails.getFirstName());
        utente.setLastName(utenteDetails.getLastName());

        return utenteRepository.save(utente);
    }

    @Transactional
    public boolean deleteUtente(String username) {
        Utente userToDelete = utenteRepository.findByUsername(username);
        if (userToDelete != null) {
            utenteRepository.delete(userToDelete);
            return true;
        }
        return false;
    }


    @Transactional
    public boolean userExistsByUsername(String username) {
        Utente user = utenteRepository.findByUsername(username);
        if (user != null) {
            return true;
        }else{
            return false;
        }
    }

    @Transactional
    public Utente getUserDetailsDataBase() {
        String username = authenticationService.getUsername();
        return utenteRepository.findByUsername(username);
    }
}

