    public static void error(String msg, OperationStatus status, boolean exit) {
        if (msg == null) msg = ""; else if (msg.length() > 0) msg += ": ";
        if (status == OperationStatus.KEYEMPTY) msg += "KEYEMPTY (current rec deleted)"; else if (status == OperationStatus.KEYEXIST) msg += "KEYEXIST (key already exists; overwrite not allowed"; else if (status == OperationStatus.NOTFOUND) msg += "NOTFOUND (requested key/data pair not found)"; else msg += "SUCCESS";
        error(msg, exit);
    }
