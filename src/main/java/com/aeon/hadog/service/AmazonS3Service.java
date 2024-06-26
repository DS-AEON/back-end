package com.aeon.hadog.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AmazonS3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private String dir = "pet";

    public String uploadImage(MultipartFile file) throws Exception{
        String originfileName = file.getOriginalFilename();
        String filePath = dir + "/" + UUID.randomUUID() + originfileName.substring(originfileName.lastIndexOf("."));;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.addUserMetadata("originfilename", URLEncoder.encode(originfileName, StandardCharsets.UTF_8));
        PutObjectResult result = amazonS3Client.putObject(bucketName, filePath, file.getInputStream(), metadata);
        return "https://hadog.s3.ap-northeast-2.amazonaws.com/"+filePath;
    }


    public List<String> uploadImages(String folder, List<MultipartFile> multipartFiles) {
        List<String> s3files = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {

            String uploadFileUrl = "";

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            try (InputStream inputStream = multipartFile.getInputStream()) {

                String keyName = folder + "/" + UUID.randomUUID() + "." + multipartFile.getOriginalFilename();

                // S3에 폴더 및 파일 업로드
                amazonS3Client.putObject(
                        new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead));

                // S3에 업로드한 폴더 및 파일 URL
                uploadFileUrl = amazonS3Client.getUrl(bucketName, keyName).toString();

            } catch (IOException e) {
                e.printStackTrace();
                log.error("Filed upload failed", e);
            }

            s3files.add(uploadFileUrl);
        }

        return s3files;
    }


}
