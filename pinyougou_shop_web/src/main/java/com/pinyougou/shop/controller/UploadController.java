package com.pinyougou.shop.controller;

import com.pinyougou.utils.FastDFSClient;
import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    @RequestMapping("upload")
    public Result upload(MultipartFile file){
        try {
            //1.原来的文件名,截取获取后缀名
            String oldName = file.getOriginalFilename();
            String extName  = oldName.substring(oldName.lastIndexOf(".") + 1);
            //    通过fastDFS获取文件
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fdfs_client.conf");
            //得到fileID然后拼接
            String fileId = fastDFSClient.uploadFile(file.getBytes(), extName, null);
            String url = FILE_SERVER_URL+ fileId;
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,"上传失败");
    }
}
