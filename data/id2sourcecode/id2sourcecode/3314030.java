    public static ServerSocketChannel getSSLServerSocketChannel(String keystore, Secrets secrets) throws CertificateException, KeyManagementException, KeyStoreException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException {
        ServerSocketFactory f = getSSLServerSocketFactory(keystore, secrets);
        return f.createServerSocket().getChannel();
    }
