package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.service.IContratService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;

    @Override
    public Contrat save(Contrat contrat) {
        // id null -> persist (INSERT) ; id renseigné -> merge (SELECT puis UPDATE)
        return contratRepository.save(contrat);
    }

    @Override
    public Optional<Contrat> findById(Long id) {
        return contratRepository.findById(id);
    }

    @Override
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    @Transactional
    public Contrat update(Long id, Contrat contrat) {
        Contrat existant = contratRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Contrat introuvable : id = " + id));
        existant.setDateSignature(contrat.getDateSignature());
        existant.setMontantTotal(contrat.getMontantTotal());
        existant.setValide(contrat.isValide());
        return contratRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        // cascade = ALL + orphanRemoval -> les paiements liés sont supprimés avec le contrat
        contratRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Contrat ajouterPaiement(Long idContrat, Paiement paiement) {
        Contrat contrat = contratRepository.findById(idContrat)
                .orElseThrow(() -> new NoSuchElementException("Contrat introuvable : id = " + idContrat));
        paiement.setContrat(contrat);
        contrat.getPaiements().add(paiement);
        // cascade = ALL -> inutile d'appeler paiementRepository.save() séparément
        return contratRepository.save(contrat);
    }

    @Override
    @Transactional
    public Contrat retirerPaiement(Long idContrat, Long idPaiement) {
        Contrat contrat = contratRepository.findById(idContrat)
                .orElseThrow(() -> new NoSuchElementException("Contrat introuvable : id = " + idContrat));
        boolean supprime = contrat.getPaiements()
                .removeIf(p -> p.getIdPaiement().equals(idPaiement));
        if (!supprime) {
            throw new NoSuchElementException("Paiement " + idPaiement + " introuvable sur ce contrat");
        }
        // orphanRemoval = true -> suppression automatique en base au flush, sans delete() explicite
        return contratRepository.save(contrat);
    }
}