package com.easybidding.app.ws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;

@Configuration
@PropertySource("classpath:application.properties")
public class AmazonS3Config {
	@Value("${aws.access.key.id}")
	private String awsKeyId;

	@Value("${aws.access.key.secret}")
	private String awsKeySecret;

	@Value("${aws.region}")
	private String awsRegion;

	@Value("${aws.s3.bucket}")
	private String awsS3Bucket;
	
	@Value("${eb.files.max.upload.threads}")
	private String maxUploadThreads;
	
	@Value("${eb.files.multipart.upload.threshold}")
	private long uploadThreshold;

	@Bean(name = "awsKeyId")
	public String getAWSKeyId() {
		return awsKeyId;
	}

	@Bean(name = "awsKeySecret")
	public String getAWSKeySecret() {
		return awsKeySecret;
	}

	@Bean(name = "awsRegion")
	public Region getAWSPollyRegion() {
		return Region.getRegion(Regions.fromName(awsRegion));
	}

	@Bean(name = "awsCredentialsProvider")
	public AWSCredentialsProvider getAWSCredentials() {
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.awsKeyId, this.awsKeySecret);
		return new AWSStaticCredentialsProvider(awsCredentials);
	}

	@Bean
	public AmazonS3 getS3Client() {
		return AmazonS3ClientBuilder.standard().withRegion(this.awsRegion).withCredentials(getAWSCredentials()).build();
	}

	@Bean
	public TransferManager getTransferManager() {
		return TransferManagerBuilder.standard()
				.withS3Client(this.getS3Client())
				.withMultipartUploadThreshold(this.uploadThreshold)
				.build();
	}

	@Bean(name = "awsS3Bucket")
	public String getAWSS3Bucket() {
		return awsS3Bucket;
	}
}