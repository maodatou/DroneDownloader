/**
 * copy Right Information: baobao
 * Project: DD
 * JDK version used: JDK1.7
 * Version: 1.0
 * Modification history: 2013.7.3 
 **/
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
/**
 * 
 * @author OYYT
 * 
 */
public class DroneDownloader {
	public static void main(String[] args){
		String userUrl = null;
		String saveAdd = null;
		String fileName = null;
		String userData = null;
		boolean fileNameIndex = true;
		Scanner userInput = new Scanner(System.in);
        if (args.length > 0){
        	userUrl = args[0];
        	if (args.length > 1){
        		saveAdd = args[1];
        		if (args.length > 2){
        			fileName = args[2];
        			fileNameIndex = false;
        		}
        	}
        	else{
        		//default address
        		saveAdd = "E:\\Workspace\\DroneDownloader";
        	}
        }
        else{
        	System.out.println("The correct input format is: \"url + save address(Optional) + fileName(Optional)\" ");
            return;
        }
        
		try{
			URL url = new URL(userUrl);
			String path = url.getFile();
			if (fileNameIndex)
				fileName = path.substring(path.lastIndexOf("/") + 1, path.length());
			URLConnection con = url.openConnection();
			InputStream in = url.openStream();
			File file = new File(saveAdd,fileName);
			FileOutputStream out = new FileOutputStream(file);
			int len = con.getContentLength();
			byte[] buffer = new byte[len];
			int length = 0;
			while (length != len) {
				length += in.read(buffer, length, len-length);
			}
			out.write(buffer);
			in.close();
			out.close();
		}
		catch (Exception e){
			e.printStackTrace();	
		}
	}
}
