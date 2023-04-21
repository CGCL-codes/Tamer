    public static Charset detect(String urlStr) {
        int lang = nsPSMDetector.SIMPLIFIED_CHINESE;
        nsDetector det = new nsDetector(lang);
        det.Init(new nsICharsetDetectionObserver() {

            public void Notify(String charset) {
                HtmlCharsetDetector.found = true;
                probableCharset = Charsets.getSupportableCharset(charset);
            }
        });
        URL url;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        BufferedInputStream imp;
        try {
            imp = new BufferedInputStream(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] buf = new byte[1024];
        int len;
        boolean done = false;
        boolean isAscii = true;
        try {
            while ((len = imp.read(buf, 0, buf.length)) != -1) {
                if (isAscii) isAscii = det.isAscii(buf, len);
                if (!isAscii && !done) done = det.DoIt(buf, len, false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        det.DataEnd();
        if (isAscii) {
            found = true;
        }
        if (!found) {
            String prob[] = det.getProbableCharsets();
            for (int i = 0; i < prob.length; i++) {
                System.out.println("Probable Charset = " + prob[i]);
            }
            probableCharset = Charsets.getSupportableCharset(prob[0]);
        }
        return probableCharset;
    }
