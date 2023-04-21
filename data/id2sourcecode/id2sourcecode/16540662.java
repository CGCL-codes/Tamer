    protected byte[] sendOCSPRequest(OCSPReq ocspRequest, String oCSPURLString) throws IOException, SignServerException {
        byte[] reqarray = ocspRequest.getEncoded();
        URL url = new URL(oCSPURLString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setAllowUserInteraction(false);
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setInstanceFollowRedirects(false);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Length", Integer.toString(reqarray.length));
        con.setRequestProperty("Content-Type", "application/ocsp-request");
        con.connect();
        OutputStream os = con.getOutputStream();
        os.write(reqarray);
        os.close();
        if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new SignServerException("Response code unexpected. Expecting : HTTP_OK(200). Received :  " + con.getResponseCode());
        }
        if ((con.getContentType() == null) || !con.getContentType().equals("application/ocsp-response")) {
            throw new SignServerException("Response type unexpected. Expecting : application/ocsp-response, Received : " + con.getContentType());
        }
        byte[] responsearr = null;
        InputStream reader = con.getInputStream();
        int responselen = con.getContentLength();
        if (responselen != -1) {
            responsearr = new byte[responselen];
            int offset = 0;
            int bread;
            while ((responselen > 0) && (bread = reader.read(responsearr, offset, responselen)) != -1) {
                offset += bread;
                responselen -= bread;
            }
            if (responselen > 0) {
                throw new SignServerException("Unexpected EOF encountered while reading ocsp response from : " + oCSPURLString);
            }
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b;
            while ((b = reader.read()) != -1) {
                baos.write(b);
            }
            responsearr = baos.toByteArray();
        }
        reader.close();
        con.disconnect();
        return responsearr;
    }
