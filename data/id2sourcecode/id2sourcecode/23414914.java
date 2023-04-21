    @Override
    protected byte[] encrypt(byte type, byte[] fragment, int offset, int len) {
        try {
            int content_mac_length = len + hash_size;
            int padding_length = is_block_cipher ? padding_length = ((8 - (++content_mac_length & 0x07)) & 0x07) : 0;
            byte[] res = new byte[content_mac_length + padding_length];
            System.arraycopy(fragment, offset, res, 0, len);
            mac_material_part[0] = type;
            mac_material_part[1] = (byte) ((0x00FF00 & len) >> 8);
            mac_material_part[2] = (byte) (0x0000FF & len);
            messageDigest.update(mac_write_secret);
            messageDigest.update(pad_1);
            messageDigest.update(write_seq_num);
            messageDigest.update(mac_material_part);
            messageDigest.update(fragment, offset, len);
            byte[] digest = messageDigest.digest();
            messageDigest.update(mac_write_secret);
            messageDigest.update(pad_2);
            messageDigest.update(digest);
            digest = messageDigest.digest();
            System.arraycopy(digest, 0, res, len, hash_size);
            if (is_block_cipher) {
                Arrays.fill(res, content_mac_length - 1, res.length, (byte) (padding_length));
            }
            if (logger != null) {
                logger.println("SSLRecordProtocol.encrypt: " + (is_block_cipher ? "GenericBlockCipher with padding[" + padding_length + "]:" : "GenericStreamCipher:"));
                logger.print(res);
            }
            byte[] rez = new byte[encCipher.getOutputSize(res.length)];
            encCipher.update(res, 0, res.length, rez);
            incSequenceNumber(write_seq_num);
            return rez;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new AlertException(AlertProtocol.INTERNAL_ERROR, new SSLProtocolException("Error during the encryption"));
        }
    }
