    public static OutputStream getPasswordProtectedOutputStream(String password, OutputStream stream) throws IOException {
        stream.write(getDigest(password));
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        try {
            SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
            cipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
            stream = new CipherOutputStream(stream, cipher);
            stream = new DeflaterOutputStream(stream);
            return stream;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }
