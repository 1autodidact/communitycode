package com.wenmrong.community1.community.controller;

import com.wenmrong.community1.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {
<<<<<<< HEAD
    @RequestMapping("/file/upload")
=======
    @RequestMapping("file/upload")
>>>>>>> 3fce48ca39cd7d3d25501ecff97e6b3228d3e7e9
    @ResponseBody
    public FileDTO upload(){
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/iceman.png");
        return fileDTO;
    }

}
