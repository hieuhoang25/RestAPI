package com.hicode.restapi.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
    @Autowired
    ServletContext app;

    // get duong dan day du
    private Path getPath(String foder, String filename) {
        File dir = Paths.get(app.getRealPath("/files/"), foder).toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return Paths.get(dir.getAbsolutePath(), filename);
    }

    public byte[] read(String foder, String filename) {
        Path path = this.getPath(foder, filename);
        try {
            return Files.readAllBytes(path);
        } catch (Exception e) {
            throw new RuntimeException(e);// TODO: handle exception
        }
    }

    /**
     * @param files
     * @param foder
     * @return
     */
    public List<String> save(MultipartFile[] files, String foder) {
        List<String> list = new ArrayList<String>();
        for (MultipartFile file : files) {
            String name = System.currentTimeMillis() + file.getOriginalFilename();
            String filename = Integer.toHexString(name.hashCode()) + name.substring(name.lastIndexOf("."));
            Path path = this.getPath(foder, filename);
            try {
                file.transferTo(path);
                list.add(filename);

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        return list;
    }

    public void delete(String foder, String filename) {
        Path path = this.getPath(foder, filename);
        path.toFile().delete();
    }

    public List<String> list(String foder) {
        List<String> list = new ArrayList<String>();
        File dir = Paths.get(app.getRealPath("/files/"), foder).toFile();
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                list.add(file.getName());
            }
        }
        return list;
    }
}
