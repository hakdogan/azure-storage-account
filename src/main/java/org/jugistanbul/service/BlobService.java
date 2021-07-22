package org.jugistanbul.service;

import com.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * @author hakdogan (huseyin.akdogan@patikaglobal.com)
 * Created on 22.07.2021
 **/
public interface BlobService
{
    default BlobClient getBlobClient(final String connectionString, final String containerName,
                                     final String blobName){

        return new BlobClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .blobName(blobName)
                .buildClient();
    }

    String generateAccessUrlWithSAS(final String containerName, final String blobName,
                                    final int amount, final ChronoUnit chronoUnit);

    Optional<String> storeFile(final MultipartFile file, final String container, final String blobName);
}
