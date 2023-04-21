    public static String upload(String fileName, byte[] fileContent, ConnectionContext ccontext, String servletUploadURL) throws IOException {
        String token = ccontext.getAuthToken();
        URL url = new URL(servletUploadURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        conn.setRequestProperty("Cookie", "ZM_AUTH_TOKEN=" + token);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        dos.writeBytes(twoHyphens + boundary + lineEnd);
        dos.writeBytes("Content-Disposition: form-data; name=\"upload\";" + " filename=\"" + fileName + "\"" + lineEnd);
        dos.writeBytes(lineEnd);
        dos.write(fileContent);
        dos.writeBytes(lineEnd);
        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        dos.flush();
        dos.close();
        byte buffer[] = new byte[1024];
        int readed;
        InputStream is = conn.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((readed = is.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, readed);
        }
        String result = new String(baos.toByteArray());
        String params[] = result.split(",");
        if (params.length != 3) {
            throw new IOException("Unexpected result of upload:" + result);
        }
        String aid = params[2].substring(1, params[2].lastIndexOf('\''));
        return aid;
    }
