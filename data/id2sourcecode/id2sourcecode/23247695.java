    private void readSOAPResponse(Writer writer) throws DataDirectException {
        try {
            log.write(sdf.format(new Date()));
            log.write("\tReading response from server");
            log.write(SOAPRequest.END_OF_LINE);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        try {
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try {
                    BufferedReader reader = getReader();
                    Parser parser = ParserFactory.getWriterParser(reader, writer);
                    parser.setLog(log);
                    parser.parseXTVD();
                    reader.close();
                    log.write(sdf.format(new Date()));
                    log.write("\tFinished reading response from server");
                    log.write(SOAPRequest.END_OF_LINE);
                    log.flush();
                } catch (Throwable t) {
                    throw new DataDirectException(t.getMessage(), t);
                }
            } else {
                processError();
            }
        } catch (IOException ioex) {
            throw new DataDirectException(ioex.getMessage(), ioex);
        }
    }
