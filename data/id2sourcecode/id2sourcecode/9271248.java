    public IPPacket processPacket(IPPacket packet, int direction) {
        long now = 0;
        try {
            now = System.nanoTime();
        } catch (Exception e) {
        }
        if (direction == DIRECTION_IN) {
            int offset = packet.getIPHeaderByteLength();
            int len = packet.getIPPacketLength() - offset;
            byte[] esp = packet.getDataUnprotected();
            int spi = (int) (((esp[offset] & 0xFF) << 24) | ((esp[offset + 1] & 0xFF) << 16) | ((esp[offset + 2] & 0xFF) << 8) | ((esp[offset + 3] & 0xFF)));
            BeetEntry entry = __inbound.get(String.valueOf(spi));
            if (entry == null) return null;
            byte[] iv = new byte[entry.getCipher().getIVLength()];
            int elen = len - ICV_LENGTH - iv.length - ESP_HEADER_LEN;
            int alen = ICV_LENGTH;
            System.arraycopy(esp, offset + ESP_HEADER_LEN, iv, 0, iv.length);
            byte[] cipher = new byte[elen];
            byte[] hash = new byte[ICV_LENGTH];
            byte[] hash2 = new byte[ICV_LENGTH];
            byte[] ivcipher = new byte[iv.length + elen + ESP_HEADER_LEN];
            System.arraycopy(esp, offset + ESP_HEADER_LEN + iv.length, cipher, 0, elen);
            System.arraycopy(esp, offset + ESP_HEADER_LEN + iv.length + cipher.length, hash, 0, alen);
            System.arraycopy(esp, offset, ivcipher, 0, ivcipher.length);
            System.arraycopy(entry.getDigest().digest(ivcipher), 0, hash2, 0, ICV_LENGTH);
            if (!Arrays.areEqual(hash, hash2)) {
                System.out.println("auth err: ICV does not match");
                return null;
            }
            byte[] plain = entry.getCipher().decrypt(cipher, iv);
            int padlen = (plain[plain.length - 2] & 0xFF);
            int proto = (plain[plain.length - 1] & 0xFF);
            IPPacket ipv6 = new IPPacket(IPPacket.IP_VERSION6_LEN + (elen - padlen - ESP_TRAILER_LEN));
            ipv6.setIPVersion(IPPacket.IP_VERSION6);
            ipv6.setNextHeader(proto);
            ipv6.setIPPacketLength((elen - padlen - ESP_TRAILER_LEN));
            ipv6.setDestination(entry.getSrc().getAddress());
            ipv6.setSource(entry.getDst().getAddress());
            byte[] ipv6raw = ipv6.getDataUnprotected();
            System.arraycopy(plain, 0, ipv6raw, IPPacket.IP_VERSION6_LEN, (elen - padlen - ESP_TRAILER_LEN));
            return ipv6;
        } else if (direction == DIRECTION_OUT) {
            byte[] dst6 = new byte[IPPacket.LENGTH_DESTINATION_ADDRESS6];
            packet.getDestination(dst6);
            BeetEntry entry = __outbound.get(Helpers.toHexString(dst6, ""));
            int elen = packet.getIPPacketLength();
            int padlen = 0;
            int eivlength = entry.getCipher().getIVLength();
            byte[] raw = packet.getDataUnprotected();
            if (eivlength > 0) {
                padlen = eivlength - ((elen + 2) % eivlength);
            } else {
                padlen = 4 - ((elen + 2) % 4);
            }
            byte[] esp = new byte[ESP_HEADER_LEN + elen + eivlength + padlen + ESP_TRAILER_LEN];
            esp[0] = (byte) ((entry.getSPI() >> 24) & 0xFF);
            esp[1] = (byte) ((entry.getSPI() >> 16) & 0xFF);
            esp[2] = (byte) ((entry.getSPI() >> 8) & 0xFF);
            esp[3] = (byte) (entry.getSPI() & 0xFF);
            esp[4] = (byte) ((entry.getSequence() >> 24) & 0xFF);
            esp[5] = (byte) ((entry.getSequence() >> 16) & 0xFF);
            esp[6] = (byte) ((entry.getSequence() >> 8) & 0xFF);
            esp[7] = (byte) (entry.getSequence() & 0xFF);
            entry.setSequence(entry.getSequence() + 1);
            byte[] edat = new byte[elen + padlen + ESP_TRAILER_LEN];
            System.arraycopy(raw, packet.getIPHeaderByteLength(), edat, 0, elen);
            edat[elen + padlen] = (byte) (padlen & 0xFF);
            edat[elen + padlen + 1] = (byte) (packet.getNextHeader() & 0xFF);
            byte[] enc = entry.getCipher().encrypt(edat);
            if (eivlength > 0) System.arraycopy(entry.getCipher().getIV(), 0, esp, ESP_HEADER_LEN, eivlength);
            System.arraycopy(enc, 0, esp, ESP_HEADER_LEN + eivlength, enc.length);
            byte[] adat = entry.getDigest().digest(esp);
            IPPacket espPacket = new IPPacket((entry.getDst() instanceof Inet4Address ? DEFAULT_IPV4_HEADER_LEN : DEFAULT_IPV6_HEADER_LEN) + esp.length + ICV_LENGTH);
            if (entry.getDst() instanceof Inet4Address) {
                espPacket.setSource(entry.getSrc().getAddress());
                espPacket.setDestination(entry.getDst().getAddress());
                espPacket.setIPVersion(IPPacket.IP_VERSION4);
                espPacket.setIPHeaderLength(DEFAULT_IPV4_HEADER_LEN / 4);
                espPacket.setTTL(DEFAULT_TTL);
                espPacket.setProtocol(ESP_PROTO);
            } else {
            }
            System.arraycopy(esp, 0, espPacket.getDataUnprotected(), espPacket.getIPHeaderByteLength(), esp.length);
            System.arraycopy(adat, 0, espPacket.getDataUnprotected(), espPacket.getIPHeaderByteLength() + esp.length, ICV_LENGTH);
            return espPacket;
        }
        return null;
    }
