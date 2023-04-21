    public Document build(URL url) throws JPListException {
        URLConnection urlconn = null;
        InputStream istream = null;
        try {
            urlconn = url.openConnection();
            istream = urlconn.getInputStream();
            return build(istream);
        } catch (IOException e) {
            throw new JPListException("IO Exception", e);
        } finally {
            if (istream != null) {
                try {
                    istream.close();
                } catch (Exception e) {
                }
            }
        }
    }
