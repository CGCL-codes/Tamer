    private PBuffer fetch(String sReqIn) {
        URL url;
        byte[] ab;
        int iBytesRead;
        InputStream is;
        String sUrlEncodedReq;
        ByteArrayOutputStream baos;
        try {
            sUrlEncodedReq = SConnection.stringToHttp("/pearls.ScriptureViewerModel?" + sReqIn);
            url = new URL(urlBase, sUrlEncodedReq);
            is = url.openStream();
            baos = new ByteArrayOutputStream();
            synchronized (abBuffer) {
                while ((iBytesRead = is.read(abBuffer)) >= 0) {
                    baos.write(abBuffer, 0, iBytesRead);
                }
            }
            is.close();
            ab = baos.toByteArray();
            return (new PBuffer(ab, 0, ab.length));
        } catch (Exception e) {
            System.out.println("Error fetching " + sReqIn + ": " + e);
        }
        return (null);
    }
