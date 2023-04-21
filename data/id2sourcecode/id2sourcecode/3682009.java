    private byte[] readFromSrc() throws SAXException {
        URL url;
        try {
            url = new URL(src);
        } catch (MalformedURLException e) {
            String systemId = locator.getSystemId();
            if (systemId == null) {
                throw new SAXException("Missing systemId which is needed " + "for resolving relative src: " + src);
            }
            try {
                url = new URL(systemId.substring(0, systemId.lastIndexOf('/') + 1) + src);
            } catch (MalformedURLException e1) {
                throw new SAXException("Invalid reference to external value src: " + src);
            }
        }
        DataInputStream in = null;
        try {
            URLConnection con = url.openConnection();
            in = new DataInputStream(con.getInputStream());
            int len = con.getContentLength();
            byte[] data = new byte[(len + 1) & ~1];
            in.readFully(data, 0, len);
            return data;
        } catch (IOException e) {
            throw new SAXException("Failed to read value from external src: " + url, e);
        } finally {
            CloseUtils.safeClose(in);
        }
    }
