package com.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String FILE_PATH = "D:/TEST";
	public static final String FILE_IO_PATH = "D:/TEST_IO";

	// 定义允许上传的文件扩展名
	private String Ext_Name = "doc,txt,sh,json,zip,exe,jar";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String savePath = FILE_PATH;
		System.out.println("保存目录：" + savePath);
		File saveFileDir = new File(savePath);
		if (!saveFileDir.exists()) {
			// 创建临时目录
			saveFileDir.mkdirs();
		}
		// 上传时生成临时文件保存目录
		String tmpPath = FILE_IO_PATH;
		System.out.println("临时文件保存目录：" + tmpPath);
		File tmpFile = new File(tmpPath);
		if (!tmpFile.exists()) {
			// 创建临时目录
			tmpFile.mkdirs();
		}
		// 消息提示
		String message = "";
		try {
			// 创建一个DiskFileItemFactory工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置工厂的缓冲区的大小
			factory.setSizeThreshold(1024 * 10);// 设置缓冲区的大小为100KB
			// 设置上传时生成的临时文件的保存目录
			factory.setRepository(tmpFile);
			// 创建一个文件上传解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 监听文件上传进度
			upload.setProgressListener(new ProgressListener() {
				public void update(long readedBytes, long totalBytes,
						int currentItem) {
					// TODO Auto-generated method stub
					System.out.println("当前已处理：" + readedBytes
							+ "-----------文件大小为：" + totalBytes + "--"
							+ currentItem);
				}
			});
			// 解决上传文件名的中文乱码问题
			upload.setHeaderEncoding("UTF-8");
			// 判断提交上来的数据是否是上传表单的数据
			if (!ServletFileUpload.isMultipartContent(request)) {
				return;
			}
			// 设置上传单个文件的最大值
			upload.setFileSizeMax(1024 * 1024 * 100);// 100M
			// 设置本次上传的所有文件的总和的最大值
			upload.setSizeMax(1024 * 1024 * 500);// 500M
			List items = upload.parseRequest(request);
			Iterator itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				// 普通的输入
				if (item.isFormField()) {
//					String name = item.getFieldName();
//					// 解决数据中文乱码问题
//					String value = item.getString("UTF-8");
//					System.out.println(name + "=" + value);
					continue;
				} else
				// 上传文件
				{
					// 得到上传文件的文件名
					String fileName = item.getName();
					System.out.println("文件名：" + fileName);
					if (fileName == null && fileName.trim().length() == 0) {
						continue;
					}
					// 保留文件名部分
					fileName = fileName
							.substring(fileName.lastIndexOf("\\") + 1);
					System.out.println("文件名部分：" + fileName);
					// 得到上传文件的扩展名
					String fileExt = fileName.substring(
							fileName.lastIndexOf(".") + 1).toLowerCase();
					System.out.println("上传的文件的扩展名是：" + fileExt);
					// 检查扩展名
					if (!Ext_Name.contains(fileExt)) {
						System.out.println("上传文件扩展名是不允许的扩展名：" + fileExt);
						message = message + "文件：" + fileName
								+ "，上传文件扩展名是不允许的扩展名：" + fileExt + "<br/>";
						break;
					}
					// 检查文件大小
					if (item.getSize() == 0) {
						continue;
					}
					if (item.getSize() > 1024 * 1024 * 100) {
						System.out.println("上传文件大小：" + item.getSize());
						message = message + "文件：" + fileName + "，上传文件大小超过限制大小："
								+ upload.getFileSizeMax() + "<br/>";
						break;
					}
					// 得到存文件的文件名
					String saveFileName = fileName;
					System.out.println("存文件的文件名：" + saveFileName);
					// 获取上传文件的输入流
					InputStream is = item.getInputStream();
					// 创建一个文件输出流
					FileOutputStream out = new FileOutputStream(savePath + "/"
							+ saveFileName);
					// 创建一个缓冲区
					byte buffer[] = new byte[1024];
					// 判断输入流中的数据是否已经读完的标致
					int len = 0;
					while ((len = is.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}
					// 关闭输出流
					out.close();
					// 关闭输入流
					is.close();
					// 删除临时文件
					item.delete();
					message = message + "文件：" + fileName + "，上传成功<br/>";
				}
			}
		} catch (FileSizeLimitExceededException e) {
			message = message + "上传文件大小超过限制<br/>";
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			request.setAttribute("message", message);
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}