    public static void main(String[] args) {
        try {
            URL url = new URL("http://google.com");
            HttpRequest httpReq = new HttpRequest(url);
            CGIParameters params = new CGIParameters();
            InputStream is = httpReq.get(params);
            int read = 0;
            byte b[] = new byte[1024];
            while ((read = is.read(b, 0, b.length)) != -1) {
                System.out.write(b, 0, read);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
