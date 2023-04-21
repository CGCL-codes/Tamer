    static OCSPResponse check(List<CertId> certIds, URI responderURI, X509Certificate responderCert, Date date) throws IOException, CertPathValidatorException {
        byte[] bytes = null;
        try {
            OCSPRequest request = new OCSPRequest(certIds);
            bytes = request.encodeBytes();
        } catch (IOException ioe) {
            throw new CertPathValidatorException("Exception while encoding OCSPRequest", ioe);
        }
        InputStream in = null;
        OutputStream out = null;
        byte[] response = null;
        try {
            URL url = responderURI.toURL();
            if (debug != null) {
                debug.println("connecting to OCSP service at: " + url);
            }
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(CONNECT_TIMEOUT);
            con.setReadTimeout(CONNECT_TIMEOUT);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "application/ocsp-request");
            con.setRequestProperty("Content-length", String.valueOf(bytes.length));
            out = con.getOutputStream();
            out.write(bytes);
            out.flush();
            if (debug != null && con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                debug.println("Received HTTP error: " + con.getResponseCode() + " - " + con.getResponseMessage());
            }
            in = con.getInputStream();
            int contentLength = con.getContentLength();
            if (contentLength == -1) {
                contentLength = Integer.MAX_VALUE;
            }
            response = new byte[contentLength > 2048 ? 2048 : contentLength];
            int total = 0;
            while (total < contentLength) {
                int count = in.read(response, total, response.length - total);
                if (count < 0) break;
                total += count;
                if (total >= response.length && total < contentLength) {
                    response = Arrays.copyOf(response, total * 2);
                }
            }
            response = Arrays.copyOf(response, total);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    throw ioe;
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ioe) {
                    throw ioe;
                }
            }
        }
        OCSPResponse ocspResponse = null;
        try {
            ocspResponse = new OCSPResponse(response, date, responderCert);
        } catch (IOException ioe) {
            throw new CertPathValidatorException(ioe);
        }
        if (ocspResponse.getResponseStatus() != ResponseStatus.SUCCESSFUL) {
            throw new CertPathValidatorException("OCSP response error: " + ocspResponse.getResponseStatus());
        }
        for (CertId certId : certIds) {
            SingleResponse sr = ocspResponse.getSingleResponse(certId);
            if (sr == null) {
                if (debug != null) {
                    debug.println("No response found for CertId: " + certId);
                }
                throw new CertPathValidatorException("OCSP response does not include a response for a " + "certificate supplied in the OCSP request");
            }
            if (debug != null) {
                debug.println("Status of certificate (with serial number " + certId.getSerialNumber() + ") is: " + sr.getCertStatus());
            }
        }
        return ocspResponse;
    }
