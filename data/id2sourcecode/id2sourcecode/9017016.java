    private String readLogFileIntoString(File aFile) {
        Reader reader = null;
        SdlUnsynchronizedCharArrayWriter writer = new SdlUnsynchronizedCharArrayWriter();
        try {
            reader = new FileReader(aFile);
            char[] buff = new char[1024 * 128];
            int read;
            while ((read = reader.read(buff)) != -1) writer.write(buff, 0, read);
        } catch (IOException e) {
            SdlCloser.close(reader);
        }
        return writer.toString();
    }
