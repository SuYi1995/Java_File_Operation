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

	// ���������ϴ����ļ���չ��
	private String Ext_Name = "doc,txt,sh,json,zip,exe,jar";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String savePath = FILE_PATH;
		System.out.println("����Ŀ¼��" + savePath);
		File saveFileDir = new File(savePath);
		if (!saveFileDir.exists()) {
			// ������ʱĿ¼
			saveFileDir.mkdirs();
		}
		// �ϴ�ʱ������ʱ�ļ�����Ŀ¼
		String tmpPath = FILE_IO_PATH;
		System.out.println("��ʱ�ļ�����Ŀ¼��" + tmpPath);
		File tmpFile = new File(tmpPath);
		if (!tmpFile.exists()) {
			// ������ʱĿ¼
			tmpFile.mkdirs();
		}
		// ��Ϣ��ʾ
		String message = "";
		try {
			// ����һ��DiskFileItemFactory����
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// ���ù����Ļ������Ĵ�С
			factory.setSizeThreshold(1024 * 10);// ���û������Ĵ�СΪ100KB
			// �����ϴ�ʱ���ɵ���ʱ�ļ��ı���Ŀ¼
			factory.setRepository(tmpFile);
			// ����һ���ļ��ϴ�������
			ServletFileUpload upload = new ServletFileUpload(factory);
			// �����ļ��ϴ�����
			upload.setProgressListener(new ProgressListener() {
				public void update(long readedBytes, long totalBytes,
						int currentItem) {
					// TODO Auto-generated method stub
					System.out.println("��ǰ�Ѵ���" + readedBytes
							+ "-----------�ļ���СΪ��" + totalBytes + "--"
							+ currentItem);
				}
			});
			// ����ϴ��ļ�����������������
			upload.setHeaderEncoding("UTF-8");
			// �ж��ύ�����������Ƿ����ϴ���������
			if (!ServletFileUpload.isMultipartContent(request)) {
				return;
			}
			// �����ϴ������ļ������ֵ
			upload.setFileSizeMax(1024 * 1024 * 100);// 100M
			// ���ñ����ϴ��������ļ����ܺ͵����ֵ
			upload.setSizeMax(1024 * 1024 * 500);// 500M
			List items = upload.parseRequest(request);
			Iterator itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				// ��ͨ������
				if (item.isFormField()) {
//					String name = item.getFieldName();
//					// �������������������
//					String value = item.getString("UTF-8");
//					System.out.println(name + "=" + value);
					continue;
				} else
				// �ϴ��ļ�
				{
					// �õ��ϴ��ļ����ļ���
					String fileName = item.getName();
					System.out.println("�ļ�����" + fileName);
					if (fileName == null && fileName.trim().length() == 0) {
						continue;
					}
					// �����ļ�������
					fileName = fileName
							.substring(fileName.lastIndexOf("\\") + 1);
					System.out.println("�ļ������֣�" + fileName);
					// �õ��ϴ��ļ�����չ��
					String fileExt = fileName.substring(
							fileName.lastIndexOf(".") + 1).toLowerCase();
					System.out.println("�ϴ����ļ�����չ���ǣ�" + fileExt);
					// �����չ��
					if (!Ext_Name.contains(fileExt)) {
						System.out.println("�ϴ��ļ���չ���ǲ��������չ����" + fileExt);
						message = message + "�ļ���" + fileName
								+ "���ϴ��ļ���չ���ǲ��������չ����" + fileExt + "<br/>";
						break;
					}
					// ����ļ���С
					if (item.getSize() == 0) {
						continue;
					}
					if (item.getSize() > 1024 * 1024 * 100) {
						System.out.println("�ϴ��ļ���С��" + item.getSize());
						message = message + "�ļ���" + fileName + "���ϴ��ļ���С�������ƴ�С��"
								+ upload.getFileSizeMax() + "<br/>";
						break;
					}
					// �õ����ļ����ļ���
					String saveFileName = fileName;
					System.out.println("���ļ����ļ�����" + saveFileName);
					// ��ȡ�ϴ��ļ���������
					InputStream is = item.getInputStream();
					// ����һ���ļ������
					FileOutputStream out = new FileOutputStream(savePath + "/"
							+ saveFileName);
					// ����һ��������
					byte buffer[] = new byte[1024];
					// �ж��������е������Ƿ��Ѿ�����ı���
					int len = 0;
					while ((len = is.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}
					// �ر������
					out.close();
					// �ر�������
					is.close();
					// ɾ����ʱ�ļ�
					item.delete();
					message = message + "�ļ���" + fileName + "���ϴ��ɹ�<br/>";
				}
			}
		} catch (FileSizeLimitExceededException e) {
			message = message + "�ϴ��ļ���С��������<br/>";
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