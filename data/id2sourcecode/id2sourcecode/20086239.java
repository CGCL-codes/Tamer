    private WoWpacket readPacketAuth() {
        WoWpacket pkt = readPacketRaw();
        if (pkt == null || pkt.code() != WoWpacket.SMSG_AUTH_CHALLENGE) return pkt;
        System.err.println("Got auth challenge packet");
        if (m_account == null || m_session == null || m_build == 0 || pkt.data() == null || pkt.data().length < 8) return null;
        try {
            Sha160 h = new Sha160();
            byte[] unk = new byte[4];
            unk[0] = unk[1] = unk[2] = unk[3] = 0;
            Random rnd = new Random();
            byte[] seed = new byte[4];
            for (int i = 0; i < 4; i++) seed[i] = (byte) rnd.nextInt(256);
            byte[] acc = m_account.getBytes("UTF-8");
            h.update(acc);
            h.update(unk);
            h.update(seed);
            h.update(pkt.data(), 4, 4);
            h.update(m_session);
            byte auth[] = new byte[4];
            auth[0] = (byte) m_build;
            auth[1] = (byte) (m_build >> 8);
            auth[2] = (byte) (m_build >> 16);
            auth[3] = (byte) (m_build >> 24);
            auth = append(auth, unk);
            auth = append(auth, acc);
            auth = append(auth, new byte[1]);
            auth = append(auth, unk);
            auth = append(auth, seed);
            auth = append(auth, unk);
            auth = append(auth, unk);
            auth = append(auth, unk);
            auth = append(auth, unk);
            auth = append(auth, unk);
            auth = append(auth, h.digest());
            auth = append(auth, unk);
            if (!writePacket(WoWpacket.CMSG_AUTH_SESSION, auth)) return null;
            startCrypt(m_session);
            return pkt;
        } catch (Exception e) {
            errorStr = e.getMessage();
        }
        return null;
    }
