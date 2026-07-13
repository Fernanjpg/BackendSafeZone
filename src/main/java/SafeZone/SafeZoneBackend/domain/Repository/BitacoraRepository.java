package SafeZone.SafeZoneBackend.domain.Repository;

import SafeZone.SafeZoneBackend.persistence.crud.BitacoraCrudRepository;
import SafeZone.SafeZoneBackend.persistence.entity.BitacoraSeguimiento;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BitacoraRepository {

    private final BitacoraCrudRepository bitacoraCrudRepository;

    public BitacoraRepository(BitacoraCrudRepository bitacoraCrudRepository) {
        this.bitacoraCrudRepository = bitacoraCrudRepository;
    }

    public BitacoraSeguimiento guardar(BitacoraSeguimiento bitacora) {
        return bitacoraCrudRepository.save(bitacora);
    }

    public List<BitacoraSeguimiento> listarPorDenuncia(String denunciaId) {
        return bitacoraCrudRepository.findByDenunciaId(denunciaId);
    }
}