package com.zhangheng.happy_shopping.controller;


import com.zhangheng.happy_shopping.utils.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/*文件加载控制器*/
@RequestMapping("fileload")
@Controller
public class FileLoadController {
    private static String baseDir = "files/";
    Logger log = LoggerFactory.getLogger(FileLoadController.class);
    private ArrayList<Object> update_files = new ArrayList<>();//更新文件集合


    /**
     * 保存图片，并返回保存路径，(如果返回为null则保存失败)
     * @param img 图片文件
     * @param imgName 图片名称
     * @param type 图片类型（父级文件夹名称）
     * @return 返回保存路径，如果返回为null则保存失败
     */
    public String saveImage(MultipartFile img, String imgName, String type){
        String path=null;
        //图片不为空
        if (!img.isEmpty()){
            //图片小于2Mb
            if (img.getSize()<2080000) {
                String filename = img.getOriginalFilename();
                log.info("图片名：{}", filename);
                log.info("图片大小：{}kb", img.getSize()/1024);
                filename = filename.replaceAll(" ", "");
                //判断文件格式是否为图片
                if (FiletypeUtil.getFileType(filename).equals("image")) {
                    //判断文件名长度
                    imgName=imgName.length()<8?imgName:imgName.substring(0,8);
                    //保存文件名
                    String name = type+"/乐在购物网"
                            + UUID.randomUUID().toString().substring(0, 5)
                            + "_" + imgName+filename.substring(filename.lastIndexOf("."));
                    File outFile = new File(baseDir + name);
                    try {
                        FileUtils.copyToFile(img.getInputStream(), outFile);
                        path = name;
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                } else {
                    log.error("上传文件类型错误");

                }
            }else {
                log.error("上传图片大小超过2Mb");

            }
        }else {
            log.error("保存图片为空");

        }
        return path;
    }

    /**
     * 保存base64的图片
     * @param base64 base64字符
     * @param fileName 文件名
     * @param savePath 保存路径文件夹
     * @return 返回保存路径，如果返回为null则保存失败
     */
    public String base64ToImg(String base64, String fileName, String savePath) {
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
            //构造文件名
            String name="/乐在购物网"
                    + UUID.randomUUID().toString().substring(0, 5)
                    + "_" + fileName;
            base64 = split[1];
            byte[] bytes = Base64.getDecoder().decode(base64);
            file=new File(filePath + name + type);
            fos = new java.io.FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            path = savePath+name+type;
            log.info("图片名：{}", path);
            log.info("图片大小：{}kb", Message.twoDecimalPlaces((double) file.length()/1024));
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

    /**
     * 删除旧图片
     * @param path 图片路径
     * @return is 否删除成功
     */
    public boolean deleteImg(String path){
        boolean is=false;
        File file = new File(baseDir+path);
        if (file.exists()){
            boolean delete = file.delete();
            if (delete) {
                log.info("旧图片删除成功："+path);
                is=true;
            }else {
                log.error("旧图片删除失败："+path);
            }
        }else {
            log.error("旧图片不存在："+path);
        }
        return is;
    }


    /**
     * 文件下载请求
     * @param name 文件名
     * @param type 文件类型（父级文件夹名称）
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("show/{type}/{name:.+}")
    public void show(@PathVariable("name") String name,
                     @PathVariable("type") String type, HttpServletRequest request,
                     HttpServletResponse response) throws IOException {
        String user_agent = CusAccessObjectUtil.getUser_Agent(request);
        String ipAddress = CusAccessObjectUtil.getIpAddress(request);
//        log.info("下载请求头："+user_agent);
//        log.info("下载IP："+ipAddress);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        File file = new File(baseDir + type+"/"+name);
        FileInputStream input = null;
        try {
            //显示文件大小
            response.setHeader("Content-Length", String.valueOf(file.length()));
            input = FileUtils.openInputStream(file);
            IOUtils.copy(input, response.getOutputStream());
//            log.info("下载请求成功:"+file.getPath());
        } catch (Exception e) {
            if (e.toString().indexOf("does not exist")>1){
//                response.sendError(404,"没有找到文件");
//                log.error("没有找到文件");
            }else {
//                response.sendError(500,e.getMessage());
            }
            log.error("错误："+e.getMessage());
        }finally {
            try {
                input.close();
            }catch (Exception e){
                log.error(e.getMessage());
//                response.sendError(500,e.getMessage());
            }
        }
    }

    /**
     * 更新下载(update目录下)
     * @param type 更新文件所在的文件夹名称
     * @param name 更新文件名称
     * @param response
     * @return
     * @throws IOException
     */
    @ResponseBody
    @GetMapping("update/{type}/{name}")
    public Message downupdate(@Nullable @PathVariable("type") String type,
                              @Nullable @PathVariable("name") String name,
                              HttpServletResponse response) throws IOException {
        Message msg=new Message();
        if (type.length()>0){
            File file = new File(baseDir+"update/"+type+"/"+name);
            log.info("更新下载："+file.getPath());
            FileInputStream input = null;
            try {
                //显示文件大小
                response.setHeader("Content-Length", String.valueOf(file.length()));
                input = FileUtils.openInputStream(file);
                IOUtils.copy(input, response.getOutputStream());
                msg.setCode(200);
                msg.setTime(TimeUtil.time(new Date()));
                msg.setTitle("请求成功");
                msg.setMessage(file.getName());
                log.info("更新下载请求成功");
            } catch (Exception e) {
                if (e.getMessage().indexOf("does not exist")>1){
                    msg.setCode(500);
                    msg.setTime(TimeUtil.time(new Date()));
                    msg.setTitle("错误:404（没找到）");
                    msg.setMessage("对不起，没有找到要更新的文件");
                }else {
                    msg.setCode(500);
                    msg.setTime(TimeUtil.time(new Date()));
                    msg.setTitle("错误");
                    msg.setMessage(e.getMessage());
                }
                log.error("错误："+e.getMessage());
            }finally {
                try {
                    input.close();
                }catch (Exception e){
                    log.error(e.getMessage());
                }
            }
        }else {
            msg.setCode(500);
            msg.setTime(TimeUtil.time(new Date()));
            msg.setTitle("内容为空");
            msg.setMessage("请求内容不能为空");

        }
        return msg;
    }

    /**
     * 更新查询(update目录下)
     * @param type 更新文件所在的文件夹名称
     * @param request
     * @return
     */
    @PostMapping("updatelist/{type}")
    @ResponseBody
    public Message updatelist(@Nullable @PathVariable("type") String type,HttpServletRequest request){
        Message msg=new Message();
        ArrayList<String> list = new ArrayList<>();
        list.clear();
        log.info("应用更新查询:"+CusAccessObjectUtil.getRequst(request));
        if (type.length()>0){
            try {
                update_files.clear();
                update_files = FolderFileScanner.scanFilesWithRecursion(baseDir+"update\\"+type);
                if (!update_files.isEmpty()) {
                    for (Object o : update_files) {
                        String s1 = "";
                        String s = String.valueOf(o);
                        if (s.indexOf("update") > 1) {
                            s1 = s.substring(s.indexOf("update"));
                        } else {
                            s1 = s;
                        }
                        s1=s1.replace("\\","/");
                        list.add(s1);
                    }
                    if (list.size()>0){//判断文件夹是否为空
                        msg.setCode(200);
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setTitle(type);
                        msg.setMessage(list.get(list.size()-1));
                    }else {
                        msg.setTime(TimeUtil.time(new Date()));
                        msg.setCode(500);
                        msg.setTitle("null");
                        msg.setMessage("没有找到对应的更新");
                    }
                }else {
                    msg.setTime(TimeUtil.time(new Date()));
                    msg.setCode(500);
                    msg.setTitle("null");
                    msg.setMessage("更新文件为空");
                }
//                    log.info("更新查询title:"+result.getTitle());
                log.info("应用更新查询:"+msg.getMessage());

            } catch (Exception e) {
                if (e.getMessage().indexOf("路径错误或没有此文件夹")>1){
                    msg.setTime(TimeUtil.time(new Date()));
                    msg.setCode(500);
                    msg.setTitle("路径错误或没有此文件夹");
                    msg.setMessage(e.getMessage());
                }else {
                    msg.setTime(TimeUtil.time(new Date()));
                    msg.setCode(500);
                    msg.setTitle("错误");
                    msg.setMessage(e.getMessage());
                }
//                    e.printStackTrace();
                log.error("update查询错误："+e.getMessage());
            }
        }else {
            msg.setTime(TimeUtil.time(new Date()));
            msg.setCode(500);
            msg.setTitle("内容为空");
            msg.setMessage("请求内容不能为空");
        }
        return msg;

    }
}
