    public static SoapHttpDynamicInfosetInvoker createSecureInvoker(GlobalContext globalConfiguration, boolean useHostKey) throws GeneralSecurityException, FileNotFoundException, IOException, XregistryException, GSSException {
        String certFile = globalConfiguration.getTrustedCertsFile();
        String keyfile = globalConfiguration.getHostcertsKeyFile();
        SoapHttpDynamicInfosetInvoker invoker;
        if (useHostKey && globalConfiguration.getTrustedCertificates() != null && keyfile != null) {
            PureTLSContext ctx = new PureTLSContext();
            ctx.setTrustedCertificates(globalConfiguration.getTrustedCertificates());
            ctx.loadEAYKeyFile(keyfile, "");
            SSLPolicyInt policy = new SSLPolicyInt();
            policy.requireClientAuth(true);
            policy.setAcceptNoClientCert(true);
            ctx.setPolicy(policy);
            invoker = new PuretlsInvoker(ctx);
        } else if (useHostKey && certFile != null && keyfile != null && new File(certFile).isFile()) {
            invoker = new PuretlsInvoker(keyfile, "", certFile);
        } else {
            X509Certificate[] certs = globalConfiguration.getTrustedCertificates();
            if (certs != null && certFile != null) {
                TrustedCertificates certificates = TrustedCertificates.load(certFile);
                TrustedCertificates.setDefaultTrustedCertificates(certificates);
                certs = certificates.getCertificates();
            } else {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                InputStream trustedCasInStream = cl.getResourceAsStream("xregistry/trusted_cas.pem");
                if (trustedCasInStream != null) {
                    File tempTrustedCas = File.createTempFile("trusted_cas", "pem");
                    FileOutputStream tempTrustedCasOut = new FileOutputStream(tempTrustedCas);
                    tempTrustedCasOut.write(Utils.readFromStream(trustedCasInStream).getBytes());
                    tempTrustedCasOut.close();
                    certs = CertUtil.loadCertificates(tempTrustedCas.getAbsolutePath());
                    tempTrustedCas.deleteOnExit();
                } else {
                    throw new XregistryException("Server is secured, but can not find trusted certificates file");
                }
            }
            GSSCredential credential = globalConfiguration.getCredential();
            invoker = new GsiInvoker(credential, certs);
            globalConfiguration.setUserDN(credential.getName().toString());
        }
        return invoker;
    }
