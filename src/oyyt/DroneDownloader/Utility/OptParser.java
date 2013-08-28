/*
 * Copyright (c) 2013 maodatou. All rights reserved.
 * The project is governed by BSD licenses.
 */

package oyyt.DroneDownloader.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptParser {
    private String errorMsg;
    private List<Option> opts;
    private Map<String, String> userValues;

    public OptParser() {
        errorMsg = "";
        opts = new ArrayList<Option>();
        userValues = new HashMap<String, String>();
    }

    public void addOpt(char shortArg, String name, String explanation, boolean single, boolean mandatory) {
        Option opt = new Option(shortArg, name, explanation, single, mandatory);
        opts.add(opt);
    }

    public void addDefaultOpt(String name, String explanation, boolean mandatory) {
        Option opt = new Option('\0', name, explanation, false, mandatory);
        opts.add(opt);
    }

    public boolean parse(String[] args) {
        int argType;
        main_loop: for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                argType = 0;   // long option
            } else if (args[i].startsWith("-")) {
                argType = 1;   // short option
            } else {
                argType = 2;   // default option
            }

            for (Option opt : opts) {
                switch (argType) {
                    case 0:   // long option
                        if (!args[i].substring(2).equals(opt.name)) {
                            continue;
                        }
                        break;
                    case 1:   // short option
                        if (args[i].charAt(1) != opt.shortArg) {
                            continue;
                        }
                        break;
                    case 2:   // default option
                        if ('\0' != opt.shortArg) {
                            continue;
                        }
                        break;
                }

                if (opt.single) {
                    userValues.put(opt.name, String.valueOf(true));
                    continue main_loop;
                }

                if (argType == 2) {
                    userValues.put(opt.name, args[i]);
                    continue main_loop;
                }

                if (i+1 < args.length && !args[i+1].startsWith("-")) {
                    userValues.put(opt.name, args[i+1]);
                    i++;
                    continue main_loop;
                } else {
                    errorMsg = "Need arguments for option " + args[i];
                    return false;
                }
            }
            // not found.
            errorMsg = "Not support option " + args[i];
            return false;
        }
        errorMsg = "";
        return true;
    }

    public boolean check() {
        for (Option opt : opts) {
            if (!opt.mandatory) {
                continue;
            }
            if (!userValues.containsKey(opt.name)) {
                errorMsg = "Option " + opt.name + " is necessary.";
                return false;
            }
        }
        return true;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getOption(String name, String defaultValue) {
        String value = userValues.get(name);
        return value == null ? defaultValue : value;
    }

    public boolean getBooleanOption(String name) {
        return Boolean.valueOf(userValues.get(name));
    }

    public String getUsage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usage: command [options]");
        for (Option opt : opts) {
            if (opt.shortArg == '\0') {
                sb.append("\n\tDefault");
            } else {
                sb.append("\n\t-").append(opt.shortArg);
            }
            sb.append("\t--").append(opt.name).append(": ");
            if (!opt.single) {
                sb.append("\t<arg>");
            }
            if (opt.mandatory) {
                sb.append("\t Needed");
            }
            sb.append("\n\t\t\t").append(opt.explanation);
        }
        return sb.toString();
    }


}

class Option {
    public final char shortArg;
    public final String name;
    public final String explanation;
    public final boolean single;
    public final boolean mandatory;

    //这里为什么要有private的构造函数呢？
    private Option() {
        this.shortArg = '\0';
        this.name = null;
        this.explanation = null;
        this.single = false;
        this.mandatory = false;
    }

    Option(char shortArg, String name, String explanation, boolean single, boolean mandatory) {
        this.shortArg = shortArg;
        this.name = name;
        this.explanation = explanation;
        this.single = single;
        this.mandatory = mandatory;
    }
}