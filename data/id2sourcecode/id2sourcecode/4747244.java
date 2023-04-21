    private void encrypt() throws InterruptedException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        final IMessageDigest hash = AlgorithmNames.getHashAlgorithm(hashName);
        final IBlockCipher cipher = AlgorithmNames.getCipherAlgorithm(cipherName);
        final int blockLength = cipher.defaultBlockSize();
        final IMode crypt = ModeFactory.getInstance("CBC", cipher, blockLength);
        FileOutputStream out = null;
        FileInputStream in = null;
        try {
            out = new FileOutputStream(outputFile);
            out.write(Convert.toBytes(HEADER[0]));
            out.write(Convert.toBytes(HEADER[1]));
            out.write(Convert.toBytes(HEADER[2]));
            final String algos = cipherName + "/" + hashName;
            out.write((byte) algos.length());
            out.write(algos.getBytes());
            hash.update(keyData, 0, keyData.length);
            final byte[] key = hash.digest();
            final Map<String, Object> attributes = new HashMap<String, Object>();
            final int keyLength = AlgorithmNames.getCipherAlgorithmKeySize(cipherName) / Byte.SIZE;
            final byte[] correctedKey = new byte[keyLength];
            System.arraycopy(key, 0, correctedKey, 0, keyLength < key.length ? keyLength : key.length);
            attributes.put(IMode.KEY_MATERIAL, correctedKey);
            attributes.put(IMode.CIPHER_BLOCK_SIZE, Integer.valueOf(blockLength));
            attributes.put(IMode.STATE, Integer.valueOf(IMode.ENCRYPTION));
            hash.reset();
            hash.update(key, 0, key.length);
            final byte[] iv = hash.digest();
            final byte[] correctedIV = new byte[keyLength];
            System.arraycopy(iv, 0, correctedIV, 0, keyLength < key.length ? keyLength : key.length);
            attributes.put(IMode.IV, correctedIV);
            crypt.init(attributes);
            byte buffer[] = new byte[Long.SIZE / Byte.SIZE];
            PRNG.nextBytes(buffer);
            final long x = Convert.longFromBytes(buffer);
            PRNG.nextBytes(buffer);
            final long y = Convert.longFromBytes(buffer);
            encryptedWrite(out, Convert.toBytes(x), crypt);
            encryptedWrite(out, Convert.toBytes(y), crypt);
            encryptedWrite(out, Convert.toBytes(x ^ y), crypt);
            buffer = new byte[Short.SIZE / Byte.SIZE];
            PRNG.nextBytes(buffer);
            short sr = (short) (Convert.shortFromBytes(buffer) & 0x00FF);
            buffer = new byte[sr];
            PRNG.nextBytes(buffer);
            encryptedWrite(out, Convert.toBytes((byte) sr), crypt);
            encryptedWrite(out, buffer, crypt);
            encryptedWrite(out, Convert.toBytes((byte) 2), crypt);
            encryptedWrite(out, Convert.toBytes(MetaData.SIZE.getTagValue()), crypt);
            encryptedWrite(out, Convert.toBytes((short) (Long.SIZE / Byte.SIZE)), crypt);
            decryptedSize = sourceFile.length();
            encryptedWrite(out, Convert.toBytes(decryptedSize), crypt);
            encryptedWrite(out, Convert.toBytes(MetaData.BLOCKED.getTagValue()), crypt);
            encryptedWrite(out, Convert.toBytes((short) (Long.SIZE / Byte.SIZE)), crypt);
            encryptedWrite(out, Convert.toBytes((long) BLOCK_SIZE), crypt);
            in = new FileInputStream(sourceFile);
            buffer = new byte[BLOCK_SIZE];
            hash.reset();
            boolean b1 = true;
            while (b1) {
                if (interrupted()) throw new InterruptedException();
                PRNG.nextBytes(buffer);
                final int r = in.read(buffer, 0, BLOCK_SIZE);
                if (r < BLOCK_SIZE) b1 = false;
                encryptedWrite(out, Convert.toBytes(b1), crypt);
                encryptedWrite(out, buffer, crypt);
                hash.update(buffer, 0, r);
                if (!b1) encryptedWrite(out, Convert.toBytes((long) r), crypt);
                bytesProcessed += r;
            }
            final byte[] digest = hash.digest();
            encryptedWrite(out, digest, crypt);
            buffer = new byte[Short.SIZE / Byte.SIZE];
            PRNG.nextBytes(buffer);
            sr = Convert.shortFromBytes(buffer);
            buffer = new byte[sr];
            PRNG.nextBytes(buffer);
            encryptedWrite(out, buffer, crypt);
            encryptedWrite(out, null, crypt);
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
            } catch (final Exception e) {
                ;
            }
        }
    }
