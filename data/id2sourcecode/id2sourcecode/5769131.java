    public void handle(InputStream input, MultiOutputStream output, HTTPHeader header, Properties htab, Connection con) {
        String file = htab.getProperty("argstring");
        if (file == null) throw (new IllegalArgumentException("no file given."));
        if (file.indexOf("..") >= 0) throw (new IllegalArgumentException("Bad filename given"));
        String filename = "htdocs/" + file;
        if (filename.endsWith("/")) filename = filename + "index.html";
        filename = filename.replace('/', File.separatorChar);
        HTTPHeader rheader = con.getResponseHandler().getHeader();
        if (filename.endsWith("gif")) rheader.setHeader("Content-type", "image/gif"); else if (filename.endsWith("jpeg") || filename.endsWith("jpg")) rheader.setHeader("Content-type", "image/jpeg"); else if (filename.endsWith("txt")) rheader.setHeader("Content-type", "text/plain");
        File fle = new File(filename);
        long length = fle.length();
        rheader.setHeader("Content-Length", Long.toString(length));
        con.setContentLength(rheader.getHeader("Content-Length"));
        Date lm = new Date(fle.lastModified() - con.getProxy().getOffset());
        rheader.setHeader("Last-Modified", HTTPDateParser.getDateString(lm));
        FileInputStream fis;
        try {
            fis = new FileInputStream(filename);
        } catch (IOException e) {
            throw (new IllegalArgumentException("Could not open file: " + file + "."));
        }
        try {
            WritableByteChannel wc = output.getChannel();
            if (wc != null) {
                channelTransfer(rheader, fis.getChannel(), length, wc);
            } else {
                simpleTransfer(rheader, fis, output);
            }
        } catch (IOException e) {
            throw (new IllegalArgumentException("Could not send: " + file + "."));
        }
        try {
            if (fis != null) fis.close();
        } catch (IOException e) {
            throw (new IllegalArgumentException("Could not close: " + file + "."));
        }
    }
