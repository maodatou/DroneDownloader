package kernel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Download {
    public String userUrl = null;
    public String saveAdd = null;
    public String fileName = null;
    boolean fileNameIndex = true;

    Download(String userUrl, String saveAdd, String fileName,
            boolean fileNameIndex) {
        this.userUrl = userUrl;
        this.saveAdd = saveAdd;
        this.fileName = fileName;
        this.fileNameIndex = fileNameIndex;
    }

    public void download() {
        try {
            if(this.userUrl == null)
                return;
            URL url = new URL(this.userUrl);
            String path = url.getFile();
            if (this.fileNameIndex) {
                this.fileName = path.substring(path.lastIndexOf("/") + 1,
                        path.length());
            }
            URLConnection con = url.openConnection();
            InputStream in = url.openStream();
            File file = new File(this.saveAdd, this.fileName);
            FileOutputStream out = new FileOutputStream(file);
            int len = con.getContentLength();
            byte[] buffer = new byte[len];
            int length = 0;
            while (length != len) {
                length += in.read(buffer, length, len - length);
            }
            out.write(buffer);
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
