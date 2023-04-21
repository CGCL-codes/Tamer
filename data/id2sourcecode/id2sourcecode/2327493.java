    protected void writeReportFile(String fileName) {
        URLConnection conn = null;
        try {
            URL url = new URL(fileName);
            conn = url.openConnection();
            conn.setDoOutput(true);
            sendData(conn);
            receiveResponse(conn);
        } catch (Exception e) {
            ErrorHandler.error(e);
        }
        commandHistory.setBaseline();
    }
