package kernel;

/**
 * copy Right Information: baobao
 * Project: DD
 * JDK version used: JDK1.7
 * Version: 1.0
 * Modification history: 2013.7.3 
 **/

public class Main {
    public static void main(String[] args) {
        Command in = new Command();
        in.parse(args);
        Downloader down = new Downloader(in.userInputUrl,in.filePath,in.fileName,in.fileNameIndex,in.filePathIndex);
        down.downMain();
    }
}
