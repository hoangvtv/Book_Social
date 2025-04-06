package com.phamtanhoang.fileservice.service;

import com.phamtanhoang.fileservice.dto.response.FileResponse;
import com.phamtanhoang.fileservice.mapper.FileMgmtMapper;
import com.phamtanhoang.fileservice.repository.FileMgmtRepository;
import com.phamtanhoang.fileservice.repository.FileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService {
  FileRepository fileRepository;
  FileMgmtRepository fileMgmtRepository;
  FileMgmtMapper fileMgmtMapper;

  public FileResponse uploadFile(MultipartFile file) throws IOException {
    var fileInfo = fileRepository.store(file);

    var fileMgmt = fileMgmtMapper.toFileMgmt(fileInfo);

    String userId = SecurityContextHolder.getContext().getAuthentication().getName();
    fileMgmt.setOwnerId(userId);

    fileMgmt =  fileMgmtRepository.save(fileMgmt);

    return FileResponse.builder()
        .url(fileInfo.getUrl())
        .originalFilename(file.getOriginalFilename())
        .build();
  }
}
