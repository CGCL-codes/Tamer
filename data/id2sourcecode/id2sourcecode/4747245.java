    private void decrypt() throws InterruptedException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidParameterException, UnsupportedEncodingException, SignatureException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(sourceFile);
            for (int i = 0; i < 3; i++) {
                byte[] header = new byte[Long.SIZE / Byte.SIZE];
                in.read(header, 0, header.length);
            }
            int length = in.read();
            byte[] buffer = new byte[length];
            in.read(buffer, 0, buffer.length);
            final String algos = new String(buffer);
            final String cipherName = algos.substring(0, algos.indexOf('/'));
            final String hashName = algos.substring(algos.indexOf('/') + 1);
            final IMessageDigest hash = AlgorithmNames.getHashAlgorithm(hashName);
            final IBlockCipher cipher = AlgorithmNames.getCipherAlgorithm(cipherName);
            final int blockLength = cipher.defaultBlockSize();
            final IMode crypt = ModeFactory.getInstance("CBC", cipher, blockLength);
            hash.update(keyData, 0, keyData.length);
            final byte[] key = hash.digest();
            final Map<String, Object> attributes = new HashMap<String, Object>();
            final int keyLength = AlgorithmNames.getCipherAlgorithmKeySize(cipherName) / Byte.SIZE;
            final byte[] correctedKey = new byte[keyLength];
            System.arraycopy(key, 0, correctedKey, 0, keyLength < key.length ? keyLength : key.length);
            attributes.put(IMode.KEY_MATERIAL, correctedKey);
            attributes.put(IMode.CIPHER_BLOCK_SIZE, Integer.valueOf(blockLength));
            attributes.put(IMode.STATE, Integer.valueOf(IMode.DECRYPTION));
            hash.reset();
            hash.update(key, 0, key.length);
            final byte[] iv = hash.digest();
            final byte[] correctedIV = new byte[keyLength];
            System.arraycopy(iv, 0, correctedIV, 0, keyLength < key.length ? keyLength : key.length);
            attributes.put(IMode.IV, correctedIV);
            crypt.init(attributes);
            buffer = new byte[Long.SIZE / Byte.SIZE];
            encryptedRead(in, buffer, crypt);
            final long x = Convert.longFromBytes(buffer);
            encryptedRead(in, buffer, crypt);
            final long y = Convert.longFromBytes(buffer);
            encryptedRead(in, buffer, crypt);
            final long z = Convert.longFromBytes(buffer);
            if ((x ^ y) != z) throw new InvalidParameterException();
            byte[] singleByte = new byte[Byte.SIZE / Byte.SIZE];
            encryptedRead(in, singleByte, crypt);
            buffer = new byte[((short) Convert.byteFromBytes(singleByte) & 0x00FF)];
            encryptedRead(in, buffer, crypt);
            encryptedRead(in, singleByte, crypt);
            final byte tags = Convert.byteFromBytes(singleByte);
            byte[] doubleByte = new byte[Short.SIZE / Byte.SIZE];
            int blockSize = 0;
            for (int i = 0; i < tags; i++) {
                encryptedRead(in, singleByte, crypt);
                encryptedRead(in, doubleByte, crypt);
                final MetaData t = MetaData.getFromTagValue(Convert.byteFromBytes(singleByte));
                final short l = Convert.shortFromBytes(doubleByte);
                buffer = new byte[l];
                encryptedRead(in, buffer, crypt);
                switch(t) {
                    case SIZE:
                        decryptedSize = Convert.longFromBytes(buffer);
                        break;
                    case BLOCKED:
                        blockSize = (int) Convert.longFromBytes(buffer);
                        break;
                    default:
                        throw new UnsupportedEncodingException();
                }
            }
            out = new FileOutputStream(outputFile);
            hash.reset();
            if (blockSize > 0) {
                final byte[] booleanBytes = new byte[1];
                final byte[] longBytes = new byte[Long.SIZE / Byte.SIZE];
                boolean booleanByte = true;
                buffer = new byte[BLOCK_SIZE];
                while (booleanByte) {
                    if (interrupted()) throw new InterruptedException();
                    encryptedRead(in, booleanBytes, crypt);
                    booleanByte = Convert.booleanFromBytes(booleanBytes);
                    int r = BLOCK_SIZE;
                    encryptedRead(in, buffer, crypt);
                    if (!booleanByte) {
                        encryptedRead(in, longBytes, crypt);
                        r = (int) Convert.longFromBytes(longBytes);
                        byte[] tmp = new byte[r];
                        System.arraycopy(buffer, 0, tmp, 0, r);
                        buffer = new byte[r];
                        System.arraycopy(tmp, 0, buffer, 0, r);
                    }
                    hash.update(buffer, 0, r);
                    out.write(buffer);
                    bytesProcessed += r;
                }
            } else for (bytesProcessed = 0; bytesProcessed < decryptedSize; bytesProcessed += blockLength) {
                if (interrupted()) throw new InterruptedException();
                int j = blockLength;
                if (bytesProcessed + blockLength > decryptedSize) j = (int) (blockLength - (bytesProcessed + blockLength - decryptedSize));
                buffer = new byte[j];
                encryptedRead(in, buffer, crypt);
                hash.update(buffer, 0, j);
                out.write(buffer);
            }
            buffer = new byte[hash.hashSize()];
            encryptedRead(in, buffer, crypt);
            final byte[] digest = hash.digest();
            if (!Arrays.equals(buffer, digest)) throw new SignatureException();
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
            } catch (final Exception e) {
                ;
            }
        }
    }
