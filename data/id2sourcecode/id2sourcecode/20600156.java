    public static InputStream getHttpStream(String url, String filename) throws Exception {
        System.out.println("trying url " + url);
        Matcher matcher = Pattern.compile("^http://", Pattern.DOTALL).matcher(url);
        if (matcher.find()) {
            HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
            if ("audio/mpeg".equals(conn.getHeaderField("Content-Type"))) return new InputStreamProxy(conn.getInputStream(), filename);
            conn.getInputStream().close();
            return null;
        }
        return null;
    }
