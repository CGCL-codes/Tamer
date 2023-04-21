    private InputStream post(String command, Map<String, Object> params) throws IOException {
        String NEWLINE = "\r\n";
        String PREFIX = "--";
        String BOUNDARY = "-----------" + Long.toString(System.currentTimeMillis(), 16);
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String fullCommand = prefix + command;
        URL url = null;
        try {
            URI uri = new URI(fullCommand);
            url = uri.toURL();
        } catch (URISyntaxException e) {
            throw new IOException("Bad URL " + e);
        } catch (MalformedURLException e) {
            throw new IOException("Bad URL " + e);
        }
        if (trace || traceSends) {
            System.out.println("Sending-->     " + url);
        }
        if (logFile != null) {
            logFile.println("Sending-->     " + url);
        }
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setAllowUserInteraction(false);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
        dos = new DataOutputStream(conn.getOutputStream());
        for (String key : params.keySet()) {
            dos.writeBytes(PREFIX);
            dos.writeBytes(BOUNDARY);
            dos.writeBytes(NEWLINE);
            Object val = params.get(key);
            if (val instanceof File) {
                File file = (File) val;
                dos.writeBytes("Content-Disposition: form-data; name=\"file\";" + " filename=\"" + file.getName() + "\"");
                dos.writeBytes(NEWLINE);
                dos.writeBytes("Content-Type: application/octet-stream");
                dos.writeBytes(NEWLINE);
                dos.writeBytes(NEWLINE);
                InputStream is = new FileInputStream(file);
                int r = 0;
                byte[] data = new byte[1024];
                while ((r = is.read(data, 0, data.length)) != -1) {
                    dos.write(data, 0, r);
                }
                is.close();
                dos.writeBytes(NEWLINE);
            } else {
                dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"");
                dos.writeBytes(NEWLINE);
                dos.writeBytes(NEWLINE);
                dos.writeBytes(val.toString());
                dos.writeBytes(NEWLINE);
            }
        }
        dos.writeBytes(PREFIX);
        dos.writeBytes(BOUNDARY);
        dos.writeBytes(PREFIX);
        dos.writeBytes(NEWLINE);
        dos.flush();
        dos.close();
        return conn.getInputStream();
    }
