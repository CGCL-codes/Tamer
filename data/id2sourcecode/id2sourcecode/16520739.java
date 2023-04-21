    protected byte[] engineSign() throws SignatureException {
        byte[] out = null;
        byte[] shaMBytes = mdSHA1_.digest();
        byte[] mdMBytes = mdMD5_.digest();
        byte[] plainSig = new byte[16 + 20];
        System.arraycopy(mdMBytes, 0, plainSig, 0, 16);
        System.arraycopy(shaMBytes, 0, plainSig, 16, 20);
        try {
            out = cipher_.doFinal(plainSig);
            return out;
        } catch (IllegalBlockSizeException ibse) {
            System.err.println("RSASignature: cipher.doFinal");
            ibse.printStackTrace();
        } catch (BadPaddingException bpe) {
            System.err.println("RSASignature: cipher.doFinal");
            bpe.printStackTrace();
        }
        return null;
    }
