    void loadKeyStore(String keyStoreName, boolean prompt) {
        if (!nullStream && keyStoreName == null) {
            keyStoreName = System.getProperty("user.home") + File.separator + ".keystore";
        }
        try {
            if (providerName == null) {
                store = KeyStore.getInstance(storetype);
            } else {
                store = KeyStore.getInstance(storetype, providerName);
            }
            if (token && storepass == null && !protectedPath && !KeyStoreUtil.isWindowsKeyStore(storetype)) {
                storepass = getPass(rb.getString("Enter Passphrase for keystore: "));
            } else if (!token && storepass == null && prompt) {
                storepass = getPass(rb.getString("Enter Passphrase for keystore: "));
            }
            if (nullStream) {
                store.load(null, storepass);
            } else {
                keyStoreName = keyStoreName.replace(File.separatorChar, '/');
                URL url = null;
                try {
                    url = new URL(keyStoreName);
                } catch (java.net.MalformedURLException e) {
                    url = new File(keyStoreName).toURI().toURL();
                }
                InputStream is = null;
                try {
                    is = url.openStream();
                    store.load(is, storepass);
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            }
            Set<TrustAnchor> tas = new HashSet<TrustAnchor>();
            try {
                KeyStore caks = KeyTool.getCacertsKeyStore();
                if (caks != null) {
                    Enumeration<String> aliases = caks.aliases();
                    while (aliases.hasMoreElements()) {
                        String a = aliases.nextElement();
                        try {
                            tas.add(new TrustAnchor((X509Certificate) caks.getCertificate(a), null));
                        } catch (Exception e2) {
                        }
                    }
                }
            } catch (Exception e) {
            }
            if (store != null) {
                Enumeration<String> aliases = store.aliases();
                while (aliases.hasMoreElements()) {
                    String a = aliases.nextElement();
                    try {
                        X509Certificate c = (X509Certificate) store.getCertificate(a);
                        if (store.isCertificateEntry(a) || c.getSubjectDN().equals(c.getIssuerDN())) {
                            tas.add(new TrustAnchor(c, null));
                        }
                    } catch (Exception e2) {
                    }
                }
            }
            certificateFactory = CertificateFactory.getInstance("X.509");
            validator = CertPathValidator.getInstance("PKIX");
            try {
                pkixParameters = new PKIXParameters(tas);
                pkixParameters.setRevocationEnabled(false);
            } catch (InvalidAlgorithmParameterException ex) {
            }
        } catch (IOException ioe) {
            throw new RuntimeException(rb.getString("keystore load: ") + ioe.getMessage());
        } catch (java.security.cert.CertificateException ce) {
            throw new RuntimeException(rb.getString("certificate exception: ") + ce.getMessage());
        } catch (NoSuchProviderException pe) {
            throw new RuntimeException(rb.getString("keystore load: ") + pe.getMessage());
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(rb.getString("keystore load: ") + nsae.getMessage());
        } catch (KeyStoreException kse) {
            throw new RuntimeException(rb.getString("unable to instantiate keystore class: ") + kse.getMessage());
        }
    }
