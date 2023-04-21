    public CipherText encrypt(SecretKey key, PlainText plain) throws EncryptionException {
        byte[] plaintext = plain.asBytes();
        boolean overwritePlaintext = ESAPI.securityConfiguration().overwritePlainText();
        assert key != null : "(Master) encryption key may not be null";
        boolean success = false;
        String xform = null;
        int keySize = key.getEncoded().length * 8;
        try {
            xform = ESAPI.securityConfiguration().getCipherTransformation();
            String[] parts = xform.split("/");
            assert parts.length == 3 : "Malformed cipher transformation: " + xform;
            String cipherMode = parts[1];
            if (!CryptoHelper.isAllowedCipherMode(cipherMode)) {
                throw new EncryptionException("Encryption failure: invalid cipher mode ( " + cipherMode + ") for encryption", "Encryption failure: Cipher transformation " + xform + " specifies invalid " + "cipher mode " + cipherMode);
            }
            Cipher encrypter = Cipher.getInstance(xform);
            String cipherAlg = encrypter.getAlgorithm();
            int keyLen = ESAPI.securityConfiguration().getEncryptionKeyLength();
            if (keySize != keyLen) {
                logger.warning(Logger.SECURITY_FAILURE, "Encryption key length mismatch. ESAPI.EncryptionKeyLength is " + keyLen + " bits, but length of actual encryption key is " + keySize + " bits.  Did you remember to regenerate your master key (if that is what you are using)???");
            }
            if (keySize < keyLen) {
                logger.warning(Logger.SECURITY_FAILURE, "Actual key size of " + keySize + " bits SMALLER THAN specified " + "encryption key length (ESAPI.EncryptionKeyLength) of " + keyLen + " bits with cipher algorithm " + cipherAlg);
            }
            if (keySize < 112) {
                logger.warning(Logger.SECURITY_FAILURE, "Potentially unsecure encryption. Key size of " + keySize + "bits " + "not sufficiently long for " + cipherAlg + ". Should use appropriate algorithm with key size " + "of *at least* 112 bits except when required by legacy apps. See NIST Special Pub 800-57.");
            }
            String skeyAlg = key.getAlgorithm();
            if (!(cipherAlg.startsWith(skeyAlg + "/") || cipherAlg.equals(skeyAlg))) {
                logger.warning(Logger.SECURITY_FAILURE, "Encryption mismatch between cipher algorithm (" + cipherAlg + ") and SecretKey algorithm (" + skeyAlg + "). Cipher will use algorithm " + cipherAlg);
            }
            byte[] ivBytes = null;
            CipherSpec cipherSpec = new CipherSpec(encrypter, keySize);
            boolean preferredCipherMode = CryptoHelper.isCombinedCipherMode(cipherMode);
            SecretKey encKey = null;
            if (preferredCipherMode) {
                encKey = key;
            } else {
                encKey = computeDerivedKey(KeyDerivationFunction.kdfVersion, getDefaultPRF(), key, keySize, "encryption");
            }
            if (cipherSpec.requiresIV()) {
                String ivType = ESAPI.securityConfiguration().getIVType();
                IvParameterSpec ivSpec = null;
                if (ivType.equalsIgnoreCase("random")) {
                    ivBytes = ESAPI.randomizer().getRandomBytes(encrypter.getBlockSize());
                } else if (ivType.equalsIgnoreCase("fixed")) {
                    String fixedIVAsHex = ESAPI.securityConfiguration().getFixedIV();
                    ivBytes = Hex.decode(fixedIVAsHex);
                } else {
                    throw new ConfigurationException("Property Encryptor.ChooseIVMethod must be set to 'random' or 'fixed'");
                }
                ivSpec = new IvParameterSpec(ivBytes);
                cipherSpec.setIV(ivBytes);
                encrypter.init(Cipher.ENCRYPT_MODE, encKey, ivSpec);
            } else {
                encrypter.init(Cipher.ENCRYPT_MODE, encKey);
            }
            logger.debug(Logger.EVENT_SUCCESS, "Encrypting with " + cipherSpec);
            byte[] raw = encrypter.doFinal(plaintext);
            CipherText ciphertext = new CipherText(cipherSpec, raw);
            if (!preferredCipherMode) {
                SecretKey authKey = computeDerivedKey(KeyDerivationFunction.kdfVersion, getDefaultPRF(), key, keySize, "authenticity");
                ciphertext.computeAndStoreMAC(authKey);
            }
            logger.debug(Logger.EVENT_SUCCESS, "JavaEncryptor.encrypt(SecretKey,byte[],boolean,boolean) -- success!");
            success = true;
            return ciphertext;
        } catch (InvalidKeyException ike) {
            throw new EncryptionException("Encryption failure: Invalid key exception.", "Requested key size: " + keySize + "bits greater than 128 bits. Must install unlimited strength crypto extension from Sun: " + ike.getMessage(), ike);
        } catch (ConfigurationException cex) {
            throw new EncryptionException("Encryption failure: Configuration error. Details in log.", "Key size mismatch or unsupported IV method. " + "Check encryption key size vs. ESAPI.EncryptionKeyLength or Encryptor.ChooseIVMethod property.", cex);
        } catch (InvalidAlgorithmParameterException e) {
            throw new EncryptionException("Encryption failure (invalid IV)", "Encryption problem: Invalid IV spec: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            throw new EncryptionException("Encryption failure (no padding used; invalid input size)", "Encryption problem: Invalid input size without padding (" + xform + "). " + e.getMessage(), e);
        } catch (BadPaddingException e) {
            throw new EncryptionException("Encryption failure", "[Note: Should NEVER happen in encryption mode.] Encryption problem: " + e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException("Encryption failure (unavailable cipher requested)", "Encryption problem: specified algorithm in cipher xform " + xform + " not available: " + e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptionException("Encryption failure (unavailable padding scheme requested)", "Encryption problem: specified padding scheme in cipher xform " + xform + " not available: " + e.getMessage(), e);
        } finally {
            if (success && overwritePlaintext) {
                plain.overwrite();
            }
        }
    }
