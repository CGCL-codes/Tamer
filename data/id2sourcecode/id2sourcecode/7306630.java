        private void parse(String fname) throws java.io.IOException, SAXException {
            InputStream stream;
            File file = new File(fname);
            if (!file.exists()) {
                URL url = new URL(fname);
                stream = url.openStream();
            } else stream = new java.io.FileInputStream(file);
            parse(new org.xml.sax.InputSource(stream));
        }
