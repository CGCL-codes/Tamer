    protected boolean engineVerify(byte[] signature) {
        byte[] shaMBytes = mdSHA1_.digest();
        byte[] mdMBytes = mdMD5_.digest();
        byte[] plain;
        try {
            plain = cipher_.doFinal(signature);
            for (int i = 0; i < 16; i++) {
                if (plain[i] != mdMBytes[i]) return false;
                if (plain[16 + i] != shaMBytes[i]) return false;
            }
            for (int i = 0; i < 4; i++) {
                if (plain[16 + 16 + i] != shaMBytes[16 + i]) return false;
            }
            return true;
        } catch (IllegalBlockSizeException ibse) {
            System.err.println("RSASignature: cipher.doFinal");
            ibse.printStackTrace();
        } catch (BadPaddingException bpe) {
            System.err.println("RSASignature: cipher.doFinal");
            bpe.printStackTrace();
        }
        return false;
    }
