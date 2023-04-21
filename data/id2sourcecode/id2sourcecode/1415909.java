    public FeedObjectIfc getFeedObject(URL urlPath) {
        FeedReaderIfc ifc = null;
        BufferedInputStream bufIn = null;
        try {
            bufIn = new BufferedInputStream(urlPath.openStream());
            String ver = readVersion(bufIn);
            if (ver != null) {
                if (ver.equals(VERSIONS[0])) {
                    ifc = new FeedReaderRss091(urlPath);
                } else if (ver.equals(VERSIONS[1])) {
                    ifc = new FeedReaderRss20(urlPath);
                }
            }
            if (ifc != null) {
                return ifc.read();
            }
        } catch (SSLHandshakeException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (bufIn != null) {
                try {
                    bufIn.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }
