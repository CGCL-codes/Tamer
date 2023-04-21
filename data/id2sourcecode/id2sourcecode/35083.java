    private void sendGet(final BufferedReader reader, final BufferedWriter writer) throws IOException {
        StringBuffer getBuffer = new StringBuffer();
        getBuffer.append("GET ");
        getBuffer.append(this.location);
        getBuffer.append(" HTTP/1.1");
        getBuffer.append(CRLF);
        getBuffer.append("Host: ");
        getBuffer.append(this.address);
        getBuffer.append(CRLF);
        getBuffer.append(CRLF);
        writer.write(getBuffer.toString());
        writer.flush();
    }
