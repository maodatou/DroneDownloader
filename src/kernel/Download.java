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
    final static int len = 1024;

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
            InputStream in = url.openStream();
            File file = new File(this.saveAdd, this.fileName);
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[len];
            int hasRead;
            while ((hasRead = in.read(buffer))>0) {
               out.write(buffer, 0, hasRead);
            }     
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
