package com.zhangheng.happy_shopping.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

public class FiletypeUtil {
    private static final String[][] MIME_MapTable={
            //{后缀名，    类型}
            {".3gp",    "video"},
            {".apk",    "application"},
            {".asf",    "video"},
            {".avi",    "video"},
            {".bin",    "application"},
            {".bmp",      "image"},
            {".c",        "text"},
            {".class",    "application"},
            {".conf",    "text"},
            {".cpp",    "text"},
            {".doc",    "text"},
            {".exe",    "application"},
            {".gif",    "image"},
            {".gtar",    "application"},
            {".gz",        "application"},
            {".h",        "text"},
            {".htm",    "text"},
            {".html",    "text"},
            {".jar",    "application"},
            {".java",    "text"},
            {".jpeg",    "image"},
            {".jpg",    "image"},
            {".js",        "text"},
            {".log",    "text"},
            {".m3u",    "audio"},
            {".m4a",    "audio"},
            {".m4b",    "audio"},
            {".m4p",    "audio"},
            {".m4u",    "video"},
            {".m4v",    "video"},
            {".mov",    "video"},
            {".mp2",    "audio"},
            {".mp3",    "audio"},
            {".mp4",    "video"},
            {".mpc",    "application"},
            {".mpe",    "video"},
            {".mpeg",    "video"},
            {".mpg",    "video"},
            {".mpg4",    "video"},
            {".mpga",    "audio"},
            {".msg",    "application"},
            {".ogg",    "audio"},
            {".pdf",    "text"},
            {".png",    "image"},
            {".pps",    "application"},
            {".ppt",    "text"},
            {".xls",    "text"},
            {".xlsx",    "text"},
            {".docx",    "text"},
            {".md",    "text"},
            {".prop",    "text"},
            {".rar",    "application"},
            {".rc",        "text"},
            {".rmvb",    "audio"},
            {".aac",    "audio"},
            {".rtf",    "application"},
            {".sh",        "text"},
            {".tar",    "application"},
            {".tgz",    "application"},
            {".txt",    "text"},
            {".wav",    "audio"},
            {".wma",    "audio"},
            {".wmv",    "audio"},
            {".wps",    "application"},
            {".xml",    "text"},
            {".z",        "application"},
            {".zip",    "application"},
            {"",        "other"}
    };
    public static String getFileType(String filename)
    {
        String type="other";
        String fName=filename;
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0){
            return type;
        }
        /* 获取文件的后缀名 */
        String end=fName.substring(dotIndex,fName.length()).toLowerCase();
        if(end=="")return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<MIME_MapTable.length;i++){
            if(end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }
    private static String baseDir = "files/";
    public static String base64ToImg(String base64, String fileName, String savePath) {
        File file = null;
        String path=null;
        //创建文件目录
        String filePath = baseDir+savePath;
        File dir = new File(filePath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = null;
        java.io.FileOutputStream fos = null;
        try {
            String[] split = base64.split(",");
            String type="."+split[0].split("/")[1].split(";")[0];
            //判断文件名长度
            fileName=fileName.length()<8?fileName:fileName.substring(0,8);
            String name="/乐在购物网"
                    + UUID.randomUUID().toString().substring(0, 5)
                    + "_" + fileName;
            base64 = split[1];
            byte[] bytes = Base64.getDecoder().decode(base64);
            file=new File(filePath + name + type);
            fos = new java.io.FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            path = file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return path;
    }
}
