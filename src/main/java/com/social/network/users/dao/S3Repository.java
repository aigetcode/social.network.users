package com.social.network.users.dao;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class S3Repository {
    private final AmazonS3 client;
    private final String bucketName;

    public Collection<String> listKeys() {
        return client.listObjectsV2(bucketName, "/").getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .toList();
    }

    public Collection<String> listKeys(String prefix) {
        return client.listObjectsV2(bucketName, prefix).getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .toList();
    }

    public void delete(String key) {
        client.deleteObject(bucketName, key);
    }

    public void put(String key, InputStream inputStream, ObjectMetadata metadata) {
        try {
            client.putObject(bucketName, key, inputStream, metadata);
        } catch (AmazonS3Exception s3Exception) {
            log.error("Doesn't exist bucket: " + bucketName, s3Exception);
            log.info("Create bucket");
            client.createBucket(bucketName);
            client.putObject(bucketName, key, inputStream, metadata);
        }

    }

    public Optional<S3Object> get(String key) {
        try {
            return Optional.ofNullable(client.getObject(bucketName, key));
        } catch (AmazonServiceException exception) {
            return Optional.empty();
        }
    }
}
