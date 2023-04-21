    @Override
    public void channelData(Channel channel, byte[] data) {
        int off, w, x, y, z;
        for (int i = 0; i < data.length; i++) {
            this.cacheData[this.channelTotal + i] = data[i];
        }
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
            off = 0;
            if (this.header == null) {
                byte[] bytes = Arrays.copyOfRange(ciphertext, 0, 167);
                this.header = SpotifyOggHeader.decode(bytes);
                System.out.format("Header: 0x%08x\n", (this.header.getBytes() & ~4095) - 167);
                this.exchange.sendResponseHeaders(200, (this.header.getBytes() & ~4095) - 167);
                off = 167;
            }
            this.output.write(ciphertext, off, data.length - off);
            this.output.flush();
            this.channelTotal += data.length;
            this.total += data.length;
        } catch (Exception e) {
        }
    }
