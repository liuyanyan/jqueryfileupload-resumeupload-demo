package org.lyy.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
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

public class ChunkedUploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private File uploadFile;
    private RandomAccessFile rafile;
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 */
    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.addHeader("Access-Control-Allow-Origin", "*"); // set headers for cross-domain
		response.setHeader("Access-Control-Allow-Headers","Content-Type, Content-Range, Content-Disposition"); // 设置允许分块上传的头信息
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
        }
        String contentDisposition = request.getHeader("Content-Disposition");
        if(contentDisposition == null){
        	throw new RuntimeException("Not chunked upload!");
        }
        
        System.out.println("Content-Disposition : " + contentDisposition);
        initFile(contentDisposition, request.getSession().getServletContext().getRealPath("/") + "/");
        
        // 获取文件总大小，用于判断
        int totalSize = getTotalSize(request.getHeader("Content-Range"));
         
        ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        JSONObject jsonobj = new JSONObject();
        JSONArray json = new JSONArray();
        try {
            List<FileItem> items = uploadHandler.parseRequest(request);
            for (FileItem item : items) {
                if (!item.isFormField()) { // 判断是否是普通表单域还是文件上传表单域
                        if(rafile.length() < totalSize){
                        	rafile.seek(rafile.length());
                        	rafile.write(item.get());
                        }
                        JSONObject jsono = new JSONObject();
                        jsono.put("name", item.getName()); // filename
                        jsono.put("size", item.getSize()); // filesize
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

	private int getTotalSize(String contentRangeStr) {
		
        if(contentRangeStr == null){
        	throw new RuntimeException("Not chunk upload!");
        }
        String totalSize = contentRangeStr.substring(contentRangeStr.lastIndexOf("/") + 1);
        return Integer.parseInt(totalSize);
	}

	private void initFile(String contentDisposition, String path) throws FileNotFoundException {
        	String fileNames = contentDisposition.substring("attachment; filename=".length());
			String filename = fileNames.substring(fileNames.indexOf("\"") + 1, fileNames.lastIndexOf("\""));
        	uploadFile = new File( path+ filename);
            rafile = new RandomAccessFile(uploadFile,"rw");
	}

}