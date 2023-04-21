    public void endPacket() throws IOException, IllegalStateException {
        Debug.debug2("Ending packet; raw_in=" + raw_in + " packet_length=" + packet_length + " padding_length=" + padding_length + " inflated_length=" + inflated_length);
        if (config.flater == null && total_in < packet_length - padding_length) {
            mark += packet_length - padding_length - total_in;
            Debug.debug2("Discarding " + (packet_length - padding_length - total_in));
        } else if (config.flater != null && total_in - 1 < inflated_length) {
            mark += inflated_length - total_in + 1;
            Debug.debug2("Discarding inflated=" + (inflated_length - total_in + 1));
        }
        byte[] padding = new byte[padding_length];
        int len = read(padding);
        Debug.debug2(len + " bytes of padding read");
        if (config.mac != null) {
            byte[] mac = readMac();
            Debug.debug2("MAC: " + Util.toString(mac));
            config.mac.update((byte) (sequence >>> 24 & 0xff));
            config.mac.update((byte) (sequence >>> 16 & 0xff));
            config.mac.update((byte) (sequence >>> 8 & 0xff));
            config.mac.update((byte) (sequence & 0xff));
            config.mac.update((byte) (packet_length >>> 24 & 0xff));
            config.mac.update((byte) (packet_length >>> 16 & 0xff));
            config.mac.update((byte) (packet_length >>> 8 & 0xff));
            config.mac.update((byte) (packet_length & 0xff));
            config.mac.update((byte) padding_length);
            if (config.flater == null) {
                byte[] b = getPayload();
                config.mac.update(b, 0, b.length);
            } else {
                byte[] b = compressed_payload.toByteArray();
                config.mac.update(b, 0, b.length);
                config.mac.update(padding, 0, padding.length);
            }
            byte[] my_mac = config.mac.digest();
            config.mac.reset();
            Debug.debug2("my MAC: " + Util.toString(my_mac));
            Debug.debug2("MAC: " + Util.toString(mac));
            if (!java.util.Arrays.equals(mac, my_mac)) {
                sequence++;
                payload.reset();
                if (config.flater != null) {
                    compressed_payload.reset();
                    inflated_length = 0;
                }
                packet_length = padding_length = -1;
                total_in = 0;
                mark = limit = 0;
                Debug.warning("MAC not validated.");
                throw new MACValidationException();
            }
            Debug.debug2("MAC validated: " + Util.toString(my_mac));
        }
        sequence++;
        payload.reset();
        if (config.flater != null) {
            compressed_payload.reset();
            inflated_length = 0;
        }
        packet_length = padding_length = -1;
        total_in = 0;
        mark = limit = 0;
    }
