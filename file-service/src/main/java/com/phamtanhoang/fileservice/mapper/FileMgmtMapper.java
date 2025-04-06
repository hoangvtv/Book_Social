package com.phamtanhoang.fileservice.mapper;


import com.phamtanhoang.fileservice.dto.FileInfo;
import com.phamtanhoang.fileservice.entity.FileMgmt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMgmtMapper {

  @Mapping(target = "id", source = "name")
  FileMgmt toFileMgmt(FileInfo fileInfo);
}
