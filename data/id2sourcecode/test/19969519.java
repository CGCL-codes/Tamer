    public MyClass(String filename, String defaultEncoding) throws FileNotFoundException, ParseException, IOException, URISyntaxException {
        int lang = nsPSMDetector.UNIMOZER;
        nsDetector det = new nsDetector(lang);
        det.Init(new nsICharsetDetectionObserver() {

            public void Notify(String charset) {
                HtmlCharsetDetector.found = true;
            }
        });
        File f = new File(filename);
        URL url = f.toURI().toURL();
        BufferedInputStream imp = new BufferedInputStream(url.openStream());
        byte[] buf = new byte[1024];
        int len;
        boolean done = false;
        boolean isAscii = true;
        while ((len = imp.read(buf, 0, buf.length)) != -1) {
            if (isAscii) isAscii = det.isAscii(buf, len);
            if (!isAscii && !done) done = det.DoIt(buf, len, false);
        }
        det.DataEnd();
        imp.close();
        boolean found = false;
        String encoding = new String(Unimozer.FILE_ENCODING);
        if (isAscii) {
            encoding = "US-ASCII";
            found = true;
        }
        if (!found) {
            String prob[] = det.getProbableCharsets();
            if (prob.length > 0) {
                encoding = prob[0];
            } else {
                encoding = defaultEncoding;
            }
        }
        String filenameSmall = new File(filename).getName().replace(".java", "");
        this.setInternalName(filenameSmall);
        loadFromFileInputStream(new FileInputStream(filename), encoding);
    }
