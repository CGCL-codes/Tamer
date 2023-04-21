    private static void copyPropertyFile() {
        InputStream istream = null;
        OutputStream ostream = null;
        try {
            PROPERTY_FILE.getParentFile().mkdirs();
            PROPERTY_FILE.createNewFile();
            istream = TmxProperties.class.getClassLoader().getResourceAsStream("org/dmonix/timex/properties/timex.properties");
            ostream = new FileOutputStream(PROPERTY_FILE);
            byte[] data = new byte[256];
            int read = 1;
            while (read > 0) {
                read = istream.read(data);
                ostream.write(data, 0, read);
                ostream.flush();
            }
        } catch (Exception ex) {
            log.log(Level.CONFIG, "Could not copy property file", ex);
        } finally {
            IOUtil.closeNoException(istream);
            IOUtil.closeException(ostream);
        }
    }
