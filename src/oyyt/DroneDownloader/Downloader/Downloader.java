/*
 * Copyright (c) 2013 maodatou. All rights reserved.
 * The project is governed by BSD licenses.
 */

package oyyt.DroneDownloader.Downloader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * do the actual download.
 * it's a independent thread.
 */
class Downloader implements Runnable {
    private final static int BUFFER_SIZE = 2 << 12;
    private final static int RETRY_TIMES = 3;
    private final static int TIME_OUT = 30;

    private final String url;
    private final String fileAbsPath;
    private final long startPos;
    private final long length;
   

    private volatile long curPos;
    private long fileLength;
    private volatile State state = State.INIT;

    Downloader(String url, String fileAbsPath, long startPos, long length,long fileLength) {
        this.url = url;
        this.fileAbsPath = fileAbsPath;
        this.startPos = startPos;
        this.length = length;
        this.curPos = startPos;
        this.fileLength = fileLength;
    }

    @Override
    public void run() {
        download();
        
    }

    public State getState() {
        return state;
    }

    public long getDownloadedLength() {
        return curPos - startPos;
    }

    private void download() {
        int retry = 0;
        state = State.DOWNLOADING;
        System.out.println(Thread.currentThread().getName());
        do {
            try {
                URL URL = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) URL.openConnection();
                conn.setConnectTimeout(TIME_OUT);
                long end = startPos + length;
                if (startPos + length > fileLength){
                    end = fileLength;
                    System.out.println("end:"+end);
                }
                System.out .println("curPos: " + curPos + "end: " + end + "Thread" + Thread.currentThread().getName());
                conn.setRequestProperty("Range", "bytes=" + curPos + "-" + end);
                conn.connect();

                RandomAccessFile file = new RandomAccessFile(fileAbsPath, "rw");
                file.seek(curPos);

                // use java.nio to get more speed.
                ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                ReadableByteChannel in = Channels.newChannel(conn.getInputStream());
                FileChannel out = file.getChannel();

                while(in.read(buffer) != -1) {
                    buffer.flip();
                    curPos += out.write(buffer);
                    buffer.clear();
                }
/*
                if (curPos == startPos + length) {
                    state = State.FINISH;
                    break;
                }
                */

                out.close();
                in.close();
                file.close();
                conn.disconnect();

            } catch (MalformedURLException e) {
                // continue
            } catch (IOException e) {
                // continue
            } finally {
                retry ++;
            }
        } while(retry < RETRY_TIMES);

        if (state != State.FINISH) {
            state = State.ERROR;
        }
    }
}
