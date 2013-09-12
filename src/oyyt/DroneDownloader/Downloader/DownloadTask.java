/*
 * Copyright (c) 2013 maodatou. All rights reserved.
 * The project is governed by BSD licenses.
 */

package oyyt.DroneDownloader.Downloader;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Download task to handle the information when downloading.
 */
public class DownloadTask {
	private final int threadNum;
    private String url ;
    private String filePath;
    private String fileName;
    private boolean resume;

    private State state = State.INIT;
    private String errorMsg = null;
    private long totalLength = 0;
    private long alreadyDownloaded = 0;
    private long currentDownSize = 0;

    private Downloader downloader = null;

    // for speed check
    private long lastCheckTime = 0;
    private long lastCheckLength = 0;

    /**
     * @param url
     * @param filePath default null
     * @param fileName default null
     * @param resume if resume from file
     */
    public DownloadTask(String url, String filePath, String fileName, boolean resume) {
        this.url = url;
        this.filePath = filePath;
        this.fileName = fileName;
        this.resume = resume;
        this.threadNum = 3;
    }

    public void start() {
        try {
            initURL();
            initFile();
            // TODO: use thread instead of runnable interface
            for (int i = 0; i < threadNum; i++){
            	state = State.DOWNLOADING;
            	System.out.println("i");
            	long startPos = alreadyDownloaded + currentDownSize * i;
            	Downloader downloadThread;
            	System.out.println("startPos: " + startPos);
            	System.out.println(Thread.currentThread().getName());
            	downloadThread= new Downloader(url, filePath + File.separator + fileName, startPos,currentDownSize,totalLength);
            	new Thread(downloadThread).start();
            }
        } catch (DDException e) {
            state = State.ERROR;
            errorMsg = e.getMessage();
        }
    }

    /**
     * get download speed
     * @return speed in bytes
     */
    public int getSpeed() {
        if (downloader == null) {
            return 0;
        }
        int speed;
        long nowTime = System.currentTimeMillis();
        long nowLength = downloader.getDownloadedLength();

        if (lastCheckTime == 0 || lastCheckLength == 0) {
            speed = 0;
        }
        else {
            speed = (int) ((nowLength - lastCheckLength) / (nowTime - lastCheckTime) * 1000);
        }

        lastCheckTime = nowTime;
        lastCheckLength = nowLength;
        return speed;

    }

    /**
     * get download percentage of the task
     * @return percentage in %
     */
    public int getPercentage() {
        if (downloader == null) {
            return 0;
        }
        return (int) ((lastCheckLength + alreadyDownloaded) * 100 / totalLength);
    }

    /**
     * get the state of the task.
     * when state is ERROR or FINISH, the task is ended.
     * @return state of the task
     */
    public State getState() {
        if (downloader == null) {
            return state;
        }
        if (downloader.getState() == State.ERROR) {
            state = State.ERROR;
            errorMsg = "Download failed.";
        }
        else if (downloader.getState() == State.FINISH) {
            state = State.FINISH;
        }
        return state;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * get content length and check the URL is able to be downloaded.
     * @throws DDException
     */
    private void initURL() throws DDException {
        URL URL = null;
        HttpURLConnection conn = null;
        try {
            URL = new URL(url);
            if (fileName == null || fileName.isEmpty()) {
                String urlPath = URL.getPath();
                fileName = urlPath.substring(urlPath.lastIndexOf('/') + 1);
                if (fileName.isEmpty()) {
                    fileName = "index.html";
                }
            }
            conn = (HttpURLConnection) URL.openConnection();
            conn.connect();
            int code = conn.getResponseCode();
            if (code >= 400) {
                // TODO: does it necessary to distinguish different code?
                throw new DDException(ErrorCode.NET_ERROR, "HTTP response code is " + code);
            }
            totalLength = conn.getContentLength();
            if (alreadyDownloaded != 0){
            	currentDownSize = (totalLength - alreadyDownloaded) / threadNum +1;
            }
            else{
            	currentDownSize = totalLength / threadNum + 1;
            	System.out.println("currentDownSize: " + currentDownSize);
            	System.out.println("totalLength: " + totalLength);
            }            
           if (totalLength <= 0) {
                throw new DDException(ErrorCode.NET_ERROR, "Could not get content length");
            }
        } catch (MalformedURLException e) {
            throw new DDException(ErrorCode.ARGS_ERROR, "Not support protocol: " + URL.getProtocol());
        } catch (IOException e) {
            throw new DDException(ErrorCode.NET_ERROR, "Connect error");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }

    /**
     * init filePath, fileName,
     * check file is exists.
     * @throws DDException
     */
    private void initFile() throws DDException {
        if (filePath == null) {
            filePath = ".";
        }
        if (resume) {
            File file = new File(filePath + File.separator + fileName);
            if (!file.exists()) {
                throw new DDException(ErrorCode.ARGS_ERROR, "Could not find file to continue download");
            }
            alreadyDownloaded = file.length();
        }
        else {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!dir.isDirectory()) {
                throw new DDException(ErrorCode.ARGS_ERROR, "File path is illegal: " + dir.getAbsolutePath());
            }
            File file = new File(filePath + File.separator + fileName);
            if (file.exists()) {
                throw new DDException(ErrorCode.ARGS_ERROR, "File is already exists: " + file.getAbsolutePath());
            }
            else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new DDException(ErrorCode.FILE_ERROR, "Could not save file: " + file.getAbsolutePath());
                }
            }
        }
    }
}
