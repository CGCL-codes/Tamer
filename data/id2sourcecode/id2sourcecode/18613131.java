    public void Generate() {
        try {
            CipherParametersBytes symkey = C.KM.GenSymKey();
            Header head = new Header(C);
            head.SymKey = symkey.getChannel();
            NormalHeader nh = new NormalHeader(C);
            RemoteNode rn = Nodes.getNodes().get(0);
            nh.setServerString(rn.getRegServer());
            C.setServerTimeIndex(nh);
            nh.setHeader(head);
            nh.Encode();
            WriteBytesChannel wbc = new WriteBytesChannel();
            ChannelWriter cw = new ChannelWriter(wbc);
            cw.putIntLenBytes(symkey.getBytes());
            cw.putIntLenBytes(nh.EncodedHeader);
            cw.close();
            Nodes.EncodePacket = ByteBuffer.wrap(wbc.getBytes());
            boolean ok = true;
            for (int cnt = 0; cnt < Nodes.getNodes().size() && ok; cnt++) {
                ok = false;
                RemoteNode n = Nodes.getNodes().get(cnt);
                ClientBio b = n.getClientID(C.getBio().getID());
                if (b != null) {
                    String keystr = b.getProp("Key");
                    if (keystr != null) {
                        byte[] keyb = Base64Codec.Decode(keystr);
                        if (keyb != null) {
                            BytesChannel cb = new BytesChannel(keyb);
                            ChannelReader r = new ChannelReader(cb);
                            CipherParametersChannel keychan = C.KM.getPublicKeyChannel();
                            r.Read(keychan);
                            Header h = new Header(C);
                            h.PublicKey = keychan;
                            h.Instruction = CoreCrypto.NORMAL;
                            h.SymKey = symkey.getChannel();
                            nh.setHeader(h);
                            if (nh.GenCollision()) {
                                nh.UpdateHeader();
                                h.Encode();
                                WriteBytesChannel wcb = new WriteBytesChannel();
                                cw = new ChannelWriter(wcb);
                                cw.Write(h);
                                cw.close();
                                Nodes.BaseEncodeHeaders.add(wcb.getByteBuffer());
                                ok = true;
                            }
                        }
                    }
                }
            }
            if (ok) {
                Nodes.EncodePacketDone = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
