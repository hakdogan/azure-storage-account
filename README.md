# How to Serve Azure Storage Resources with Grant Limited Access using SAS?

`Azure Blob Storage` is Microsoft's object storage solution that allows you to store a massive amount of unstructured data. It is a good solution for needs such as streaming video and audio, serving images or documents directly to a browser, etc, as they are accessible using `REST APIs`. This repository shows you how to serve `Azure Storage Resources` with grant limited access using `SAS` in a declarative way.

```java
    public String generateAccessUrlWithSAS(final String containerName, final String blobName,
                                           final int amount, final ChronoUnit chronoUnit) {
        var blobContainerSasPermission = new BlobContainerSasPermission().setReadPermission(true);
        var builder = new BlobServiceSasSignatureValues(OffsetDateTime.now().plus(amount, chronoUnit),
                blobContainerSasPermission).setProtocol(SasProtocol.HTTPS_ONLY);

        var client = getBlobClient(connectionString, containerName, blobName);

        return client.exists()?String.format("https://%s.blob.core.windows.net/%s/%s?%s", client.getAccountName(),
                client.getContainerName(), blobName, client.generateSas(builder)):"Resource not found";
    }
```

## Requirements

- JDK 12 or later
- Maven 3.8.1 or later

## How to run
```shell
mvn spring-boot:run
```
