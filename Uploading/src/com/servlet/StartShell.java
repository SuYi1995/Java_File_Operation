package com.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StartShell extends HttpServlet {

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		 final String [] command= { "/bin/sh", "-c", "/root/xx_svr/start.sh"};
//		final String[] command = { "cmd", "/c", "D:/backend/xx_svr/start.sh" };
		 ExecutorService cachedThreadPool = Executors.newCachedThreadPool();  
        cachedThreadPool.execute(new Runnable() {  
            public void run() {  
            	ShellUtil.executer(command);//ºÄÊ±ÈÎÎñ
            }  
        });  
        cachedThreadPool.shutdown(); 
        try {
			response.getWriter()
					.print("<script type='text/javascript'>history.back()</script>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		// // String [] cmd= { "cmd", "/c", "D:/backend/xx_svr/start.sh"};
		// TODO Auto-generated method stub
		// try {
		// Process process = Runtime.getRuntime().exec(cmd);
		// System.out.println("process:" + process);
		// InputStream is = process.getInputStream();
		// InputStreamReader isr = new InputStreamReader(is);
		// BufferedReader br = new BufferedReader(isr);
		// String s = null;
		// while((s = br.readLine()) != null){
		// System.out.println(s);
		// }

		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } finally {
		// try {
		// response.getWriter()
		// .print("<script type='text/javascript'>history.back()</script>");
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
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