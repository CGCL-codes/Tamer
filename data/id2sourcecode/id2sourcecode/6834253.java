    private void updateKeys(KeyExchange kex) throws Exception {
        byte[] K = kex.getK();
        byte[] H = kex.getH();
        HASH hash = kex.getHash();
        if (session_id == null) {
            session_id = new byte[H.length];
            System.arraycopy(H, 0, session_id, 0, H.length);
        }
        buf.reset();
        buf.putMPInt(K);
        buf.putByte(H);
        buf.putByte((byte) 0x41);
        buf.putByte(session_id);
        hash.update(buf.buffer, 0, buf.index);
        IVc2s = hash.digest();
        int j = buf.index - session_id.length - 1;
        buf.buffer[j]++;
        hash.update(buf.buffer, 0, buf.index);
        IVs2c = hash.digest();
        buf.buffer[j]++;
        hash.update(buf.buffer, 0, buf.index);
        Ec2s = hash.digest();
        buf.buffer[j]++;
        hash.update(buf.buffer, 0, buf.index);
        Es2c = hash.digest();
        buf.buffer[j]++;
        hash.update(buf.buffer, 0, buf.index);
        MACc2s = hash.digest();
        buf.buffer[j]++;
        hash.update(buf.buffer, 0, buf.index);
        MACs2c = hash.digest();
        try {
            Class c;
            String method;
            method = guess[KeyExchange.PROPOSAL_ENC_ALGS_STOC];
            c = Class.forName(getConfig(method));
            s2ccipher = (Cipher) (c.newInstance());
            while (s2ccipher.getBlockSize() > Es2c.length) {
                buf.reset();
                buf.putMPInt(K);
                buf.putByte(H);
                buf.putByte(Es2c);
                hash.update(buf.buffer, 0, buf.index);
                byte[] foo = hash.digest();
                byte[] bar = new byte[Es2c.length + foo.length];
                System.arraycopy(Es2c, 0, bar, 0, Es2c.length);
                System.arraycopy(foo, 0, bar, Es2c.length, foo.length);
                Es2c = bar;
            }
            s2ccipher.init(Cipher.DECRYPT_MODE, Es2c, IVs2c);
            s2ccipher_size = s2ccipher.getIVSize();
            method = guess[KeyExchange.PROPOSAL_MAC_ALGS_STOC];
            c = Class.forName(getConfig(method));
            s2cmac = (MAC) (c.newInstance());
            s2cmac.init(MACs2c);
            s2cmac_result1 = new byte[s2cmac.getBlockSize()];
            s2cmac_result2 = new byte[s2cmac.getBlockSize()];
            method = guess[KeyExchange.PROPOSAL_ENC_ALGS_CTOS];
            c = Class.forName(getConfig(method));
            c2scipher = (Cipher) (c.newInstance());
            while (c2scipher.getBlockSize() > Ec2s.length) {
                buf.reset();
                buf.putMPInt(K);
                buf.putByte(H);
                buf.putByte(Ec2s);
                hash.update(buf.buffer, 0, buf.index);
                byte[] foo = hash.digest();
                byte[] bar = new byte[Ec2s.length + foo.length];
                System.arraycopy(Ec2s, 0, bar, 0, Ec2s.length);
                System.arraycopy(foo, 0, bar, Ec2s.length, foo.length);
                Ec2s = bar;
            }
            c2scipher.init(Cipher.ENCRYPT_MODE, Ec2s, IVc2s);
            c2scipher_size = c2scipher.getIVSize();
            method = guess[KeyExchange.PROPOSAL_MAC_ALGS_CTOS];
            c = Class.forName(getConfig(method));
            c2smac = (MAC) (c.newInstance());
            c2smac.init(MACc2s);
            method = guess[KeyExchange.PROPOSAL_COMP_ALGS_CTOS];
            initDeflater(method);
            method = guess[KeyExchange.PROPOSAL_COMP_ALGS_STOC];
            initInflater(method);
        } catch (Exception e) {
            if (e instanceof JSchException) throw e;
            throw new JSchException(e.toString(), e);
        }
    }
