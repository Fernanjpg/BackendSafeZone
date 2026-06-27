package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.BitacoraRepository;
import SafeZone.SafeZoneBackend.domain.Repository.DenunciasRepository;
import SafeZone.SafeZoneBackend.domain.dto.BitacoraRequest;
import SafeZone.SafeZoneBackend.persistence.entity.BitacoraSeguimiento;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BitacoraService {

    private final BitacoraRepository bitacoraRepository;
    private final DenunciasRepository denunciasRepository;

    public BitacoraService(BitacoraRepository bitacoraRepository,
                           DenunciasRepository denunciasRepository) {
        this.bitacoraRepository = bitacoraRepository;
        this.denunciasRepository = denunciasRepository;
    }

    public BitacoraSeguimiento registrar(BitacoraRequest request) {

        if (request.getDenunciaId() == null || request.getDenunciaId().isBlank()) {
            throw new RuntimeException("El id de la denuncia es obligatorio");
        }

        if (request.getEspecialistaId() == null || request.getEspecialistaId().isBlank()) {
            throw new RuntimeException("El id del especialista es obligatorio");
        }

        if (request.getRolEspecialista() == null || request.getRolEspecialista().isBlank()) {
            throw new RuntimeException("El rol del especialista es obligatorio");
        }

        if (request.getNota() == null || request.getNota().isBlank()) {
            throw new RuntimeException("La nota no puede estar vacía");
        }

        denunciasRepository.buscarPorId(request.getDenunciaId())
                .orElseThrow(() -> new RuntimeException("No existe la denuncia"));

        BitacoraSeguimiento bitacora = new BitacoraSeguimiento();
        bitacora.setId(UUID.randomUUID().toString());
        bitacora.setDenunciaId(request.getDenunciaId());
        bitacora.setEspecialistaId(request.getEspecialistaId());
        bitacora.setRolEspecialista(request.getRolEspecialista().toUpperCase());
        bitacora.setNota(request.getNota());
        bitacora.setFechaRegistro(LocalDateTime.now());

        return bitacoraRepository.guardar(bitacora);
    }

    public List<BitacoraSeguimiento> listarPorDenuncia(String denunciaId) {
        denunciasRepository.buscarPorId(denunciaId)
                .orElseThrow(() -> new RuntimeException("No existe la denuncia"));

        return bitacoraRepository.listarPorDenuncia(denunciaId);
    }
}