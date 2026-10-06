package tn.esprit.autoloc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.service.IVehiculeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final IVehiculeRepository vehiculeRepository;
    private final IVehiculeService vehiculeService;
    private final IClientService clientService;
    private final IReservationService reservationService;
    private final IContratService contratService;

    @Override
    @Transactional
    public void run(String... args) {
        if (vehiculeRepository.count() == 0) {
            insererVehiculesDeDemo();
        }

        testerCascadeEtOrphanRemoval();
    }

    private void insererVehiculesDeDemo() {
        Vehicule v1 = new Vehicule(null, "TU-1234-AB", "Renault", "Clio",
                CategorieVehicule.CITADINE, new BigDecimal("60.00"), StatutVehicule.DISPONIBLE);
        Vehicule v2 = new Vehicule(null, "TU-5678-CD", "Peugeot", "308",
                CategorieVehicule.BERLINE, new BigDecimal("90.00"), StatutVehicule.DISPONIBLE);
        Vehicule v3 = new Vehicule(null, "TU-9012-EF", "Toyota", "RAV4",
                CategorieVehicule.SUV, new BigDecimal("150.00"), StatutVehicule.MAINTENANCE);

        vehiculeRepository.saveAll(List.of(v1, v2, v3));
    }

    private void testerCascadeEtOrphanRemoval() {
        System.out.println("=== DEBUT TEST CASCADE / ORPHAN REMOVAL ===");

        Client client = new Client(null, "Ben Ali", "Sami", "sami.benali@test.tn",
                "20123456", "PM123456", LocalDate.now());
        client = clientService.save(client);

        Vehicule vehicule = vehiculeService.findAll().get(0);
        Reservation reservation = new Reservation(null, LocalDate.now(), LocalDate.now().plusDays(3),
                StatutReservation.CONFIRMEE);
        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        reservation = reservationService.save(reservation);

        Contrat contrat = new Contrat(null, LocalDate.now(), new BigDecimal("180.00"), true);
        contrat.setReservation(reservation);
        contrat = contratService.save(contrat);
        System.out.println(">>> Contrat créé, id = " + contrat.getIdContrat());

        Paiement paiement1 = new Paiement(null, new BigDecimal("180.00"), LocalDate.now(), ModePaiement.CARTE);
        contrat = contratService.ajouterPaiement(contrat.getIdContrat(), paiement1);
        System.out.println(">>> Paiement ajouté, nombre de paiements = " + contrat.getPaiements().size());

        Long idPaiement = contrat.getPaiements().get(0).getIdPaiement();
        contrat = contratService.retirerPaiement(contrat.getIdContrat(), idPaiement);
        System.out.println(">>> Paiement retiré, nombre de paiements = " + contrat.getPaiements().size());

        System.out.println("=== FIN TEST CASCADE / ORPHAN REMOVAL ===");
    }
}