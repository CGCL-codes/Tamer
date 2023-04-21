    public void writeAck(int intCode, String strMessage) throws Exception {
        try {
            String strBuffer = getAckHeader(intCode);
            strBuffer += strMessage + strLineTerm;
            char achrBuffer[] = strBuffer.toCharArray();
            objOStreamWriter.write(achrBuffer);
            objOStreamWriter.flush();
        } catch (Exception e) {
            objEventlog.printError("SessionThread.writeAck: " + e.getMessage());
            throw e;
        }
    }
