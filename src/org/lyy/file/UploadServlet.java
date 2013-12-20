package org.lyy.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;

public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*"); // set headers for cross-domain
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Content-Range, Content-Disposition"); // set headers for chunk upload
		if (!ServletFileUpload.isMultipartContent(request)) {
			throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
		}

		ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
		PrintWriter writer = response.getWriter();
		response.setContentType("application/json");
		JSONObject jsonobj = new JSONObject();
		JSONArray json = new JSONArray();
		try {
			List<FileItem> items = uploadHandler.parseRequest(request);
			for (FileItem item : items) {
				if (!item.isFormField()) { // 判断是否是普通表单域还是文件上传表单域
					File file = new File(request.getSession().getServletContext().getRealPath("/") + "/" + item.getName());
					item.write(file); // 将内容写到文件中
					JSONObject jsono = new JSONObject();
					jsono.put("name", item.getName()); // 文件名称
					jsono.put("size", item.getSize()); // 文件大小
					jsono.put("url", "upload?getfile=" + item.getName());
					jsono.put("thumbnail_url", "upload?getthumb=" + item.getName());
					jsono.put("delete_url", "upload?delfile=" + item.getName());
					jsono.put("delete_type", "GET");
					json.put(jsono);
				}
			}
			jsonobj.put("files", json);
		} catch (FileUploadException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			writer.write(jsonobj.toString());
			writer.close();
		}

	}

}