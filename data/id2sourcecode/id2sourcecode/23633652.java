    protected void engineInit(Key key, AlgorithmParameterSpec aps) throws InvalidKeyException, InvalidAlgorithmParameterException {
        initOnly = false;
        preInit = false;
        truncated = false;
        truncatedLength = 0;
        if (aps != null && !aps.getClass().equals(HMACParameterSpec.class)) {
            throw new InvalidAlgorithmParameterException("AlgorithmParameterSpec must be an instance of HMACParameterSpec");
        }
        HMACParameterSpec spec = (HMACParameterSpec) aps;
        if (spec != null && spec.getInitialBuffer() != null) {
            preInit = true;
        }
        if (preInit) {
            byte[] stored = spec.getInitialBuffer();
            int length = stored.length / 2;
            inner = new byte[length];
            outer = new byte[length];
            System.arraycopy(stored, 0, inner, 0, length);
            System.arraycopy(stored, length, outer, 0, length);
            macSupport.secondaryInit(inner);
            firstPass = false;
        } else {
            if (key == null && !preInit) {
                throw new InvalidKeyException("Secret key required");
            }
            if (!(key instanceof SecretKey)) {
                throw new InvalidKeyException("Secret key expected");
            }
            byte[] secret = key.getEncoded();
            if (secret == null || secret.length == 0) {
                throw new InvalidKeyException("No secret key found");
            }
            int byteLength = messageDigest.getByteLength();
            if (secret.length > byteLength) {
                byte[] temp = messageDigest.digest(secret);
                Arrays.fill(secret, (byte) 0);
                secret = temp;
            }
            inner = new byte[byteLength];
            outer = new byte[byteLength];
            for (int i = 0; i < secret.length; i++) {
                inner[i] = (byte) (secret[i] ^ 0x36);
                outer[i] = (byte) (secret[i] ^ 0x5c);
            }
            for (int i = secret.length; i < byteLength; i++) {
                inner[i] = (byte) (0 ^ 0x36);
                outer[i] = (byte) (0 ^ 0x5c);
            }
            Arrays.fill(secret, (byte) 0);
            secret = null;
            if (spec.isInitOnly()) {
                initOnly = true;
                int digestLength = messageDigest.getDigestLength();
                initOnlyBuffer = new byte[digestLength * 2];
                messageDigest.update(inner);
                System.arraycopy(macSupport.getBuffer(), 0, initOnlyBuffer, 0, digestLength);
                messageDigest.reset();
                messageDigest.update(outer);
                System.arraycopy(macSupport.getBuffer(), 0, initOnlyBuffer, digestLength, digestLength);
                messageDigest.reset();
            }
        }
        truncated = spec.isTruncated();
        if (truncated) {
            truncatedLength = messageDigest.getTruncationLength();
        }
    }
