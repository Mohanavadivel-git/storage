package com.azurestorage.storage;
import com.microsoft.applicationinsights.core.dependencies.apachecommons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;


@RestController
public class WebController {

    @Value("blob://test/myfile1.txt")
    private Resource blobFile;

    @Value("blob://test/myfile1.jpg")
    private Resource ImageFile;

    File file = new File("C:\\Users\\VMOHANAV\\Desktop\\New folder (3)\\storage\\src\\main\\resources\\Test.JPG");

    @GetMapping(value = "/storage")
    public String readBlobFile() throws IOException {
        return StreamUtils.copyToString(
                this.blobFile.getInputStream(),
                Charset.defaultCharset()) + "\n";
    }

    @PostMapping(value = "/storage")
    public String writeBlobFile(@RequestBody String data) throws IOException {
        try (OutputStream os = ((WritableResource) this.blobFile).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "File was updated.\n";
    }

    @PostMapping(value = "/storageimage")
    public String writeBlobFilewithImage(@RequestBody String data) throws IOException {
        try (OutputStream os = ((WritableResource) this.ImageFile).getOutputStream()) {
            // Reading a Image file from file system
            FileInputStream imageInFile = new FileInputStream(file);
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);

            // Converting Image byte array into Base64 String
            String imageDataString = encodeImage(imageData);

            // Converting a Base64 String into Image byte array
            byte[] imageByteArray = decodeImage(imageDataString);
            os.write(imageByteArray);
        }
        return "File was updated.\n";
    }


    @PostMapping(value = "/storageimagefile")
    public String writeBlobFilewithMobileImage(@RequestParam ("file") MultipartFile myFile) throws IOException {
        try (OutputStream os = ((WritableResource) this.ImageFile).getOutputStream()) {

            // Reading a Image file from file system
            //FileInputStream imageInFile = new FileInputStream(file);
            //byte imageData[] = new byte[(int) file.length()];
            byte imageData[] = myFile.getBytes();
            //imageInFile.read(imageData);

            // Converting Image byte array into Base64 String
            String imageDataString = encodeImage(imageData);

            // Converting a Base64 String into Image byte array
            byte[] imageByteArray = decodeImage(imageDataString);
            os.write(imageByteArray);
        }
        return "File was updated.\n";
    }

    /**
     * Encodes the byte array into base64 string
     *
     * @param imageByteArray - byte array
     * @return String a {@link java.lang.String}
     */
    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeBase64URLSafeString(imageByteArray);
    }

    /**
     * Decodes the base64 string into byte array
     *
     * @param imageDataString - a {@link java.lang.String}
     * @return byte array
     */
    public static byte[] decodeImage(String imageDataString) {
        return Base64.decodeBase64(imageDataString);
    }
}

