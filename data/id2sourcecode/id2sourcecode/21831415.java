    private void addCRLFromURL(Collection<CRL> crlAndCerts, CertificateFactory certFactory) {
        DataInputStream data = null;
        try {
            URL url = new URL(urlCrlPath);
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setUseCaches(false);
            data = new DataInputStream(connection.getInputStream());
            CRL crl = certFactory.generateCRL(data);
            crlAndCerts.add(crl);
        } catch (MalformedURLException e) {
            logger.error(" bad uri synthax " + urlCrlPath, e);
        } catch (IOException e) {
            logger.error(" IOException when we wan to retrieve CRL with data ", e);
        } catch (CRLException e) {
            logger.error(" CRL cannot be built with the retrieved data ");
        } finally {
            try {
                if (data != null) {
                    data.close();
                }
            } catch (IOException e) {
                logger.error(" IOException when we close the DATAInputStream", e);
            }
        }
    }
