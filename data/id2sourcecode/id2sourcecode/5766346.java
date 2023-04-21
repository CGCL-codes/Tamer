    Sample(final long[] time_interval, final MACAddress mac_address, final int phy, final InetAddress net_address, final double rssi, final int sequence_number, final int frame_ctrl, final int QoS_priority, Landmark landmark, final double[] rssi_histogram_values, final int[] rssi_histogram_count, final byte[] header, final int protocol_byte_length) {
        this.time_created = System.currentTimeMillis();
        this.protocol_byte_length = protocol_byte_length;
        this.time_interval = time_interval;
        this.net_address = net_address;
        this.mac_address = mac_address;
        this.rssi = rssi;
        this.sequence_number = sequence_number;
        this.frame_ctrl = frame_ctrl;
        this.QoS_Priority = QoS_priority;
        this.landmark = landmark;
        this.noise = 0;
        this.header = header;
        this.rssi_histogram_count = rssi_histogram_count;
        this.rssi_histogram_values = rssi_histogram_values;
        this.time_stamp = time_interval[0] + ((time_interval[1] - time_interval[0]) / 2);
        if (Sample.digest != null) {
            byte[] mac_bytes = this.mac_address.toByteArray();
            byte[] hashcode_bytes;
            synchronized (Sample.digest) {
                digest.update(mac_bytes);
                for (int i = 0; i < 8; i++) {
                    digest.update((byte) (this.time_created >> 8 * i));
                    digest.update((byte) (this.time_stamp >> 8 * i));
                }
                digest.update((byte) phy);
                digest.update((byte) landmark.antenna);
                digest.update((byte) landmark.mode);
                digest.update((byte) this.sequence_number);
                digest.update((byte) this.QoS_Priority);
                digest.update((byte) Double.doubleToLongBits(this.rssi));
                if (this.header != null && this.header.length > 0) digest.update(this.header);
                hashcode_bytes = digest.digest();
            }
            long temp_hash = 0l;
            for (int i = 0; i < 8; i++) {
                temp_hash ^= ((hashcode_bytes[2 * i] ^ hashcode_bytes[2 * i + 1]) << 8 * i);
            }
            this.hash_code = temp_hash;
        } else {
            int hi = (int) (this.time_stamp >> 32) ^ (int) (this.mac_address.toLong() >> 32);
            int lo = (int) this.time_stamp ^ (int) this.mac_address.toLong();
            this.hash_code = (long) hi << 32 + lo;
        }
        this.phy = phy;
        if (this.header != null && this.header.length > 145) {
            byte[] temp_bytes = new byte[this.header.length - 145];
            System.arraycopy(this.header, 145, temp_bytes, 0, temp_bytes.length);
            synchronized (Sample.digest) {
                this.header_hash = digest.digest(temp_bytes);
            }
        } else this.header_hash = null;
    }
