    public void getData(Calendar start, Calendar end, Writer writer) throws DataDirectException {
        try {
            log.write(sdf.format(new Date()));
            log.write("\tOpening connection to ");
            log.write(webserviceURI.toString());
            log.write(SOAPRequest.END_OF_LINE);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        sendDownloadRequest(start, end);
        readSOAPResponse(writer);
        try {
            writer.flush();
        } catch (IOException ioex) {
            try {
                log.write(DataDirectException.getStackTraceString(ioex));
            } catch (IOException ioex1) {
                ioex1.printStackTrace();
            }
        }
        httpConnection.disconnect();
    }
