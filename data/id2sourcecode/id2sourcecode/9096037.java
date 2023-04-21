    public static void log(String strMessage, int nId, byte nFlags) {
        strMessage = strMessage.replace("\n", "*n");
        try {
            out.write(getDate() + ":" + lIdInUse.get(nId).strClass + ":" + lIdInUse.get(nId).strThread + strDelim + strMessage + strDelim + nFlags + "\r\n");
            out.flush();
        } catch (IOException e) {
            System.err.println("Couldn't log message: " + strMessage + " Location: " + fLog.getAbsolutePath());
        }
    }
