package com.gopai.cli;

public class Arguments {

    private String username;
    private String password;
    private String guid;
    private String path;
    private boolean sendToOut = false;
    private boolean isInteractiveMode;

    public Arguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (isFlag(args, i, "-u", 1))
                username = args[i + 1];
            else if (isFlag(args, i, "-p", 1))
                password = args[i + 1];
            else if (isFlag(args, i, "-guid", 1))
                guid = args[i + 1];
            else if (isFlag(args, i, "-o", 0))
                sendToOut = true;
            else if (isFlag(args, i, "-path", 1))
                path = args[i + 1];
            else if (isFlag(args, i, "-i", 0))
                isInteractiveMode = true;
        }
    }

    private boolean isFlag(String[] args, int i, String flag, int numberOfOperands) {
        return args[i].equalsIgnoreCase(flag) && i + numberOfOperands < args.length;
    }

    public boolean missingArguments() {
        if (isInteractiveMode)
            return false;
        return (username == null || password == null || guid == null) || (!sendToOut && path == null);
    }

    public boolean hasLoginCredentials(){
        return username != null && password != null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGuid() {
        return guid;
    }

    public String getPath() {
        return path;
    }

    public boolean getSendToOut() {
        return sendToOut;
    }

    public boolean getInteractiveMode() {
        return isInteractiveMode;
    }
}
