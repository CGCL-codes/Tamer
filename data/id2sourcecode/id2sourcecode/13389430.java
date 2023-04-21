    private PaymentService(URL confURL) throws IOException {
        try {
            this.confURL = confURL;
            Properties props = loadProps(confURL);
            baseURL = props.getProperty("base-url");
            business = props.getProperty("business");
            certId = props.getProperty("cert-id");
            identityToken = props.getProperty("identity-token");
            connectTimeout = Integer.parseInt(props.getProperty("connect-timeout"));
            readTimeout = Integer.parseInt(props.getProperty("read-timeout"));
            KeyStore ks = KeyStore.getInstance("JKS");
            URL url = getResource(props.getProperty("key-store"));
            InputStream in = url.openStream();
            try {
                ks.load(in, null);
            } finally {
                in.close();
            }
            String keyAlias = props.getProperty("key-alias");
            String keyPwd = props.getProperty("key-password");
            ownKey = (PrivateKey) ks.getKey(keyAlias, keyPwd.toCharArray());
            ownCert = (X509Certificate) ks.getCertificate(keyAlias);
            String ppCertAlias = props.getProperty("paypal-cert-alias");
            paypalCert = (X509Certificate) ks.getCertificate(ppCertAlias);
        } catch (CertificateException e) {
            LOG.log(Level.SEVERE, "Error loading key store", e);
            throw new IOException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            LOG.log(Level.SEVERE, "Error loading key store", e);
            throw new IOException(e.getMessage());
        } catch (UnrecoverableKeyException e) {
            LOG.log(Level.SEVERE, "Error loading key store", e);
            throw new IOException(e.getMessage());
        } catch (KeyStoreException e) {
            LOG.log(Level.SEVERE, "Error loading key store", e);
            throw new IOException(e.getMessage());
        }
    }
