package org.jugistanbul.resource;

import lombok.RequiredArgsConstructor;
import org.jugistanbul.service.AzureBlobServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.temporal.ChronoUnit;
import java.util.Optional;


/**
 * @author hakdogan (huseyin.akdogan@patikaglobal.com)
 * Created on 22.07.2021
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccessBlobResource
{
    private final AzureBlobServiceImpl service;

    @GetMapping(value = "/access/{container}/{blob}/{amount}/{timeUnit}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getAccessURL(@PathVariable("container") String container, @PathVariable("blob") String blob,
                               @PathVariable("amount") int amount, @PathVariable("timeUnit") String timeUnit) {
        return service.generateAccessUrlWithSAS(container, blob, amount, getChronoUnit(timeUnit));
    }

    @PostMapping(value = "/upload")
    public Optional<String> uploadFile(@RequestParam("file") MultipartFile file,
                                       @RequestParam("container") String container,
                                       @RequestParam("blob") String blob) {
        return service.storeFile(file, container, blob);
    }

    private ChronoUnit getChronoUnit(final String timeUnit){
        return switch (timeUnit) {
            case "Minutes" -> ChronoUnit.MINUTES;
            case "Hours" -> ChronoUnit.HOURS;
            case "HalfDays" -> ChronoUnit.HALF_DAYS;
            case "Days" -> ChronoUnit.DAYS;
            default -> ChronoUnit.SECONDS;
        };
    }
}
