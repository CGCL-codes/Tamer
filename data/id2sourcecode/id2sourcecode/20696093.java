    void receiveKeyExchangeDH() throws IOException {
        BigInteger dss_p = null, dss_q = null, dss_g = null, dss_y = null;
        BigInteger rsa_e = null, rsa_n = null;
        PacketOutputStream ks_data = new PacketOutputStream(client);
        int ks_len = (int) pin.readUInt32();
        host_key_algs = pin.readASCII();
        ks_data.writeASCII(host_key_algs);
        if (host_key_algs.equals("ssh-dss")) {
            dss_p = pin.readMPint();
            dss_q = pin.readMPint();
            dss_g = pin.readMPint();
            dss_y = pin.readMPint();
            ks_data.writeMPint(dss_p);
            ks_data.writeMPint(dss_q);
            ks_data.writeMPint(dss_g);
            ks_data.writeMPint(dss_y);
        } else if (host_key_algs.equals("ssh-rsa")) {
            rsa_e = pin.readMPint();
            rsa_n = pin.readMPint();
            ks_data.writeMPint(rsa_e);
            ks_data.writeMPint(rsa_n);
        } else {
            throw new SSH2Exception("Expecting host key algorithm name but got " + host_key_algs);
        }
        BigInteger host_pub_bi = pin.readMPint();
        byte[] host_sig = null;
        pin.readUInt32();
        host_key_algs = pin.readASCII();
        host_sig = pin.readString();
        K_S = ks_data.getPayload();
        pin.endPacket();
        Debug.debug2("got KEXDH_REPLY");
        try {
            OutgoingMessage outm = new OutgoingMessage();
            outm.writeMPI(host_pub_bi);
            IncomingMessage inm = new IncomingMessage(outm.toByteArray());
            dh_kex.processMessage(inm);
            shared_secret = new BigInteger(1, dh_kex.getSharedSecret());
        } catch (KeyAgreementException kae) {
            pout.reset();
            pout.write(SSH_MSG_DISCONNECT);
            pout.writeUInt32(SSH_DISCONNECT_KEY_EXCHANGE_FAILED);
            pout.writeUTF8(kae.getMessage());
            pout.writeASCII("en-US");
            out.write(pout.toBinaryPacket());
            out.flush();
            throw new SSH2Exception("error generating shared key: " + kae.getMessage());
        }
        hash = HashFactory.getInstance(Registry.SHA160_HASH);
        PacketOutputStream hash_data = new PacketOutputStream(new Configuration());
        hash_data.writeASCII(client_version);
        hash_data.writeASCII(server_version);
        hash_data.writeString(my_kex_payload);
        hash_data.writeString(server_kex_payload);
        hash_data.writeString(K_S);
        hash_data.writeMPint(dh_y);
        hash_data.writeMPint(host_pub_bi);
        hash_data.writeMPint(shared_secret);
        byte[] buf = hash_data.getPayload();
        hash.update(buf, 0, buf.length);
        hash_data = null;
        digest = hash.digest();
        Debug.debug2("verify digest=" + Util.toString(digest));
        if (server.session_id == null) server.session_id = (byte[]) digest.clone();
        if (client.session_id == null) client.session_id = (byte[]) digest.clone();
        Object host_sig_value = null;
        ISignature sig = null;
        if (host_key_algs.intern() == "ssh-dss") {
            sig = SignatureFactory.getInstance(Registry.DSS_SIG);
            server_pubkey = new DSSPublicKey(dss_p, dss_q, dss_g, dss_y);
            host_sig_value = dssBlobToGNU(host_sig);
            Debug.debug2("dss-p=" + dss_p);
            Debug.debug2("dss-q=" + dss_q);
            Debug.debug2("dss-g=" + dss_g);
            Debug.debug2("dss-y=" + dss_y);
        } else {
            sig = SignatureFactory.getInstance(Registry.RSA_PKCS1_V1_5_SIG);
            server_pubkey = new GnuRSAPublicKey(rsa_n, rsa_e);
            host_sig_value = host_sig;
        }
        sig.setupVerify(Collections.singletonMap(ISignature.VERIFIER_KEY, server_pubkey));
        sig.update(digest, 0, digest.length);
        sig_result = sig.verify(host_sig_value);
        Debug.debug("Signature alg=" + host_key_algs);
        Debug.debug("       result=" + sig_result);
    }
