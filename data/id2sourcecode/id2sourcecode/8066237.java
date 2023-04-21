    public void writeTo(OutputStream os, String[] ignoreList) throws IOException, MessagingException {
        if (!saved) saveChanges();
        if (modified) {
            MimeBodyPart.writeTo(this, os, ignoreList);
            return;
        }
        Enumeration hdrLines = getNonMatchingHeaderLines(ignoreList);
        LineOutputStream los = new LineOutputStream(os);
        while (hdrLines.hasMoreElements()) los.writeln((String) hdrLines.nextElement());
        los.writeln();
        if (content == null) {
            InputStream is = getContentStream();
            byte[] buf = new byte[8192];
            int len;
            while ((len = is.read(buf)) > 0) os.write(buf, 0, len);
            is.close();
            buf = null;
        } else {
            os.write(content);
        }
        os.flush();
    }
