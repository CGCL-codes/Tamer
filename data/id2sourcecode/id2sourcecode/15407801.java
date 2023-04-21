    public void writeAck(String strTimestamp, int intCode, String strMessage) throws Exception {
        try {
            String strBuffer = getAckHeader(strTimestamp, intCode);
            strBuffer += strMessage + strLineTerm;
            char achrBuffer[] = strBuffer.toCharArray();
            objOStreamWriter.write(achrBuffer);
            objOStreamWriter.flush();
        } catch (Exception e) {
            objEventlog.printError("SessionThread.writeAck: " + e.getMessage());
            throw e;
        }
    }
