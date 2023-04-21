    public int makeOCSPRequest(final X509Certificate issuerCert, final X509Certificate userCert, final String serviceAddr) throws OCSPException, org.bouncycastle.ocsp.OCSPException {
        OCSPReq request;
        try {
            request = generateOCSPRequest(issuerCert, userCert.getSerialNumber());
        } catch (final OCSPException e) {
            e.printStackTrace();
            throw new OCSPException("UNABLE TO CREATE REQUEST");
        }
        byte[] array;
        try {
            array = request.getEncoded();
        } catch (final IOException e) {
            e.printStackTrace();
            throw new OCSPException("UNABLE TO PREPARE REQUEST");
        }
        if (serviceAddr == null) {
            throw new OCSPException("NO OCSP ADDRESS");
        }
        if (!serviceAddr.startsWith("http")) {
            throw new OCSPException("UNSUPPORTED PROTOCOL");
        }
        HttpURLConnection con = null;
        URL url;
        try {
            url = new URL(serviceAddr);
        } catch (final MalformedURLException e) {
            e.printStackTrace();
            throw new OCSPException("MALFORMED OCSP ADDRESS");
        }
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (final IOException e) {
            e.printStackTrace();
            throw new OCSPException("UNABLE TO CONNECT OCSP ADDRESS");
        }
        con.setRequestProperty("Content-Type", "application/ocsp-request");
        con.setRequestProperty("Accept", "application/ocsp-response");
        con.setDoOutput(true);
        OCSPResp ocspResponse;
        try {
            final OutputStream out = con.getOutputStream();
            final DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(out));
            dataOut.write(array);
            dataOut.flush();
            dataOut.close();
            if (con.getResponseCode() / 100 != 2) {
                throw new OCSPException("HTTP STATUS ERROR");
            }
            final InputStream in = (InputStream) con.getContent();
            ocspResponse = new OCSPResp(in);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new OCSPException("UNABLE TO CONNECT OCSP ADDRESS");
        }
        final int status = ocspResponse.getStatus();
        BasicOCSPResp basicResponse;
        try {
            basicResponse = (BasicOCSPResp) ocspResponse.getResponseObject();
        } catch (final org.bouncycastle.ocsp.OCSPException e) {
            e.printStackTrace();
            throw new OCSPException("UNABLE TO PARSE RESPONSE");
        }
        if (basicResponse == null) {
            throw new OCSPException("NO RESPONSE FROM SERVER");
        }
        final SingleResp[] responses = basicResponse.getResponses();
        if (responses.length == 0) {
            throw new OCSPException("NO RESPONSE FROM SERVER");
        }
        if (responses.length != 1) {
            throw new OCSPException("TOO MANY RESPONSES");
        }
        final SingleResp resp = responses[0];
        final Object status2 = resp.getCertStatus();
        if (status2 == CertificateStatus.GOOD) {
            return 0;
        }
        if (status2 instanceof RevokedStatus) {
            return 1;
        }
        if (status2 instanceof UnknownStatus) {
            throw new OCSPException("STATUS UNKNOWN");
        }
        if (status == 0) {
            return 0;
        }
        if (status == 1) {
            return 1;
        }
        if (status == 2) {
            return 2;
        }
        return 2;
    }
