package am.itspace.employeemanagement.util;

import am.itspace.employeemanagement.entity.Employee;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

@Component
@Slf4j
public class FileUtil {

    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    @SneakyThrows
    public void uploadImage(Employee employee, MultipartFile multipartFile) {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            multipartFile.transferTo(new File(uploadDirectory, fileName));
            employee.setPicture(fileName);
        }
    }

    @SneakyThrows
    public byte[] getPicture(String picName) {
        File file = new File(uploadDirectory, picName);
        if (file.exists()) {
            byte[] byteArray = IOUtils.toByteArray(new FileInputStream(file));
            if (byteArray.length == 0) {
                return null;
            }
            return byteArray;
        }
        return null;
    }

}

