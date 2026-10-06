package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import java.util.List;
import java.util.Optional;

public interface IContratService {
    Contrat save(Contrat contrat);
    Optional<Contrat> findById(Long id);
    List<Contrat> findAll();
    Contrat update(Long id, Contrat contrat);
    void deleteById(Long id);
    Contrat ajouterPaiement(Long idContrat, Paiement paiement);
    Contrat retirerPaiement(Long idContrat, Long idPaiement);
}