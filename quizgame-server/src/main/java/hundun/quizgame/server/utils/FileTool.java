package hundun.quizgame.server.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;


/**
 *
 * @author hundun
 * Created on 2019/03/14
 */
@Slf4j
public class FileTool {
	
	
	public static void putFileToResponse(HttpServletResponse response, File file, String fileName) throws Exception {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            FileInputStream inputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int i;
            while ((i = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }
            outputStream.flush();
            response.flushBuffer();
            bufferedInputStream.close();
        } catch (IOException e) {
            log.error("loadImage() 下载图片时异常 ===> ", e);
            throw e;
        }
	}
	
	
	private static void folderCheck(String folderPath) {
		File file =new File(folderPath);       
		if (!file.exists()  && !file.isDirectory()) {       
		    if (file.mkdirs()) {
		    	log.info("成功新建" + folderPath + "文件夹");
		    } else {
		        log.error("未能新建" + folderPath + "文件夹");
		    }
		}
	}
	
	public static String saveFileByPathAndName(MultipartFile multipartFile, String path, String fileName) {
		String msg;
		
		folderCheck(path);
		
		String filePathName = path + fileName;
		
		if (multipartFile == null || multipartFile.isEmpty()) {
            msg = "请求未包含文件";
            log.warn(msg);
            return msg;
		}
		
		writeMultiPartFile(multipartFile, filePathName);
		
		msg = filePathName + "已保存";
		log.info(msg);
        return msg;
	}
	
	private static boolean writeMultiPartFile(MultipartFile multipartFile,String path) {
		//write file to local
		try {
			byte[] bytes = multipartFile.getBytes();
			Path multipartFilePath = Paths.get(path);
			Files.write(multipartFilePath, bytes);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

}
