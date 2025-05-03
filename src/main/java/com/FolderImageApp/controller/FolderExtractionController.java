package com.FolderImageApp.controller;

import com.FolderImageApp.advices.ApiResponse;
import com.FolderImageApp.dto.CustomMetaData;
import com.FolderImageApp.services.FolderExtractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FolderExtractionController
{
    private final FolderExtractionService folderExtractionService;

    @GetMapping(path = "/getMetaFile")
    public ResponseEntity<CustomMetaData> getMetaData(@RequestParam String fileLoc,
                                                     @RequestParam(required = false,defaultValue = "0") Integer page,
                                                     @RequestParam(required = false,defaultValue = "50") Integer size)
    {
        return ResponseEntity.ok(folderExtractionService.getMetaData(fileLoc,page,size));
    }

    @GetMapping(path = "/getMetaFileWithinDate")
    public ResponseEntity<CustomMetaData> getMetaFileWithinDate(@RequestParam String fileLoc,
                                      @RequestParam(required = false,defaultValue = "0") Integer page,
                                      @RequestParam(required = false,defaultValue = "50") Integer size,
                                                @RequestParam(required = false,defaultValue = "") Long startDate,
                                                @RequestParam(required = false,defaultValue = "") Long endDate
                                                )
    {
        return ResponseEntity.ok(folderExtractionService.getMetaFileWithinDate(fileLoc,page,size,startDate,endDate));
    }

    @GetMapping("/delete")
    public ResponseEntity<Boolean> getMetaFileWithinDate(@RequestParam String fileLoc) throws Exception {
        log.info("DELETE {}",fileLoc);
        return ResponseEntity.ok(folderExtractionService.deleteFile(fileLoc));
    }

    @GetMapping("/download")
    public ResponseEntity<ApiResponse<String>> downloadImage(@RequestParam String fileLoc) throws IOException {
//        log.info("file {} ",fileLoc);
        if(folderExtractionService.downloadImage(fileLoc))
            return ResponseEntity.ok().body(new ApiResponse<>("Successfully Downloaded the Image in file"));
        else
        {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to Download Image"));
        }
    }
}


