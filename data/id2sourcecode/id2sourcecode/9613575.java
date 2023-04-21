    public void checkID(String id) throws Exception {
        ArrayList<String> processed = new ArrayList<String>();
        mOut.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        String path = translate(id);
        mOut.println("<Check>");
        mOut.println("<ID>" + id + "</ID>");
        if (path != null) {
            path += ".xml";
            File file = new File(path);
            if (file.exists()) {
                mOut.println("<Known />");
            } else {
                mOut.println("<Unknown />");
                mOut.println("<Message>Unable to locate within known authority.</Message>");
            }
        } else {
            if (mHigherAuthority != null) {
                String buffer = "";
                String url = mHigherAuthority + getURLParameters(id);
                URL urlSource = new URL(url);
                URLConnection con = urlSource.openConnection();
                InputStream stream = con.getInputStream();
                streamContent(stream, false);
            } else {
                mOut.println("<Message>Unable to locate authority.</Message>");
            }
        }
        mOut.println("</Check>");
    }
