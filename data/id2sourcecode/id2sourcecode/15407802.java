    protected void writeWelcome() throws Exception {
        if (intConnectionId == 0) {
            throw new ExcOutOfResources();
        }
        objEventlog.println("session " + Integer.toString(intConnectionId) + " welcome " + objCommSocket.getInetAddress().getHostAddress());
        String strBuffer;
        strBuffer = "SERVER " + objSRCPDaemon.getProcessor().getProcessorName() + "; ";
        strBuffer += "SRCP " + Declare.strSRCPVersion;
        strBuffer += "; HOST " + objCommSocket.getLocalAddress().getHostName() + ";" + strLineTerm;
        try {
            char achrBuffer[] = strBuffer.toCharArray();
            objOStreamWriter.write(achrBuffer);
            objOStreamWriter.flush();
        } catch (Exception e) {
            objEventlog.printError("SessionThread.writeWelcome: " + e.getMessage());
        }
    }
