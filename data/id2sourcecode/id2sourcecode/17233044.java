    private void setupSigningParams() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnsupportedCallbackException, UnrecoverableKeyException {
        if (Configuration.DEBUG) log.entering(this.getClass().getName(), "setupSigningParams");
        if (ksURL == null || ksURL.trim().length() == 0) {
            String userHome = SystemProperties.getProperty("user.home");
            if (userHome == null || userHome.trim().length() == 0) throw new SecurityException(Messages.getString("Main.85"));
            ksURL = "file:" + userHome.trim() + "/.keystore";
        } else {
            ksURL = ksURL.trim();
            if (ksURL.indexOf(":") == -1) ksURL = "file:" + ksURL;
        }
        if (ksType == null || ksType.trim().length() == 0) ksType = KeyStore.getDefaultType(); else ksType = ksType.trim();
        store = KeyStore.getInstance(ksType);
        if (ksPassword == null) {
            PasswordCallback pcb = new PasswordCallback(Messages.getString("Main.92"), false);
            getCallbackHandler().handle(new Callback[] { pcb });
            ksPasswordChars = pcb.getPassword();
        } else ksPasswordChars = ksPassword.toCharArray();
        URL url = new URL(ksURL);
        InputStream stream = url.openStream();
        store.load(stream, ksPasswordChars);
        if (!store.containsAlias(alias)) throw new SecurityException(Messages.getFormattedString("Main.6", alias));
        if (!store.isKeyEntry(alias)) throw new SecurityException(Messages.getFormattedString("Main.95", alias));
        Key key;
        if (password == null) {
            passwordChars = ksPasswordChars;
            try {
                key = store.getKey(alias, passwordChars);
            } catch (UnrecoverableKeyException x) {
                String prompt = Messages.getFormattedString("Main.97", alias);
                PasswordCallback pcb = new PasswordCallback(prompt, false);
                getCallbackHandler().handle(new Callback[] { pcb });
                passwordChars = pcb.getPassword();
                key = store.getKey(alias, passwordChars);
            }
        } else {
            passwordChars = password.toCharArray();
            key = store.getKey(alias, passwordChars);
        }
        if (!(key instanceof PrivateKey)) throw new SecurityException(Messages.getFormattedString("Main.99", alias));
        signerPrivateKey = (PrivateKey) key;
        signerCertificateChain = store.getCertificateChain(alias);
        if (Configuration.DEBUG) log.fine(String.valueOf(signerCertificateChain));
        if (sigFileName == null) sigFileName = alias;
        sigFileName = sigFileName.toUpperCase(EN_US_LOCALE);
        if (sigFileName.length() > 8) sigFileName = sigFileName.substring(0, 8);
        char[] chars = sigFileName.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (!(Character.isLetter(c) || Character.isDigit(c) || c == '_' || c == '-')) chars[i] = '_';
        }
        sigFileName = new String(chars);
        if (signedJarFileName == null) signedJarFileName = jarFileName;
        if (Configuration.DEBUG) log.exiting(this.getClass().getName(), "setupSigningParams");
    }
