    Fingerprint(final FingerprintFunction fingerprint_function, final NetworkTransmitter transmitter, final List<Landmark> landmarks, final double[] rssi_values, final int[] count, final long time_start, final long time_end) {
        if (fingerprint_function == null) throw new NullPointerException("Cannot make a fingerprint from a null function.");
        this.fingerprint_function = fingerprint_function;
        this.transmitter = transmitter;
        this.rssi_values = rssi_values;
        this.count = count;
        this.time_start = time_start;
        this.time_end = time_end;
        this.landmarks = landmarks;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            throw new NullPointerException(nsae.getMessage());
        }
        digest.update(this.transmitter.mac_address.toByteArray());
        digest.update(new Integer(this.transmitter.physical_layer_type).byteValue());
        digest.update(new Integer(this.fingerprint_function.id).byteValue());
        byte[] t_start = new byte[8];
        byte[] t_end = new byte[8];
        for (int i = 0; i < 7; i++) {
            t_start[i] = (byte) (this.time_start << 8 * i);
            t_end[i] = (byte) (this.time_end << 8 * i);
        }
        digest.update(t_start);
        digest.update(t_end);
        for (Landmark landmark : this.landmarks) {
            digest.update((byte) ((int) this.getRSSI(landmark)));
        }
        byte[] hash_code = digest.digest();
        long hash_long = 0l;
        for (int i = 0; i < 16; i++) {
            hash_long ^= hash_code[i] << 8 * (i % 8);
        }
        this.hashcode = hash_long;
    }
