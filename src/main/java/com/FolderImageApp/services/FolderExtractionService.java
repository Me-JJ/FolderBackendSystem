package com.FolderImageApp.services;

import com.FolderImageApp.dto.CustomMetaData;
import com.FolderImageApp.dto.MetaData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class FolderExtractionService {
    public CustomMetaData getMetaData(String fileLoc, Integer page, Integer size)
    {
        log.info("getMetaData from Loc {}",fileLoc);
        File directory = new File(fileLoc);
        List<MetaData> metaDataList = new ArrayList<>();
        try {
            traverseFilesUnfilterd(directory,metaDataList);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        log.info("images from Loc {}",metaDataList.size());

//        metaDataList.sort(Comparator.comparing(MetaData::getCreatedDate));
        List<MetaData> data;
        if(page*size > metaDataList.size())
        {
            data=metaDataList.subList(metaDataList.size()-size-1,metaDataList.size());
        }
        else
        {
            data=metaDataList.subList(page*size,Math.min(page*size+size,metaDataList.size()));
        }

      log.info("index start  - {} , index end - {}" , page*size,Math.min(page*size+size,metaDataList.size()));
      return new CustomMetaData(data,metaDataList.size());
    }

    private void traverseFilesUnfilterd(File folder,List<MetaData> metaDataList) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        traverseFilesUnfilterd(file,metaDataList); // Recursive call for subdirectories
                    } else {
                        String path = file.getAbsolutePath();
                        try {
                            if (imageFile(path)) {
//                                System.out.println("File: " + path);
                                FileTime creationTime = (FileTime) Files.getAttribute(Path.of(path), "creationTime");
//                                System.out.println("Created At -> " + creationTime.toInstant());
                                LocalDateTime localDateTime = LocalDateTime.ofInstant(creationTime.toInstant(), ZoneId.systemDefault());
                                long diff =localDateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
//                                long timeInMillis =creationTime.toInstant().toEpochMilli();
                                    MetaData metaData = new MetaData(path, localDateTime);
                                    metaDataList.add(metaData);
                            }
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
            }
        }
    }


    private boolean imageFile(String str)
    {
        // Regex to check valid image file extension.
        String regex
                = "([^\\s]+(\\.(?i)(jpe?g|png|gif|bmp))$)";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given string
        // and regular expression.
        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }

    public CustomMetaData getMetaFileWithinDate(String fileLoc, Integer page, Integer size, Long startDate, Long endDate)
    {

        log.info("getMetaDataWithin Date {} & {} from Loc {}",startDate,endDate,fileLoc);
        File directory = new File(fileLoc);
        List<MetaData> metaDataList = new ArrayList<>();
        try {
            traverseFilesFilterd(directory,metaDataList,startDate,endDate);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        log.info("images from Date {} & {} from Loc {} -> , size = {} ",startDate,endDate,fileLoc,metaDataList.size());

//        metaDataList.sort(Comparator.comparing(MetaData::getCreatedDate));

        List<MetaData> data=metaDataList.subList(page*size,Math.min(page*size+size,metaDataList.size()));
        return new CustomMetaData(data,metaDataList.size());
    }

    private void traverseFilesFilterd(File folder,List<MetaData> metaDataList,Long startDate,Long endDate) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        traverseFilesFilterd(file,metaDataList,startDate,endDate); // Recursive call for subdirectories
                    } else {
                        String path = file.getAbsolutePath();
                        try {
                            if (imageFile(path)) {
//                                System.out.println("File: " + path);
                                FileTime creationTime = (FileTime) Files.getAttribute(Path.of(path), "creationTime");
//                                System.out.println("Created At -> " + creationTime.toInstant());
                                LocalDateTime localDateTime = LocalDateTime.ofInstant(creationTime.toInstant(), ZoneId.systemDefault());
                                long diff =localDateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
                                if(startDate<=diff)
                                {
                                    if(endDate==0)
                                    {
                                        MetaData metaData = new MetaData(path, localDateTime);
                                        metaDataList.add(metaData);
                                    }
                                    else if(endDate >= diff)
                                    {
                                        MetaData metaData = new MetaData(path, localDateTime);
                                        metaDataList.add(metaData);
                                    }
                                }
//                                long timeInMillis =creationTime.toInstant().toEpochMilli();
                            }
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
            }
        }
    }

    public Boolean deleteFile(String fileLoc)
    {
        File file = new File(fileLoc);
        if (file.delete()) {
            log.info("File deleted successfully");
            return true;
        }
        else {
            log.info("Failed to delete the file");
            return false;
        }
    }
}
