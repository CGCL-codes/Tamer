    public EncryptionPoint(String password) {
        byte[] salt = new byte[8];
        Double theSum = new Double(computeSum(password));
        for (int i = 0; i < salt.length; i++) {
            salt[i] = theSum.byteValue();
            theSum = (theSum % 10) * 3;
        }
        int interator = getPrime(computeSum(password) % 100);
        interator = interator % 100;
        PBEKeySpec theSpec = new PBEKeySpec(password.toCharArray());
        PBEParameterSpec theParamSpec = new PBEParameterSpec(salt, interator);
        try {
            SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey pbeKey = keyFac.generateSecret(theSpec);
            encCipher = Cipher.getInstance(pbeKey.getAlgorithm());
            encCipher.init(Cipher.ENCRYPT_MODE, pbeKey, theParamSpec);
            decCipher = Cipher.getInstance(pbeKey.getAlgorithm());
            decCipher.init(Cipher.DECRYPT_MODE, pbeKey, theParamSpec);
            MessageDigest myDigest = MessageDigest.getInstance("MD5");
            passwordHash = myDigest.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }
