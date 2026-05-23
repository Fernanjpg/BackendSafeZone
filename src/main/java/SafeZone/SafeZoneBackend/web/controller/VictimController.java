package SafeZone.SafeZoneBackend.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/victim")
public class VictimController {

    @GetMapping("/test")
    public String test() {
        return "JWT funcionando correctamente 🔥";
    }
}