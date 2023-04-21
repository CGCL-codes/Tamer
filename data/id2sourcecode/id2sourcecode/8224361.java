    public File EncodePiggybackRoute(PiggybackRouteNodes Nodes, File Data) throws EncodeFailedException {
        File outfile = null;
        try {
            CipherParametersBytes symkey = KM.GenSymKey();
            Nodes.EncodePacket = ByteBuffer.wrap(symkey.getBytes());
            File basefile = getTemp().createNewFile("encode", "pigroute");
            ChannelWriter cw = new ChannelWriter(basefile);
            for (int cnt = 0; cnt < Nodes.PrivateEncodeKeys.size(); cnt++) {
                Nodes.PrivateEncodeKeys.get(cnt).clear();
                cw.putByteBuffer(Nodes.PrivateEncodeKeys.get(cnt));
                Nodes.PrivateEncodeKeys.get(cnt).clear();
            }
            cw.putFile(Data);
            cw.close();
            File encodedfile = RawEncode(basefile, Nodes.EncodePacket);
            Header head = new Header(this);
            head.SymKey = symkey.getChannel();
            PiggybackRouteHeader nh = new PiggybackRouteHeader(this);
            nh.setHeader(head);
            nh.DecodeLength = encodedfile.length();
            nh.NumberPiggybackDecodes = Nodes.PrivateEncodeKeys.size();
            nh.RouteFileLength = Data.length();
            nh.Encode();
            outfile = getTemp().createNewFile("encode", "pigroute");
            cw = new ChannelWriter(outfile);
            cw.Write(nh);
            cw.putFile(encodedfile);
            cw.close();
            boolean ok = true;
            for (int cnt = 0; cnt < Nodes.getNodes().size() && ok; cnt++) {
                ok = false;
                RemoteNode n = Nodes.getNodes().get(cnt);
                ClientBio b = n.getClientID(getBio().getID());
                if (b != null) {
                    String keystr = b.getProp(KEYPROP);
                    if (keystr != null) {
                        byte[] keyb = Base64Codec.Decode(keystr);
                        if (keyb != null) {
                            BytesChannel cb = new BytesChannel(keyb);
                            ChannelReader r = new ChannelReader(cb);
                            CipherParametersChannel keychan = KM.getPublicKeyChannel();
                            r.Read(keychan);
                            Header h = new Header(this);
                            h.PublicKey = keychan;
                            h.Instruction = PIGGYBACK;
                            h.SymKey = symkey.getChannel();
                            nh.setHeader(h);
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
            if (!ok) {
                throw new EncodeFailedException("!ok");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncodeFailedException("failed");
        }
        return outfile;
    }
