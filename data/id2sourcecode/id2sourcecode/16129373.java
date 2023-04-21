    private void startRemoteCpProtocol(InputStream in, OutputStream out, File localFile) throws IOException, JSchException, ScpException {
        File startFile = localFile;
        while (true) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            while (true) {
                int read = in.read();
                if (read < 0) {
                    return;
                }
                if ((byte) read == LINE_FEED) {
                    break;
                }
                stream.write(read);
            }
            String serverResponse = stream.toString("UTF-8");
            if (serverResponse.charAt(0) == 'C') {
                parseAndFetchFile(serverResponse, startFile, out, in);
            } else if (serverResponse.charAt(0) == 'D') {
                startFile = parseAndCreateDirectory(serverResponse, startFile);
                sendAck(out);
            } else if (serverResponse.charAt(0) == 'E') {
                startFile = startFile.getParentFile();
                sendAck(out);
            } else if (serverResponse.charAt(0) == '\01' || serverResponse.charAt(0) == '\02') {
                throw new IOException(serverResponse.substring(1));
            }
        }
    }
