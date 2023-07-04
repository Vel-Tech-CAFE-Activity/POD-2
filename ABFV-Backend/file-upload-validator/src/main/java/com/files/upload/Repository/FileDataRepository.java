package com.files.upload.Repository;

import com.files.upload.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, Long>{

    Optional<FileData> findByHash(String hash);
	Optional<List<FileData>> findByFileName(String fileName);
}
