package SafeZone.SafeZoneBackend.domain.Repository;


import SafeZone.SafeZoneBackend.persistence.crud.RegionesCrudRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Regiones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class RegionesRepository {

@Autowired
private RegionesCrudRepository crud;


   public List<Regiones> listarTodas() {
      Iterable<Regiones> resultado = crud.findAll();

      // 2. Lo convertimos a una lista real de Java usando StreamSupport
      return StreamSupport.stream(resultado.spliterator(), false)
              .collect(Collectors.toList());
   }

   public Optional<Regiones> buscarPorId(String id) {
      return crud.findById(id);
   }


   public Regiones guardar(Regiones region) {
      return crud.save(region);
   }


}
