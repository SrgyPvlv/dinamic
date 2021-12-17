package com.example.sbkafka.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sbkafka.Model.FileDB;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB,Integer> {

}
