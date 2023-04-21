    private void executeRequest(String s, int i, ByteWriter writer) throws IOException {
        String turl = this.url + "?";
        turl += "t=" + s;
        turl += "&i=" + Integer.toString(i);
        URL realURL = new URL(turl);
        HttpURLConnection connection = (HttpURLConnection) realURL.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "IIC2.0/PC 3.5.2540");
        connection.setRequestProperty("Pragma", this.pragma);
        connection.setRequestProperty("Content-Type", "application/oct-stream");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Cookie", "ssic=" + this.ssic);
        connection.setDoOutput(true);
        OutputStream out = connection.getOutputStream();
        out.write(writer.toByteArray(), 0, writer.size());
        out.flush();
        if (connection.getResponseCode() == 200) {
            int contentLength = connection.getContentLength();
            if (contentLength > 4) {
                InputStream in = connection.getInputStream();
                writer.clear();
                while (contentLength > 4) {
                    writer.writeByte(in.read());
                    contentLength--;
                }
                this.bytesRecived(writer.toByteArray(), 0, writer.size());
            }
        } else {
            throw new IOException("Invalid response stateCode=" + connection.getResponseCode());
        }
    }
