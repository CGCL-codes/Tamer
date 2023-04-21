    public void setSocksProxyPassword(String val) {
        if (get("isSaveProxyPassword", isSaveProxyPassword) && myPropertyFileCipher != null) {
            try {
                myPropertyFileCipher.init(Cipher.ENCRYPT_MODE, myCipherKey, myCipherParams);
                set("socksProxyPassword", getString(myPropertyFileCipher.doFinal(getBytes(val))));
                set("socksProxyPasswordHash", getString(myMessageDigester.digest(getBytes(val))));
            } catch (GeneralSecurityException e) {
                Trace.display(e, "Cannot encrypt password; it will not be saved");
            } catch (IllegalStateException e) {
                Trace.display(e, "Cannot encrypt password; it will not be saved");
            }
        }
        socksProxyPassword = val;
    }
