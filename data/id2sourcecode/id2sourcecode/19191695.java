    public boolean write(char[] key) {
        try {
            fleWrite.delete();
            fleWrite.createNewFile();
            byte[] k1 = { (byte) 10, (byte) 35, (byte) 40, (byte) 44, (byte) 123, (byte) 37, (byte) 55, (byte) 41 };
            KeySpec keySpec = new PBEKeySpec(key, k1, 2);
            SecretKey scKey;
            scKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(k1, 2);
            Cipher cptFile = Cipher.getInstance(scKey.getAlgorithm());
            cptFile.init(Cipher.ENCRYPT_MODE, scKey, paramSpec);
            FileOutputStream fosOut = new FileOutputStream(fleWrite);
            CipherOutputStream cosOut = new CipherOutputStream(fosOut, cptFile);
            writeXML(cosOut);
            cosOut.flush();
            cosOut.close();
            fosOut.flush();
            fosOut.close();
            return true;
        } catch (IOException ex) {
            System.out.println("write");
            System.out.println(ex.getClass());
            return false;
        } catch (NoSuchAlgorithmException ex1) {
            return false;
        } catch (InvalidKeySpecException ex2) {
            System.out.println(ex2.getMessage());
            return false;
        } catch (NoSuchPaddingException ex3) {
            return false;
        } catch (InvalidKeyException ex4) {
            return false;
        } catch (InvalidAlgorithmParameterException ex5) {
            return false;
        }
    }
