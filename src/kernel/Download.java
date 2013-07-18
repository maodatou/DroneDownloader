package kernel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Download {
    int index;
    public String userUrl = null;
    public String saveAdd = null;
    public String fileName = null;
    boolean fileNameIndex = true;
    boolean saveAddIndex = true;
    final static int len = 1024;

    Download(String userUrl, String saveAdd, String fileName,
            boolean fileNameIndex, boolean saveAddIndex) {
        this.userUrl = userUrl;
        this.saveAdd = saveAdd;
        this.fileName = fileName;
        this.fileNameIndex = fileNameIndex;
        this.saveAddIndex = saveAddIndex;
    }

    public void downMain() {
        try {
            File file = new File(this.saveAdd, this.fileName);
            if (file.exists()) {
                long localFileSize = file.length();
                if (localFileSize > 0) {
                    download(localFileSize, 0);
                } else {
                    download(0, 0);
                }
            } else {
                file.createNewFile();
                download(0, 0);
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public void download(long start, int index) {
        if (index == 2) {
            System.out.println("Download failure!");
            System.exit(0);
        }
        try {
            if (this.userUrl == null)
                return;
            URL url = new URL(this.userUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            byte startbyte = (byte) start;
            connection.setConnectTimeout(10000);
            connection.setRequestProperty("RANGE", "bytes=" + startbyte);
            connection.connect();
            int code = connection.getResponseCode();
            if (Integer.toString(code).startsWith("4")
                    || Integer.toString(code).startsWith("5")) {
                System.out.println("failure code:" + code);
            }
            int allSize = connection.getContentLength();
            String path = url.getFile();
            if (this.fileNameIndex) {
                this.fileName = path.substring(path.lastIndexOf("/") + 1,
                        path.length());
            }
            InputStream in = url.openStream();
            String fileNameTemp = "";
            if (!this.saveAddIndex) {
                fileNameTemp = this.saveAdd + this.fileName;
            } else {
                fileNameTemp = this.fileName;
            }
            RandomAccessFile existedFile = new RandomAccessFile(fileNameTemp,
                    "rw");
            existedFile.seek(start);
            byte[] buffer = new byte[len];
            int hasRead;
            int fileTempSize = 0, tempSize = 0;
            long startTime = 0, tempTime;
            System.out.println("Has downloaded:");
            while ((startTime = System.nanoTime()) != 0
                    && (hasRead = in.read(buffer, 0, len)) > 0) {
                fileTempSize += hasRead;
                tempSize += hasRead;
                existedFile.write(buffer, 0, hasRead);
                tempTime = System.nanoTime() - startTime;
                // ns to s
                double time = tempTime / 1000.0 / 1000.0 / 1000.0;
                // byte/s to kb/s
                double speed = tempSize / 1024.0 / time;
                speed = (int) speed;
                if (tempTime >= 1 * 1000 * 100) {
                    System.out.print("\r");
                    System.out.print(fileTempSize * 100 / allSize + "%" + "  "
                            + speed + "kb/s");
                }
                tempSize = 0;
                tempTime = 0;
            }
            fileTempSize = 0;
            in.close();
            existedFile.close();
            connection.disconnect();

        } catch (Exception e) {
            // e.printStackTrace();

            System.out.println("During download ing!");
            download(start, ++index);

        }
        System.out.println();
        System.out.println("Success download!");
    }

}
