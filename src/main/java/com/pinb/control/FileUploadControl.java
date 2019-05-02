/**
 * 
 */
package com.pinb.control;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pinb.common.ServiceException;
import com.pinb.enums.RespCode;
import com.pinb.util.PropertiesUtils;
import com.pinb.util.RespUtil;

/**
 * @author chenzhao @date Apr 28, 2019
 */
@RestController
public class FileUploadControl {

	private static final Logger log = LoggerFactory.getLogger(FileUploadControl.class);

	@RequestMapping("fileUpload")
	public Object fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("fileMd5") String fileMd5,HttpServletRequest req)
			throws Exception {
		if (file.isEmpty()) {
			throw new ServiceException(RespCode.file_unExist);
		}
		if (StringUtils.isEmpty(fileMd5)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "fileMd5");
		}
		
		String imagesPath = PropertiesUtils.getProperty("images.path", "/data/pinb/images/");
		String oldFileName = file.getOriginalFilename();
		String newFileName = fileMd5 + oldFileName.substring(oldFileName.lastIndexOf("."), oldFileName.length());
		if (System.getProperty("user.dir").contains(":")) {
			// windows 环境
			imagesPath = System.getProperty("user.dir").substring(0, 2) + imagesPath;
		}
		log.debug("#oldFileName:[{}]，#newFileName:[{}]，#imagesPath:[{}]", oldFileName, newFileName, imagesPath + newFileName);
		File newFile = new File(imagesPath + newFileName);
		if (newFile.exists()) {
			log.debug("#文件已存在服务器，上传成功,#path:[{}]", newFile.getPath());
			return RespUtil.dataResp(newFileName);
		}

		if (!newFile.getParentFile().exists()) { // 判断文件父目录是否存在
			newFile.getParentFile().mkdirs();
		}
		// #计算文件流md5
		String newFileMd5 = DigestUtils.md5DigestAsHex(file.getBytes());
		if (!fileMd5.equalsIgnoreCase(newFileMd5)) {
			throw new ServiceException(RespCode.file_fileMd5Error, fileMd5, newFileMd5);
		}
		file.transferTo(newFile); // 保存文件
		return RespUtil.dataResp(newFileName);
	}

	public static void main(String[] args) {
		String oldFileName = "sldfjsdlj.xml";
		String newFileName = "2453" + oldFileName.substring(oldFileName.indexOf("."), oldFileName.length());
		System.out.println(System.getProperty("user.dir").substring(0, 2) + newFileName);
	}

}
