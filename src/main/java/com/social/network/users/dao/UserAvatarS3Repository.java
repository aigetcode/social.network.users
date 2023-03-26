package com.social.network.users.dao;

import com.amazonaws.services.s3.AmazonS3;
import com.social.network.users.configuration.S3Properties;
import org.springframework.stereotype.Component;

@Component
public class UserAvatarS3Repository extends S3Repository {
    public UserAvatarS3Repository(AmazonS3 client, S3Properties properties) {
        super(client, properties.getBucketUsersAvatar());
    }
}
