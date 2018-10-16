package com.wordMatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class WordMatch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// In this user have to give the url of the files where the parameter name is filename.
		String matchingWords = findMatchingWord(request);
		response.getWriter().append(matchingWords);
	}
	
	public static String findMatchingWord(HttpServletRequest request){
		String matchWords = "";
		try{
			if(request.getParameterValues("filename") != null){
				String[] listOfFiles = request.getParameterValues("filename");
				if(listOfFiles.length > 1){
					matchWords = getTheCommonWordFromFiles(listOfFiles);
				}else{
					matchWords = "For Comparison minimum two file should be there.";
				}
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return matchWords;
	}
	
	private static String getTheCommonWordFromFiles(String[] listOfFiles) throws IOException {
		// TODO Auto-generated method stub
		String content;
		Map<String, Integer> fileValue = new HashMap<String, Integer>();
		BufferedReader br = null;
		try {
			for (int i=0; i < listOfFiles.length; i++) {
				URL url = new URL(listOfFiles[i]);
				URLConnection urlConnection = url.openConnection();
				br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();
		        while (line != null) {
		        	sb.append(line);
		            sb.append(" ");
		            line = br.readLine();
		        }
		        content = sb.toString();
		        if(i == 0){
		        	String[] arr = content.split(" ");
		        	for(int j=0; j< arr.length; j++){
		        		fileValue.put(arr[j].trim(), 1);
		        	}
		        }
		        if(i != 0){
		        	for (String key : fileValue.keySet()) {
		        		key = key.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");
		        		content = content.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");
		        		if(content.startsWith(key+" ") || content.endsWith(" "+key) || content.contains(" "+key+" ")){
		        			fileValue.put(key, fileValue.get(key) + 1);
		        		}
		        	}
		        }
			}
			boolean anyNoValueIsCommon = true;
			String matchingWord = "";
			for (String key : fileValue.keySet()) {
				if(fileValue.get(key).equals(listOfFiles.length)){
					matchingWord = matchingWord +", " +key;
					anyNoValueIsCommon = false;
				}
			}
			if(anyNoValueIsCommon){
				return "No Word is Common between all the files";
			}else{
				matchingWord = "Matching Words are : "+matchingWord.substring(2, matchingWord.length());
				return matchingWord;
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			br.close();
	    }
		return null;
	}
}