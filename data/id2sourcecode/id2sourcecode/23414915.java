    @Override
    protected byte[] decrypt(byte type, byte[] fragment, int offset, int len) {
        byte[] data = decCipher.update(fragment, offset, len);
        byte[] content;
        if (is_block_cipher) {
            int padding_length = data[data.length - 1];
            for (int i = 0; i < padding_length; i++) {
                if (data[data.length - 2 - i] != padding_length) {
                    throw new AlertException(AlertProtocol.DECRYPTION_FAILED, new SSLProtocolException("Received message has bad padding"));
                }
            }
            content = new byte[data.length - hash_size - padding_length - 1];
        } else {
            content = new byte[data.length - hash_size];
        }
        byte[] mac_value;
        mac_material_part[0] = type;
        mac_material_part[1] = (byte) ((0x00FF00 & content.length) >> 8);
        mac_material_part[2] = (byte) (0x0000FF & content.length);
        messageDigest.update(mac_read_secret);
        messageDigest.update(pad_1);
        messageDigest.update(read_seq_num);
        messageDigest.update(mac_material_part);
        messageDigest.update(data, 0, content.length);
        mac_value = messageDigest.digest();
        messageDigest.update(mac_read_secret);
        messageDigest.update(pad_2);
        messageDigest.update(mac_value);
        mac_value = messageDigest.digest();
        if (logger != null) {
            logger.println("Decrypted:");
            logger.print(data);
            logger.println("Expected mac value:");
            logger.print(mac_value);
        }
        for (int i = 0; i < hash_size; i++) {
            if (mac_value[i] != data[i + content.length]) {
                throw new AlertException(AlertProtocol.BAD_RECORD_MAC, new SSLProtocolException("Bad record MAC"));
            }
        }
        System.arraycopy(data, 0, content, 0, content.length);
        incSequenceNumber(read_seq_num);
        return content;
    }
