package com.example.sbkafka.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.sbkafka.Model.BsList;

public interface BsListRepository extends JpaRepository<BsList,Long> {

	//поиск БС по её номеру (надо вводить точный номер БС, для заполнения заявки)
	@Query(value="select * from bslist where bsnumber in(:filter)",nativeQuery=true)
	public BsList findBsByNumber(@Param("filter") String filter);
	
	//поиск БС по её номеру (можно ввести часть номера БС)
	@Query(value="select * from bslist where lower(bsnumber) like concat('%',:bsnum,'%')",nativeQuery=true)
	public List<BsList> findBsByNumber2(@Param("bsnum") String bsnum);
}
