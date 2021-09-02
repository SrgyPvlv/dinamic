package com.example.sbkafka;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
	
	@Autowired FileDBRepository fileDBRepository;
	
	public FileDB load(MultipartFile file,int orderform_id) throws IOException{
		String fileName=StringUtils.cleanPath(file.getOriginalFilename());
		FileDB fileDB=new FileDB(fileName,file.getContentType(),file.getBytes(),orderform_id);
		return fileDB;
	}
	
	public void deleteFileDBById(int id) {
		fileDBRepository.deleteById(id);
	}
}
	//сохранил для будущих проектов
	/* public FileDB store(MultipartFile file,int orderform_id) throws IOException{
		String fileName=StringUtils.cleanPath(file.getOriginalFilename());
		FileDB fileDB=new FileDB(fileName,file.getContentType(),file.getBytes(), orderform_id);
		
		return fileDBRepository.save(fileDB);
	}
	public FileDB getFile(int id) {
		return fileDBRepository.findById(id).get();
	}
	public Stream<FileDB> getAllFiles(){
		return fileDBRepository.findAll().stream();
	} */

