    byte[] getBytes(String URLstring) throws MalformedURLException, IOException {
        System.out.println("Loader getBytes " + URLstring);
        URL fu;
        byte[] buf = null;
        if (debug) {
            System.out.println("loader.getBytes(" + URLstring + ") called");
        }
        fu = new URL(URLstring);
        if (fu.getProtocol().equals("file")) {
            if (debug) {
                System.out.println("Using direct input stream on file url");
            }
            URLConnection urlc = fu.openConnection();
            urlc.connect();
            urlc.getContentType();
            DataInputStream is = new DataInputStream(urlc.getInputStream());
            int length = urlc.getContentLength();
            buf = new byte[length];
            is.readFully(buf, 0, length);
        } else {
            ContentNegotiator cn = new ContentNegotiator(fu);
            buf = (byte[]) cn.getContent();
        }
        return buf;
    }
