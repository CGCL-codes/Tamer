    public void initEncrypt(Key key, SecureRandom secureRandom, AlgorithmParameterSpec params) throws InvalidKeyException {
        if (key instanceof RSAPublicKey) {
            rsaPublicKey_ = (RSAPublicKey) key;
            rsaPrivateKey_ = null;
            secureRandom_ = secureRandom;
            try {
                md_ = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException nsae) {
                System.err.println("SHA-1 Algorithm not found");
                nsae.printStackTrace();
            }
            cipherBlockSize_ = ((rsaPublicKey_.getModulus().bitLength()) + 7) / 8;
            blockSize_ = cipherBlockSize_ - 2 * (md_.getDigestLength()) - 2;
        } else {
            throw new InvalidKeyException("Key is not a RSAPublicKey!");
        }
        if (params instanceof RSAOAEPAlgorithmParameterSpec) {
            ASN1Sequence paramSeq = (ASN1Sequence) ((RSAOAEPAlgorithmParameterSpec) params).getParameters();
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DEREncoder dec = new DEREncoder(baos);
                paramSeq.encode(dec);
                byte[] paramBytes = baos.toByteArray();
                dec.close();
                md_.update(paramBytes);
                pHash_ = md_.digest();
            } catch (IOException ioe) {
                System.out.println("shouldn't happen");
                ioe.printStackTrace();
            } catch (ASN1Exception ae) {
                System.out.println("shouldn't happen");
                ae.printStackTrace();
            }
        }
    }
