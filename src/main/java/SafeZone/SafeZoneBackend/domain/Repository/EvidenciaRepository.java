package SafeZone.SafeZoneBackend.domain.Repository;

import SafeZone.SafeZoneBackend.persistence.crud.EvidenciaCrudRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Evidencia;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EvidenciaRepository {

    private final EvidenciaCrudRepository evidenciaCrudRepository;

    public EvidenciaRepository(EvidenciaCrudRepository evidenciaCrudRepository) {
        this.evidenciaCrudRepository = evidenciaCrudRepository;
    }

    public Evidencia guardar(Evidencia evidencia) {
        return evidenciaCrudRepository.save(evidencia);
    }

    public List<Evidencia> listarPorDenuncia(String denunciaId) {
        return evidenciaCrudRepository.findByDenunciaid(denunciaId);
    }

    public Optional<Evidencia> buscarPorId(String id) {
        return evidenciaCrudRepository.findById(id);
    }

    public void eliminar(Evidencia evidencia) {
        evidenciaCrudRepository.delete(evidencia);
    }
}