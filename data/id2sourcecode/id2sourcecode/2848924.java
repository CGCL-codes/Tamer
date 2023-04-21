    public byte[] getData(AlbumRequest request) throws IOException {
        String u = request.replace(url);
        BufferedInputStream bis = null;
        try {
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            bis = new BufferedInputStream(is);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int ch;
            while ((ch = bis.read()) != -1) {
                baos.write(ch);
            }
            byte[] bytes = baos.toByteArray();
            String charset = "UTF-8";
            String s = new String(bytes, charset);
            Matcher m = CHARSET_PATTERN.matcher(s);
            if (m.find()) {
                charset = m.group();
                s = new String(bytes, charset);
            }
            return s.getBytes();
        } finally {
            if (bis != null) try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
