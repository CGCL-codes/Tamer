    public static void log(String[] strMessageArr, int nId, byte nFlags) {
        for (String strArray : strMessageArr) {
            strArray = strArray.replace("\n", "*n");
        }
        try {
            out.write(getDate() + ":" + lIdInUse.get(nId).strClass + ":" + lIdInUse.get(nId).strThread + strDelim + "Dumping array" + strDelim + nFlags + "\r\n");
            for (int i = 0; i < strMessageArr.length; i++) {
                out.write(getDate() + ":" + lIdInUse.get(nId).strClass + ":" + lIdInUse.get(nId).strThread + strDelim + strMessageArr[i] + strDelim + nFlags + "\r\n");
            }
            out.write(getDate() + ":" + lIdInUse.get(nId).strClass + ":" + lIdInUse.get(nId).strThread + strDelim + "Finished dumping array" + strDelim + nFlags + "\r\n");
            out.flush();
        } catch (IOException e) {
            log("in log(String[]), couldn't log array to file, location: " + fLog.getAbsolutePath(), nDebugId, FLAG_ERROR);
        }
    }
