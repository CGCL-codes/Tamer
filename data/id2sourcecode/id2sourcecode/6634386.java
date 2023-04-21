    public static void test(URL url) throws IOException {
        String boundary = StringUtils.generateRandomString("1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_", 40);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.writeBytes("--" + boundary + CRLF);
        out.writeBytes("Content-Disposition: form-data; name=\"text\"\r\n");
        out.writeBytes("Content-Type: text/plain; charset=utf-8");
        out.writeBytes(W_CRLF);
        out.write("eLXg".getBytes("utf-8"));
        out.writeBytes(CRLF);
        File file = new File("c:\\temp\\send.txt");
        out.writeBytes("--" + boundary + CRLF);
        out.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"");
        out.write(file.getName().getBytes("utf-8"));
        out.writeBytes("\"");
        out.writeBytes(CRLF);
        out.writeBytes("Content-Type: application/octet-stream");
        out.writeBytes(W_CRLF);
        out.writeBytes(IOUtils.toString(file, _1K_BYTES, "utf-8"));
        out.writeBytes(CRLF);
        out.writeBytes("--" + boundary + "--");
        out.flush();
        out.close();
        PrintUtils.print(conn.getInputStream(), "utf-8");
    }
