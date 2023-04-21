    public static void log(String[] strKeyArr, int[] nValueArr, int nId, byte nFlags) {
        if (strKeyArr.length != nValueArr.length) {
            log("strKeyArr and nValueArr are not of equal size, can't log key->value", Debug.nDebugId, FLAG_ERROR);
            return;
        }
        try {
            out.write(getDate() + ":" + lIdInUse.get(nId).strClass + ":" + lIdInUse.get(nId).strThread + strDelim + "Dumping key->value" + strDelim + nFlags + "\r\n");
            for (int i = 0; i < strKeyArr.length; i++) {
                out.write(getDate() + ":" + lIdInUse.get(nId).strClass + ":" + lIdInUse.get(nId).strThread + strDelim + "Key: " + strKeyArr[i] + " -> " + nValueArr[i] + strDelim + nFlags + "\r\n");
            }
            out.write(getDate() + ":" + lIdInUse.get(nId).strClass + ":" + lIdInUse.get(nId).strThread + strDelim + "Finished dumping key->value" + strDelim + nFlags + "\r\n");
            out.flush();
        } catch (IOException e) {
            log("in log(String[], int[]), couldn't log key->values pairs to file, location: " + fLog.getAbsolutePath(), Debug.nDebugId, FLAG_ERROR);
        }
    }
