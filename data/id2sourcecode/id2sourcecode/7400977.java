    @Override
    public void channelData(Channel channel, byte[] data) {
        int off, w, x, y, z;
        byte[] ciphertext = new byte[data.length + 1024];
        byte[] keystream = new byte[16];
        for (int block = 0; block < data.length / 1024; block++) {
            off = block * 1024;
            w = block * 1024 + 0 * 256;
            x = block * 1024 + 1 * 256;
            y = block * 1024 + 2 * 256;
            z = block * 1024 + 3 * 256;
            for (int i = 0; i < 1024 && (block * 1024 + i) < data.length; i += 4) {
                ciphertext[off++] = data[w++];
                ciphertext[off++] = data[x++];
                ciphertext[off++] = data[y++];
                ciphertext[off++] = data[z++];
            }
            for (int i = 0; i < 1024 && (block * 1024 + i) < data.length; i += 16) {
                try {
                    keystream = this.cipher.doFinal(this.iv);
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < 16; j++) {
                    ciphertext[block * 1024 + i + j] ^= keystream[j] ^ this.iv[j];
                }
                for (int j = 15; j >= 0; j--) {
                    this.iv[j] += 1;
                    if ((this.iv[j] & 0xFF) != 0) {
                        break;
                    }
                }
                try {
                    this.cipher.init(Cipher.ENCRYPT_MODE, this.key, new IvParameterSpec(this.iv));
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            this.output.write(ciphertext, 0, ciphertext.length - 1024);
        } catch (IOException e) {
        }
    }
