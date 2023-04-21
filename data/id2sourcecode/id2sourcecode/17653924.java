    public byte[] toBinaryPacket() throws SSH2Exception {
        byte[] payload_bytes = payload.toByteArray();
        byte[] packet;
        int packet_length;
        int padding_length;
        byte[] padding = null;
        byte[] mac = null;
        if (config.flater != null) {
            ByteArrayOutputStream compressed = new ByteArrayOutputStream();
            byte[] buf = new byte[BUF_SIZE];
            config.flater.next_in = payload_bytes;
            config.flater.next_in_index = 0;
            config.flater.avail_in = payload_bytes.length;
            do {
                config.flater.next_out = buf;
                config.flater.next_out_index = 0;
                config.flater.avail_out = buf.length;
                int err = config.flater.deflate(JZlib.Z_PARTIAL_FLUSH);
                switch(err) {
                    case JZlib.Z_OK:
                        compressed.write(buf, 0, buf.length - config.flater.avail_out);
                        break;
                    default:
                        throw new SSH2Exception("compression failed (" + err + ") : " + config.flater.msg);
                }
            } while (config.flater.avail_out == 0);
            payload_bytes = compressed.toByteArray();
        }
        int s = 8;
        if (config.cipher != null) s = Math.max(8, config.cipher.currentBlockSize());
        padding_length = s * ((5 + payload_bytes.length) / s + 1) - (5 + payload_bytes.length);
        if (padding_length < s) padding_length += s;
        padding = new byte[padding_length];
        if (config.random != null && config.cipher != null) config.random.nextBytes(padding);
        packet_length = 1 + payload_bytes.length + padding.length;
        byte[] plain = new byte[packet_length + 4];
        plain[0] = (byte) ((packet_length >>> 24) & 0xff);
        plain[1] = (byte) ((packet_length >>> 16) & 0xff);
        plain[2] = (byte) ((packet_length >>> 8) & 0xff);
        plain[3] = (byte) (packet_length & 0xff);
        plain[4] = (byte) (padding.length & 0xff);
        System.arraycopy(payload_bytes, 0, plain, 5, payload_bytes.length);
        System.arraycopy(padding, 0, plain, 5 + payload_bytes.length, padding.length);
        if (config.mac != null) {
            byte[] seq = { (byte) ((sequence >>> 24) & 0xff), (byte) ((sequence >>> 16) & 0xff), (byte) ((sequence >>> 8) & 0xff), (byte) (sequence & 0xff) };
            config.mac.update(seq, 0, seq.length);
            config.mac.update(plain, 0, plain.length);
            mac = config.mac.digest();
            config.mac.reset();
        }
        byte[] encrypted = null;
        if (config.cipher != null) {
            final int bs = config.cipher.currentBlockSize();
            encrypted = new byte[plain.length];
            for (int i = 0; i < plain.length; i += bs) {
                config.cipher.update(plain, i, encrypted, i);
            }
        } else encrypted = plain;
        plain = null;
        packet = new byte[encrypted.length + ((mac == null) ? 0 : mac.length)];
        System.arraycopy(encrypted, 0, packet, 0, encrypted.length);
        if (mac != null) System.arraycopy(mac, 0, packet, encrypted.length, mac.length);
        encrypted = mac = null;
        sequence++;
        total_raw_out += packet.length;
        return packet;
    }
