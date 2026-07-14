package SafeZone.SafeZoneBackend.domain.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class CifradoService {

    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "SafeZoneRF12Key!"; // 16 caracteres

    public String cifrar(byte[] archivo) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] archivoCifrado = cipher.doFinal(archivo);
            return Base64.getEncoder().encodeToString(archivoCifrado);

        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar el archivo");
        }
    }

    public byte[] descifrar(String archivoCifrado) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decoded = Base64.getDecoder().decode(archivoCifrado);
            return cipher.doFinal(decoded);

        } catch (Exception e) {
            throw new RuntimeException("Error al descifrar el archivo");
        }
    }
}