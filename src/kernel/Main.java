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
        Download down = new Download(in.userUrl,in.saveAdd,in.fileName,in.fileNameIndex,in.saveAddIndex);
        down.downMain();
    }
}
