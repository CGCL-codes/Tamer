    private void initialize() throws java.io.IOException {
        parsePre();
        if (context != null) {
            String fontCache = context.getProperty("jdvi.font.cache");
            if (fontCache != null) try {
                URL cacheURL = new URL(document, fontCache);
                InputStream istream = cacheURL.openStream();
                InflaterInputStream cacheIn = new InflaterInputStream(istream);
                ObjectInputStream fontIn = new ObjectInputStream(cacheIn);
                font = (jdvi.font.Font[]) fontIn.readObject();
                istream.close();
                Frame1.writelog("read fonts from cache file.");
            } catch (java.io.IOException e2) {
                System.err.println(e2);
            } catch (java.lang.ClassNotFoundException e3) {
                System.err.println(e3);
            }
        } else Frame1.writelog("context null!.");
        parsePost();
    }
