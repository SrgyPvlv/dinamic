package com.example.sbkafka.Service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.sbkafka.Model.FileDB;
import com.example.sbkafka.Repository.FileDBRepository;

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
