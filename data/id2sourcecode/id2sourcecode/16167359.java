    private byte[] getSignature(byte[] rawxml, PrivateKey pk) {
        try {
            Signature instance = Signature.getInstance("SHA1withRSA");
            instance.initSign(pk);
            MessageDigest digest = MessageDigest.getInstance("sha1");
            rawxml = digest.digest(rawxml);
            byte[][] digs = new byte[1][];
            digs[0] = rawxml;
            rawxml = generateSignedInfo(digs, new String[] { SecurityManager.bodyPartID });
            rawxml = digest.digest(rawxml);
            instance.update(rawxml);
            byte[] signature = instance.sign();
            return signature;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }
