    private byte[] computeH() {
        String V_C = transport.clientId;
        String V_S = transport.serverId;
        if (V_C.endsWith("\n") || V_C.endsWith("\r")) {
            V_C = V_C.substring(0, V_C.length() - 1);
        }
        if (V_C.endsWith("\n") || V_C.endsWith("\r")) {
            V_C = V_C.substring(0, V_C.length() - 1);
        }
        if (V_S.endsWith("\n") || V_S.endsWith("\r")) {
            V_S = V_S.substring(0, V_S.length() - 1);
        }
        if (V_S.endsWith("\n") || V_S.endsWith("\r")) {
            V_S = V_S.substring(0, V_S.length() - 1);
        }
        byte[] V_C_s = SshMisc.createString(V_C);
        byte[] V_S_s = SshMisc.createString(V_S);
        byte[] I_C_s = SshMisc.createString(I_C);
        byte[] I_S_s = SshMisc.createString(I_S);
        byte[] K_S_s = SshMisc.createMpInt(serverHostKey.getEncoded());
        byte[] e_mpint = SshMisc.createMpInt(e);
        byte[] f_mpint = SshMisc.createMpInt(f);
        byte[] K_mpint = SshMisc.createMpInt(sharedSecretK.getEncoded());
        byte[] stuffToHash = new byte[V_C_s.length + V_S_s.length + I_C_s.length + I_S_s.length + K_S_s.length + e_mpint.length + f_mpint.length + K_mpint.length];
        int offset = 0;
        for (int i = 0; i < V_C_s.length; i++) stuffToHash[offset++] = V_C_s[i];
        for (int i = 0; i < V_S_s.length; i++) stuffToHash[offset++] = V_S_s[i];
        for (int i = 0; i < I_C_s.length; i++) stuffToHash[offset++] = I_C_s[i];
        for (int i = 0; i < I_S_s.length; i++) stuffToHash[offset++] = I_S_s[i];
        for (int i = 0; i < K_S_s.length; i++) stuffToHash[offset++] = K_S_s[i];
        for (int i = 0; i < e_mpint.length; i++) stuffToHash[offset++] = e_mpint[i];
        for (int i = 0; i < f_mpint.length; i++) stuffToHash[offset++] = f_mpint[i];
        for (int i = 0; i < K_mpint.length; i++) stuffToHash[offset++] = K_mpint[i];
        hasher.update(stuffToHash, 0, stuffToHash.length);
        return hasher.digest();
    }
