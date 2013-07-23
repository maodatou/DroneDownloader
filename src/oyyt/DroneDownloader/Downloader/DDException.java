/*
 * Copyright (c) 2013 maodatou. All rights reserved.
 * The project is governed by BSD licenses.
 */

package oyyt.DroneDownloader.Downloader;

public class DDException extends Exception {
    private final ErrorCode errorCode;
    public DDException (ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

enum ErrorCode {
    ARGS_ERROR,
    NET_ERROR,
    FILE_ERROR,
}
