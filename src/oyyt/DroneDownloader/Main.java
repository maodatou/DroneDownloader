/*
 * Copyright (c) 2013 maodatou. All rights reserved.
 * The project is governed by BSD licenses.
 */

package oyyt.DroneDownloader;

import oyyt.DroneDownloader.Downloader.DownloadTask;
import oyyt.DroneDownloader.Downloader.State;
import oyyt.DroneDownloader.Gui.DownloadGui;
import oyyt.DroneDownloader.Utility.OptParser;
import oyyt.DroneDownloader.Utility.Utility;

/**
 * This is the main entrance of CLI environment.
 */
public class Main {
    public static void main(String[] args) {
    
/*   	
    	// parse option
        OptParser optParser = new OptParser();
        optParser.addDefaultOpt("url", "The URL to be downloaded.", true);
        optParser.addOpt('d', "dir", "save file in dir.", false, false);
        optParser.addOpt('f', "filename", "save as filename.", false, false);
        optParser.addOpt('c', "continue", "resume getting a partially-downloaded file.", true, false);
        optParser.addOpt('s', "silent", "run in silent mode.", true, false);
        optParser.addOpt('h', "help", "print help message.", true, false);

        if (!optParser.parse(args)) {
            System.err.println(optParser.getErrorMsg());
            System.exit(1);
        }

        if (optParser.getBooleanOption("help")) {
            System.err.println(optParser.getUsage());
            System.exit(0);
        }

        if (!optParser.check()) {
            System.err.println(optParser.getErrorMsg());
            System.err.println(optParser.getUsage());
            System.exit(1);
        }

        String url = optParser.getOption("url", "");
        String dir = optParser.getOption("dir", null);
        String filename = optParser.getOption("filename", null);
        boolean resume = optParser.getBooleanOption("continue");
*/
//    	DownloadGui user = new DownloadGui();
//    	user.init();
    	
    	
    	DownloadGui downloadGui =  new DownloadGui();
    	downloadGui.init();

    }

}
