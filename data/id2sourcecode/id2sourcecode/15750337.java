    public void run() {
        URL url;
        BufferedInputStream in;
        FileOutputStream out;
        Tidy tidy = new Tidy();
        tidy.setXmlOut(xmlOut);
        try {
            tidy.setErrout(new PrintWriter(new FileWriter(errOutFileName), true));
            url = new URL(strUrl);
            in = new BufferedInputStream(url.openStream());
            out = new FileOutputStream(outFileName);
            tidy.parse(in, out);
        } catch (IOException e) {
            log.warn(this.toString() + e.toString());
        }
    }
