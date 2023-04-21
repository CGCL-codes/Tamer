    public static String getExpectedChecksum(String version) throws IOException {
        String pageChecksum = null;
        URL checksumUrl = new URL(String.format(checksumUrlFormat, version));
        HttpURLConnection conn = (HttpURLConnection) checksumUrl.openConnection();
        BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read;
        while ((read = in.read()) != -1) {
            out.write(read);
        }
        String html = new String(out.toByteArray());
        in.close();
        out.close();
        Matcher m = checksumPattern.matcher(html);
        if (m.find()) {
            pageChecksum = m.group(1);
        }
        return pageChecksum;
    }
