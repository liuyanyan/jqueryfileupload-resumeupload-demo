package org.lyy.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class ResumePointServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private File uploadFile;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*"); // set headers for cross-domain
		response.setHeader("Access-Control-Allow-Headers","Content-Type, Content-Range, Content-Disposition");
		String filename = request.getParameter("file");
		uploadFile = new File(request.getSession().getServletContext().getRealPath("/") + "/" + filename);
		if (uploadFile.exists()) {

		}
		PrintWriter writer = response.getWriter();
		response.setContentType("application/json");
		JSONObject jsonobj = new JSONObject();
		try {
			JSONObject jsono = new JSONObject();
			jsono.put("size", uploadFile.length()); // file size to indicate the resume point
			jsonobj.put("file", jsono);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			writer.write(jsonobj.toString());
			writer.close();
		}

	}

}