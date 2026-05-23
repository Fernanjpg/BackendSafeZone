package SafeZone.SafeZoneBackend.exception;

import SafeZone.SafeZoneBackend.domain.dto.ErrorResponse;
import com.azure.core.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

/**
 * Manejador global de excepciones.
 * <p>
 * Centraliza las respuestas de error para que el frontend reciba siempre el
 * mismo formato {@link ErrorResponse} sin importar qué salió mal en el backend.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Recursos no encontrados — HTTP 404.
     * Ejemplo: un profesional busca una alerta que ya fue eliminada.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Argumentos inválidos — HTTP 400.
     * <p>
     * RF-03: cubre el caso en que la alerta llega sin ninguna forma de ubicación,
     * o cuando un ID de alerta no existe al intentar atender/resolver.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Errores de validación de Bean Validation (@Valid) — HTTP 400.
     * <p>
     * RF-03: se activa cuando el DTO no pasa la validación {@code @AssertTrue}
     * (sin GPS ni dirección manual) o los campos obligatorios están vacíos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        // Concatenamos todos los mensajes de validación en uno solo
        String mensajes = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        // También capturamos errores a nivel de clase (como @AssertTrue en el DTO)
        String erroresGlobales = ex.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(ge -> ge.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        String mensaje = mensajes.isEmpty() ? erroresGlobales
                : erroresGlobales.isEmpty() ? mensajes
                : mensajes + " | " + erroresGlobales;

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                mensaje,
                request.getDescription(false));

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Errores internos no controlados — HTTP 500.
     * Oculta detalles técnicos de Cosmos DB o del servicio de mapas al cliente.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        System.out.println("💥 ERROR GLOBAL:");
        ex.printStackTrace();

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
