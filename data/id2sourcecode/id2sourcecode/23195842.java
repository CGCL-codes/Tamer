    public static X509CRL fetchCRLFromURL(URL url, CertificateFactory certFactory) throws SignServerException {
        URLConnection connection;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            throw new SignServerException("Error opening connection for fetching CRL from address : " + url.toString(), e);
        }
        connection.setDoInput(true);
        byte[] responsearr = null;
        InputStream reader = null;
        try {
            try {
                reader = connection.getInputStream();
            } catch (IOException e) {
                throw new SignServerException("Error getting input stream for fetching CRL from address : " + url.toString(), e);
            }
            int responselen = connection.getContentLength();
            if (responselen != -1) {
                responsearr = new byte[responselen];
                int offset = 0;
                int bread;
                try {
                    while ((responselen > 0) && (bread = reader.read(responsearr, offset, responselen)) != -1) {
                        offset += bread;
                        responselen -= bread;
                    }
                } catch (IOException e) {
                    throw new SignServerException("Error reading CRL bytes from address : " + url.toString(), e);
                }
                if (responselen > 0) {
                    throw new SignServerException("Unexpected EOF encountered while reading crl from : " + url.toString());
                }
            } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int b;
                try {
                    while ((b = reader.read()) != -1) {
                        baos.write(b);
                    }
                } catch (IOException e) {
                    throw new SignServerException("Error reading input stream for fetching CRL from address (no length header): " + url.toString(), e);
                }
                responsearr = baos.toByteArray();
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    LOG.info("Could not close stream after reading CRL", ex);
                }
            }
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(responsearr);
        X509CRL crl;
        try {
            crl = (X509CRL) certFactory.generateCRL(bis);
        } catch (CRLException e) {
            throw new SignServerException("Error creating CRL object with bytes from address : " + url.toString(), e);
        }
        return crl;
    }
