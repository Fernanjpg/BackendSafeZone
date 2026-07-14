package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.DenunciasRepository;
import SafeZone.SafeZoneBackend.domain.Repository.EvidenciaRepository;
import SafeZone.SafeZoneBackend.domain.dto.EvidenciaResponse;
import SafeZone.SafeZoneBackend.persistence.entity.Evidencia;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EvidenciaService {

    private final EvidenciaRepository evidenciaRepository;
    private final DenunciasRepository denunciasRepository;
    private final CifradoService cifradoService;

    public EvidenciaService(EvidenciaRepository evidenciaRepository,
                            DenunciasRepository denunciasRepository,
                            CifradoService cifradoService) {
        this.evidenciaRepository = evidenciaRepository;
        this.denunciasRepository = denunciasRepository;
        this.cifradoService = cifradoService;
    }

    public EvidenciaResponse subir(String denunciaId, String especialistaId, MultipartFile archivo) {

        if (denunciaId == null || denunciaId.isBlank()) {
            throw new RuntimeException("El id de la denuncia es obligatorio");
        }

        if (especialistaId == null || especialistaId.isBlank()) {
            throw new RuntimeException("El id del especialista es obligatorio");
        }

        if (archivo == null || archivo.isEmpty()) {
            throw new RuntimeException("El archivo es obligatorio");
        }

        denunciasRepository.buscarPorId(denunciaId)
                .orElseThrow(() -> new RuntimeException("No existe la denuncia"));

        try {
            String archivoCifrado = cifradoService.cifrar(archivo.getBytes());

            Evidencia evidencia = new Evidencia();
            evidencia.setId(UUID.randomUUID().toString());
            evidencia.setDenunciaId(denunciaId);
            evidencia.setEspecialistaId(especialistaId);
            evidencia.setNombreArchivo(archivo.getOriginalFilename());
            evidencia.setTipoArchivo(archivo.getContentType());
            evidencia.setArchivoCifrado(archivoCifrado);
            evidencia.setFechaSubida(LocalDateTime.now());

            return toResponse(evidenciaRepository.guardar(evidencia));

        } catch (Exception e) {
            throw new RuntimeException("Error al subir la evidencia");
        }
    }

    public List<EvidenciaResponse> listarPorDenuncia(String denunciaId) {
        denunciasRepository.buscarPorId(denunciaId)
                .orElseThrow(() -> new RuntimeException("No existe la denuncia"));

        return evidenciaRepository.listarPorDenuncia(denunciaId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Evidencia buscarArchivo(String evidenciaId) {
        return evidenciaRepository.buscarPorId(evidenciaId)
                .orElseThrow(() -> new RuntimeException("No existe la evidencia"));
    }

    public byte[] descargar(String evidenciaId) {
        Evidencia evidencia = buscarArchivo(evidenciaId);
        return cifradoService.descifrar(evidencia.getArchivoCifrado());
    }

    public void eliminar(String evidenciaId) {
        Evidencia evidencia = buscarArchivo(evidenciaId);
        evidenciaRepository.eliminar(evidencia);
    }

    private EvidenciaResponse toResponse(Evidencia evidencia) {
        EvidenciaResponse response = new EvidenciaResponse();
        response.setId(evidencia.getId());
        response.setDenunciaId(evidencia.getDenunciaId());
        response.setNombreArchivo(evidencia.getNombreArchivo());
        response.setTipoArchivo(evidencia.getTipoArchivo());
        response.setEspecialistaId(evidencia.getEspecialistaId());
        response.setFechaSubida(evidencia.getFechaSubida());
        return response;
    }
}