    public static boolean processOCSPRequest(final Certificate issuer, final Certificate cert, final String hostaddr) throws OCSPException, IOException, CertificateException {
        final OCSPReq request = generateOCSPRequest(issuer.getX509Certificate(), cert.getX509Certificate().getSerialNumber());
        final byte[] array = request.getEncoded();
        HttpURLConnection con = null;
        final URL url = new URL((String) hostaddr);
        con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Content-Type", "application/ocsp-request");
        con.setRequestProperty("Accept", "application/ocsp-response");
        con.setDoOutput(true);
        final OutputStream out = con.getOutputStream();
        final DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(out));
        dataOut.write(array);
        dataOut.flush();
        dataOut.close();
        final InputStream in = (InputStream) con.getContent();
        if (con.getResponseCode() / 100 != 2) {
            return false;
        }
        final OCSPResp ocspResponse = new OCSPResp(in);
        final BasicOCSPResp basicResponse = (BasicOCSPResp) ocspResponse.getResponseObject();
        if (basicResponse == null) {
            return false;
        } else {
            final SingleResp[] responses = basicResponse.getResponses();
            final SingleResp resp = responses[0];
            final Object status = resp.getCertStatus();
            if (status instanceof org.bouncycastle.ocsp.RevokedStatus) {
                cert.setStatus(CertificateStatus.BEID_CERTSTATUS_CERT_REVOKED);
                return false;
            } else if (status instanceof org.bouncycastle.ocsp.UnknownStatus) {
                cert.setStatus(CertificateStatus.BEID_CERTSTATUS_CERT_UNKNOWN);
                return false;
            } else {
                if (cert.getStatus().equals(CertificateStatus.BEID_CERTSTATUS_CERT_NOT_VALIDATED)) {
                    cert.setStatus(CertificateStatus.BEID_CERTSTATUS_CERT_VALIDATED_OK);
                }
                return true;
            }
        }
    }
