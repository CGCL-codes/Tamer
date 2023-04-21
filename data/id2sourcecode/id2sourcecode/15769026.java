    protected boolean engineVerify(byte[] signature) {
        byte[] shaMBytes = md_.digest();
        byte[] plain;
        try {
            plain = cipher_.doFinal(signature);
            DigestInfo di = new DigestInfo();
            ByteArrayInputStream bais = new ByteArrayInputStream(plain);
            DERDecoder decoder = new DERDecoder(bais);
            di.decode(decoder);
            decoder.close();
            ASN1ObjectIdentifier oid = (di.getAlgorithmIdentifier()).getAlgorithmOID();
            if (!oid.equals(md5oid_)) return false;
            byte[] hashBytes = di.getDigest();
            if (hashBytes.length != shaMBytes.length) {
                return false;
            }
            for (int i = 0; i < hashBytes.length; i++) {
                if (hashBytes[i] != shaMBytes[i]) {
                    return false;
                }
            }
            return true;
        } catch (IllegalBlockSizeException ibse) {
            System.err.println("RSASignature: cipher.doFinal");
            ibse.printStackTrace();
        } catch (BadPaddingException bpe) {
            System.err.println("RSASignature: cipher.doFinal");
            bpe.printStackTrace();
        } catch (ASN1Exception ae) {
            System.out.println("internal error:");
            ae.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("internal error:");
            ioe.printStackTrace();
        }
        return false;
    }
