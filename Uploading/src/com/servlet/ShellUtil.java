package com.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
 
public class ShellUtil {
	private static final int SUCCESS = 0;	
    private static final String SUCCESS_MESSAGE = "successful";
    private static final String ERROR_MESSAGE = "fail";
    /**
     * 
    * @Title: executer 
    * @Description: TODO
    * @param command
    * @return
     */
    public static boolean executer(String[] command){
        try{
        	Process process=Runtime.getRuntime().exec(command);
            readProcessOutput(process);
            // �ȴ�����ִ�н��������״̬
            int exitCode = process.waitFor();
            if (exitCode == SUCCESS) {
                System.out.println(SUCCESS_MESSAGE);
                return true;
            } else {
                System.err.println(ERROR_MESSAGE + "==========>" +exitCode);
            }
        }catch(Exception e){
        	e.printStackTrace();
        }
        return false;
    }
    
    /**
     * �����ӡ��Ϣ
    * @Title: readProcessOutput 
    * @Description: TODO
    * @param process
     */
    private static void readProcessOutput(final Process process) {
	        // �����̵���������� System.out �д�ӡ�����̵Ĵ�������� System.err �д�ӡ
	        read(process.getInputStream(), System.out);
	        read(process.getErrorStream(), System.err);
	    }
    
    private static void  read(InputStream inputStream, PrintStream out) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                out.println(line);
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
 
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
