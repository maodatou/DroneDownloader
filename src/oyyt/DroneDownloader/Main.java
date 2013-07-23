/*
 * Copyright (c) 2013 maodatou. All rights reserved.
 * The project is governed by BSD licenses.
 */

package oyyt.DroneDownloader;

import oyyt.DroneDownloader.Downloader.DownloadTask;
import oyyt.DroneDownloader.Downloader.State;
import oyyt.DroneDownloader.Utility.OptParser;
import oyyt.DroneDownloader.Utility.Utility;

/**
 * This is the main entrance of CLI environment.
 */
public class Main {
    public static void main(String[] args) {
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

        // start download and print information.
        DownloadTask task = new DownloadTask(url, dir, filename, resume);
        task.start();

        do {
            State state = task.getState();
            if (state == State.FINISH) {
                System.out.println("\nFINISH");
                break;
            }

            if (state == State.ERROR) {
                System.out.println("\nError:\n\t" + task.getErrorMsg());
                break;
            }

            if (state == State.DOWNLOADING) {
                System.out.print("\r" + task.getFileName() + ": ");
                System.out.print("\t" + Utility.formatSpeed(task.getSpeed()));
                System.out.print(String.format("\t%3d%%", task.getPercentage()));
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("Thread is interrupted.");
            }
        } while(true);
    }
}
