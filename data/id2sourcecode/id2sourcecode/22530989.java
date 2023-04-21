    public void setPwd(byte[] pwd) {
        try {
            Cipher cipher = Cipher.getInstance("DES");
            Key key = getKey();
            if (key == null) {
                key = KeyGenerator.getInstance("DES").generateKey();
                writeKey(key);
            }
            cipher.init(Cipher.ENCRYPT_MODE, key);
            pwd = cipher.doFinal(pwd);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.pwd = pwd;
    }
