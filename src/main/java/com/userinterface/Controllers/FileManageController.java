package com.userinterface.Controllers;

import com.userinterface.Entities.FileAttributes;
import com.userinterface.Entities.User;
import com.userinterface.Services.FileService;
import com.userinterface.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class FileManageController {
    private UserService userService;
    private FileService fileService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/folder")
    public String showMainPage(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        Stream<Path> files = fileService.getFileList(user.getFolder());
        List<FileAttributes> fileAttributes = files.map(
                path ->  {
                    String filename = path.getFileName().toString();
                    long filesize = 0;
                    try {
                        filesize = Files.size(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String url = MvcUriComponentsBuilder.fromMethodName(FileManageController.class,
                            "downloadFile", path.getFileName().toString()).build().toString();
                    String durl = MvcUriComponentsBuilder.fromMethodName(FileManageController.class,
                            "deleteFile", path.getFileName().toString()).build().toString();
                    return new FileAttributes(filename,filesize, url,durl);
                }
        )
                .collect(Collectors.toList());
        model.addAttribute("files",fileAttributes);
        return "main-page";
    }

    @GetMapping("/folder/delete/{filename}")
    public String deleteFile(@PathVariable String filename){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        fileService.deleteFile(user.getFolder()+"/"+filename);
        return "redirect:/folder";
    }

    @PostMapping("/folder/upload")
    public String uploadFile( Model model, @RequestParam("file") MultipartFile file){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        boolean result = fileService.saveFile(file,user.getFolder());
        return "redirect:/folder";
    }

    @GetMapping("/folder/download/{filename}")
    public ResponseEntity<Resource> downloadFile( @PathVariable String filename){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        Resource file = fileService.loadFile(user.getFolder()+"/"+filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
