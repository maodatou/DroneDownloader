package kernel;

public class Command {
    final static String prompt = "Usage: java url [-optional]\n"
            + "The option include:\n"
            + "  -f  name of downloaded file (default name:original name)\n"
            + "  -s  save address of downloaded file (default address: E:\\Workspace\\DroneDownloader)";
    public String userUrl = null;
    public String saveAdd = null;
    public String fileName = null;
    public boolean fileNameIndex = true;

    Command(){
        
    }
    public void parse(String[] args) {
        if (args.length == 0) {
            System.out.println(prompt);
            return;
        }
        this.userUrl = args[0];
        for (int i = 1; i < args.length; i++) {
            int index = i;
            index++;
            if (args[i].startsWith("-")) {
                if (index > args.length - 1)
                    continue;
                if (args[index].startsWith("-"))
                    continue;
                switch (args[i].charAt(1)) {
                case 'f':
                    this.fileName = args[index];
                    this.fileNameIndex = false;
                    break;
                case 's':
                    this.saveAdd = args[++i];
                    break;
                default:
                }
            }
        }
    }

}