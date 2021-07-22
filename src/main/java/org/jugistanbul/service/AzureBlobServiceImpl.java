package org.jugistanbul.service;

import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.sas.SasProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * @author hakdogan (huseyin.akdogan@patikaglobal.com)
 * Created on 22.07.2021
 **/
@Service
@Slf4j
public class AzureBlobServiceImpl implements BlobService
{

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Override
    public Optional<String> storeFile(final MultipartFile file,
                                      final String container,
                                      final String blobName) {

        var blobClient = getBlobClient(connectionString, container, blobName);
        if(blobClient.exists()){
            log.warn("The file was already located on storage account. File name {}",
                    file.getOriginalFilename());
            return Optional.ofNullable(blobClient.getBlobUrl());
        }
        try {
            blobClient.upload(file.getInputStream(), file.getSize());
            log.info("Uploading file to {} container. File name {} Original file name {}", container,
                    blobName, file.getOriginalFilename());

            return Optional.ofNullable(blobClient.getBlobUrl());
        } catch (IOException ioe) {
            log.error("File {} could not be uploaded!", file.getOriginalFilename(), ioe);
        }

        return Optional.empty();
    }

    @Override
    public String generateAccessUrlWithSAS(final String containerName, final String blobName,
                                           final int amount, final ChronoUnit chronoUnit) {
        var blobContainerSasPermission = new BlobContainerSasPermission().setReadPermission(true);
        var builder = new BlobServiceSasSignatureValues(OffsetDateTime.now().plus(amount, chronoUnit),
                blobContainerSasPermission).setProtocol(SasProtocol.HTTPS_ONLY);

        var client = getBlobClient(connectionString, containerName, blobName);

        return client.exists()?String.format("https://%s.blob.core.windows.net/%s/%s?%s", client.getAccountName(),
                client.getContainerName(), blobName, client.generateSas(builder)):"Resource not found";
    }
}
