    public boolean next(Buffer _buf) throws Exception {
        int i, j;
        switch(state) {
            case SSH_MSG_KEX_DH_GEX_GROUP:
                _buf.getInt();
                _buf.getByte();
                j = _buf.getByte();
                if (j != SSH_MSG_KEX_DH_GEX_GROUP) {
                    System.err.println("type: must be SSH_MSG_KEX_DH_GEX_GROUP " + j);
                    return false;
                }
                p = _buf.getMPInt();
                g = _buf.getMPInt();
                dh.setP(p);
                dh.setG(g);
                e = dh.getE();
                packet.reset();
                buf.putByte((byte) SSH_MSG_KEX_DH_GEX_INIT);
                buf.putMPInt(e);
                session.write(packet);
                if (JSch.getLogger().isEnabled(Logger.INFO)) {
                    JSch.getLogger().log(Logger.INFO, "SSH_MSG_KEX_DH_GEX_INIT sent");
                    JSch.getLogger().log(Logger.INFO, "expecting SSH_MSG_KEX_DH_GEX_REPLY");
                }
                state = SSH_MSG_KEX_DH_GEX_REPLY;
                return true;
            case SSH_MSG_KEX_DH_GEX_REPLY:
                j = _buf.getInt();
                j = _buf.getByte();
                j = _buf.getByte();
                if (j != SSH_MSG_KEX_DH_GEX_REPLY) {
                    System.err.println("type: must be SSH_MSG_KEX_DH_GEX_REPLY " + j);
                    return false;
                }
                K_S = _buf.getString();
                byte[] f = _buf.getMPInt();
                byte[] sig_of_H = _buf.getString();
                dh.setF(f);
                K = dh.getK();
                buf.reset();
                buf.putString(V_C);
                buf.putString(V_S);
                buf.putString(I_C);
                buf.putString(I_S);
                buf.putString(K_S);
                buf.putInt(min);
                buf.putInt(preferred);
                buf.putInt(max);
                buf.putMPInt(p);
                buf.putMPInt(g);
                buf.putMPInt(e);
                buf.putMPInt(f);
                buf.putMPInt(K);
                byte[] foo = new byte[buf.getLength()];
                buf.getByte(foo);
                sha.update(foo, 0, foo.length);
                H = sha.digest();
                i = 0;
                j = 0;
                j = ((K_S[i++] << 24) & 0xff000000) | ((K_S[i++] << 16) & 0x00ff0000) | ((K_S[i++] << 8) & 0x0000ff00) | ((K_S[i++]) & 0x000000ff);
                String alg = new String(K_S, i, j);
                i += j;
                boolean result = false;
                if (alg.equals("ssh-rsa")) {
                    byte[] tmp;
                    byte[] ee;
                    byte[] n;
                    type = RSA;
                    j = ((K_S[i++] << 24) & 0xff000000) | ((K_S[i++] << 16) & 0x00ff0000) | ((K_S[i++] << 8) & 0x0000ff00) | ((K_S[i++]) & 0x000000ff);
                    tmp = new byte[j];
                    System.arraycopy(K_S, i, tmp, 0, j);
                    i += j;
                    ee = tmp;
                    j = ((K_S[i++] << 24) & 0xff000000) | ((K_S[i++] << 16) & 0x00ff0000) | ((K_S[i++] << 8) & 0x0000ff00) | ((K_S[i++]) & 0x000000ff);
                    tmp = new byte[j];
                    System.arraycopy(K_S, i, tmp, 0, j);
                    i += j;
                    n = tmp;
                    SignatureRSA sig = null;
                    try {
                        Class c = Class.forName(session.getConfig("signature.rsa"));
                        sig = (SignatureRSA) (c.newInstance());
                        sig.init();
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    sig.setPubKey(ee, n);
                    sig.update(H);
                    result = sig.verify(sig_of_H);
                    if (JSch.getLogger().isEnabled(Logger.INFO)) {
                        JSch.getLogger().log(Logger.INFO, "ssh_rsa_verify: signature " + result);
                    }
                } else if (alg.equals("ssh-dss")) {
                    byte[] q = null;
                    byte[] tmp;
                    type = DSS;
                    j = ((K_S[i++] << 24) & 0xff000000) | ((K_S[i++] << 16) & 0x00ff0000) | ((K_S[i++] << 8) & 0x0000ff00) | ((K_S[i++]) & 0x000000ff);
                    tmp = new byte[j];
                    System.arraycopy(K_S, i, tmp, 0, j);
                    i += j;
                    p = tmp;
                    j = ((K_S[i++] << 24) & 0xff000000) | ((K_S[i++] << 16) & 0x00ff0000) | ((K_S[i++] << 8) & 0x0000ff00) | ((K_S[i++]) & 0x000000ff);
                    tmp = new byte[j];
                    System.arraycopy(K_S, i, tmp, 0, j);
                    i += j;
                    q = tmp;
                    j = ((K_S[i++] << 24) & 0xff000000) | ((K_S[i++] << 16) & 0x00ff0000) | ((K_S[i++] << 8) & 0x0000ff00) | ((K_S[i++]) & 0x000000ff);
                    tmp = new byte[j];
                    System.arraycopy(K_S, i, tmp, 0, j);
                    i += j;
                    g = tmp;
                    j = ((K_S[i++] << 24) & 0xff000000) | ((K_S[i++] << 16) & 0x00ff0000) | ((K_S[i++] << 8) & 0x0000ff00) | ((K_S[i++]) & 0x000000ff);
                    tmp = new byte[j];
                    System.arraycopy(K_S, i, tmp, 0, j);
                    i += j;
                    f = tmp;
                    SignatureDSA sig = null;
                    try {
                        Class c = Class.forName(session.getConfig("signature.dss"));
                        sig = (SignatureDSA) (c.newInstance());
                        sig.init();
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    sig.setPubKey(f, p, q, g);
                    sig.update(H);
                    result = sig.verify(sig_of_H);
                    if (JSch.getLogger().isEnabled(Logger.INFO)) {
                        JSch.getLogger().log(Logger.INFO, "ssh_dss_verify: signature " + result);
                    }
                } else {
                    System.err.println("unknown alg");
                }
                state = STATE_END;
                return result;
        }
        return false;
    }
