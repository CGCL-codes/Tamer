    private BufferedReader createReader(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        String charset = null;
        if (conn.getContentType() != null) {
            Matcher m = CONTENT_TYPE.matcher(conn.getContentType());
            if (m.matches()) {
                charset = m.group(1);
            }
        }
        if (charset == null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder contents = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                contents.append(line).append(' ');
            }
            Matcher mTag = HTTP_EQUIV.matcher(contents);
            if (mTag.find()) {
                String contentType = mTag.group(1);
                Matcher mCharset = CONTENT_TYPE.matcher(contentType);
                if (mCharset.matches()) {
                    charset = mCharset.group(1);
                }
            }
            conn = url.openConnection();
        }
        if (charset == null) charset = "utf-8";
        System.out.println("$$ charset = " + charset);
        return new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
    }
