    public Uri encryptFileWithSessionKey(ContentResolver contentResolver, Uri fileUri) throws CryptoHelperException {
        if (debug) Log.d(TAG, "Encrypt with session key");
        status = false;
        if (password == null) {
            String msg = "Must call setPassword before runing encrypt.";
            throw new CryptoHelperException(msg);
        }
        String outputPath = "";
        try {
            InputStream is;
            if (fileUri.getScheme().equals("file")) {
                is = new java.io.FileInputStream(fileUri.getPath());
                outputPath = fileUri.getPath() + OISAFE_EXTENSION;
            } else {
                is = contentResolver.openInputStream(fileUri);
                outputPath = getTemporaryFileName();
            }
            FileOutputStream os = new FileOutputStream(outputPath);
            byte[] cipherSessionKey = {};
            SecretKey sessionKey = null;
            byte[] sessionKeyEncoded = null;
            try {
                KeyGenerator keygen;
                keygen = KeyGenerator.getInstance("AES");
                keygen.init(256);
                sessionKey = keygen.generateKey();
                sessionKeyEncoded = sessionKey.getEncoded();
            } catch (NoSuchAlgorithmException e) {
                Log.e(TAG, "generateMasterKey(): " + e.toString());
                return null;
            }
            try {
                pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
                cipherSessionKey = pbeCipher.doFinal(sessionKeyEncoded);
                status = true;
            } catch (IllegalBlockSizeException e) {
                Log.e(TAG, "encryptWithSessionKey(): " + e.toString());
            } catch (BadPaddingException e) {
                Log.e(TAG, "encryptWithSessionKey(): " + e.toString());
            } catch (InvalidKeyException e) {
                Log.e(TAG, "encryptWithSessionKey(): " + e.toString());
            } catch (InvalidAlgorithmParameterException e) {
                Log.e(TAG, "encryptWithSessionKey(): " + e.toString());
            }
            if (status == false) {
                return null;
            }
            status = false;
            String stringCipherVersion = "A";
            byte[] bytesCipherVersion = stringCipherVersion.getBytes();
            os.write(bytesCipherVersion, 0, bytesCipherVersion.length);
            os.write(cipherSessionKey, 0, cipherSessionKey.length);
            if (debug) Log.d(TAG, "bytesCipherVersion.length: " + bytesCipherVersion.length);
            if (debug) Log.d(TAG, "cipherSessionKey.length: " + cipherSessionKey.length);
            Trivium tri = new Trivium();
            try {
                tri.setupKey(Trivium.MODE_ENCRYPT, sessionKeyEncoded, 0);
                tri.setupNonce(sessionKeyEncoded, 10);
                final int bytesLen = 4096;
                byte[] bytesIn = new byte[bytesLen];
                byte[] bytesOut = new byte[bytesLen];
                int offset = 0;
                int numRead = 0;
                while ((numRead = is.read(bytesIn, 0, bytesLen)) >= 0) {
                    tri.process(bytesIn, 0, bytesOut, 0, numRead);
                    os.write(bytesOut, 0, numRead);
                    offset += numRead;
                }
                if (offset < is.available()) {
                    throw new IOException("Could not completely read file ");
                }
                is.close();
                os.close();
                SecureDelete.delete(new File(fileUri.getPath()));
                status = true;
            } catch (ESJException e) {
                Log.e(TAG, "Error encrypting file", e);
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found", e);
        } catch (IOException e) {
            Log.e(TAG, "IO Exception", e);
        }
        if (status == false) {
            return null;
        }
        return Uri.fromFile(new File(outputPath));
    }
