package com.hicode.restapi.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import com.hicode.restapi.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("*")
public class FileRestController {
      
	@Autowired
	FileService fileService;
	
	@GetMapping("/rest/files/{folder}/{file}")
	public byte[] download(@PathVariable("folder")String folder,@PathVariable("file")String file) {
		return fileService.read(folder, file);
	}
	@PostMapping("/rest/files/{folder}")
	public List<String> upload(@PathVariable("folder")String folder, @PathParam("files") MultipartFile[] files) {
		return fileService.save(files, folder);
	}
	@GetMapping("/rest/files/{folder}")
	public List<String> list(@PathVariable("folder") String folder){
		return fileService.list(folder);
	}
	@DeleteMapping("/rest/files/{folder}/{file}")
	public void delete(@PathVariable("folder")String folder,@PathVariable("file")String file) {
		fileService.delete(folder, file);
	}
}
