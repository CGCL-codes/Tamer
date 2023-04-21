    void setupAlgorithms() throws SSH2Exception, IllegalStateException {
        byte[] shared_mpint = new byte[shared_secret.toByteArray().length + 4];
        shared_mpint[0] = (byte) ((shared_secret.toByteArray().length >>> 24) & 0xff);
        shared_mpint[1] = (byte) ((shared_secret.toByteArray().length >>> 16) & 0xff);
        shared_mpint[2] = (byte) ((shared_secret.toByteArray().length >>> 8) & 0xff);
        shared_mpint[3] = (byte) (shared_secret.toByteArray().length & 0xff);
        System.arraycopy(shared_secret.toByteArray(), 0, shared_mpint, 4, shared_secret.toByteArray().length);
        byte x = (byte) 'A';
        hash.reset();
        hash.update(shared_mpint, 0, shared_mpint.length);
        hash.update(digest, 0, digest.length);
        hash.update(x);
        hash.update(client.session_id, 0, client.session_id.length);
        byte[] iv_client = trimOrExpand(hash.digest(), client.CIPHER_IV_LENGTHS, client_cipher, hash, shared_mpint, x++, digest);
        hash.reset();
        hash.update(shared_mpint, 0, shared_mpint.length);
        hash.update(digest, 0, digest.length);
        hash.update(x);
        hash.update(client.session_id, 0, client.session_id.length);
        byte[] iv_server = trimOrExpand(hash.digest(), server.CIPHER_IV_LENGTHS, server_cipher, hash, shared_mpint, x++, digest);
        hash.reset();
        hash.update(shared_mpint, 0, shared_mpint.length);
        hash.update(digest, 0, digest.length);
        hash.update(x);
        hash.update(client.session_id, 0, client.session_id.length);
        byte[] ckey_client = trimOrExpand(hash.digest(), client.CIPHER_KEY_LENGTHS, server_cipher, hash, shared_mpint, x++, digest);
        hash.reset();
        hash.update(shared_mpint, 0, shared_mpint.length);
        hash.update(digest, 0, digest.length);
        hash.update(x);
        hash.update(client.session_id, 0, client.session_id.length);
        byte[] ckey_server = trimOrExpand(hash.digest(), server.CIPHER_KEY_LENGTHS, server_cipher, hash, shared_mpint, x++, digest);
        hash.reset();
        hash.update(shared_mpint, 0, shared_mpint.length);
        hash.update(digest, 0, digest.length);
        hash.update(x);
        hash.update(client.session_id, 0, client.session_id.length);
        byte[] mkey_client = trimOrExpand(hash.digest(), client.MAC_KEY_LENGTHS, client_mac, hash, shared_mpint, x++, digest);
        hash.reset();
        hash.update(shared_mpint, 0, shared_mpint.length);
        hash.update(digest, 0, digest.length);
        hash.update(x);
        hash.update(client.session_id, 0, client.session_id.length);
        byte[] mkey_server = trimOrExpand(hash.digest(), server.MAC_KEY_LENGTHS, server_mac, hash, shared_mpint, x++, digest);
        chooseCipher(client, iv_client, ckey_client, client_cipher, IMode.ENCRYPTION);
        chooseCipher(server, iv_server, ckey_server, server_cipher, IMode.DECRYPTION);
        chooseMac(client, mkey_client, client_mac);
        chooseMac(server, mkey_server, server_mac);
        if (client_comp.equals("zlib")) {
            client.flater = new ZStream();
            client.flater.deflateInit(JZlib.Z_DEFAULT_COMPRESSION);
        }
        if (server_comp.equals("zlib")) {
            server.flater = new ZStream();
            server.flater.inflateInit();
        }
        i_sent_kexinit = false;
        kex_timestamp = System.currentTimeMillis();
        pin.resetTotalBytesIn();
        pout.resetTotalBytesOut();
        Debug.debug2("KEX done.");
    }
