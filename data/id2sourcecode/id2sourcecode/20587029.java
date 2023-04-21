    private void pushFile(String file, String encoding, InputStreamReader reader) throws JasperException, FileNotFoundException {
        String longName = file;
        int fileid = registerSourceFile(longName);
        if (fileid == -1) {
            err.jspError("jsp.error.file.already.registered", file);
        }
        currFileId = fileid;
        try {
            CharArrayWriter caw = new CharArrayWriter();
            char buf[] = new char[1024];
            for (int i = 0; (i = reader.read(buf)) != -1; ) caw.write(buf, 0, i);
            caw.close();
            if (current == null) {
                current = new Mark(this, caw.toCharArray(), fileid, getFile(fileid), master, encoding);
            } else {
                current.pushStream(caw.toCharArray(), fileid, getFile(fileid), longName, encoding);
            }
        } catch (Throwable ex) {
            log.error("Exception parsing file ", ex);
            popFile();
            err.jspError("jsp.error.file.cannot.read", file);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception any) {
                }
            }
        }
    }
