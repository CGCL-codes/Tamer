    protected ConnectionStateTLS(SSLSessionImpl session) {
        try {
            CipherSuite cipherSuite = session.cipherSuite;
            hash_size = cipherSuite.getMACLength();
            boolean is_exportabe = cipherSuite.isExportable();
            int key_size = (is_exportabe) ? cipherSuite.keyMaterial : cipherSuite.expandedKeyMaterial;
            int iv_size = cipherSuite.getBlockSize();
            String algName = cipherSuite.getBulkEncryptionAlgorithm();
            String macName = cipherSuite.getHmacName();
            if (logger != null) {
                logger.println("ConnectionStateTLS.create:");
                logger.println("  cipher suite name: " + cipherSuite.getName());
                logger.println("  encryption alg name: " + algName);
                logger.println("  mac alg name: " + macName);
                logger.println("  hash size: " + hash_size);
                logger.println("  block size: " + iv_size);
                logger.println("  IV size (== block size):" + iv_size);
                logger.println("  key size: " + key_size);
            }
            byte[] clientRandom = session.clientRandom;
            byte[] serverRandom = session.serverRandom;
            byte[] key_block = new byte[2 * hash_size + 2 * key_size + 2 * iv_size];
            byte[] seed = new byte[clientRandom.length + serverRandom.length];
            System.arraycopy(serverRandom, 0, seed, 0, serverRandom.length);
            System.arraycopy(clientRandom, 0, seed, serverRandom.length, clientRandom.length);
            PRF.computePRF(key_block, session.master_secret, KEY_EXPANSION_LABEL, seed);
            byte[] client_mac_secret = new byte[hash_size];
            byte[] server_mac_secret = new byte[hash_size];
            byte[] client_key = new byte[key_size];
            byte[] server_key = new byte[key_size];
            boolean is_client = !session.isServer;
            is_block_cipher = (iv_size > 0);
            System.arraycopy(key_block, 0, client_mac_secret, 0, hash_size);
            System.arraycopy(key_block, hash_size, server_mac_secret, 0, hash_size);
            System.arraycopy(key_block, 2 * hash_size, client_key, 0, key_size);
            System.arraycopy(key_block, 2 * hash_size + key_size, server_key, 0, key_size);
            IvParameterSpec clientIV = null;
            IvParameterSpec serverIV = null;
            if (is_exportabe) {
                System.arraycopy(clientRandom, 0, seed, 0, clientRandom.length);
                System.arraycopy(serverRandom, 0, seed, clientRandom.length, serverRandom.length);
                byte[] final_client_key = new byte[cipherSuite.expandedKeyMaterial];
                byte[] final_server_key = new byte[cipherSuite.expandedKeyMaterial];
                PRF.computePRF(final_client_key, client_key, CLIENT_WRITE_KEY_LABEL, seed);
                PRF.computePRF(final_server_key, server_key, SERVER_WRITE_KEY_LABEL, seed);
                client_key = final_client_key;
                server_key = final_server_key;
                if (is_block_cipher) {
                    byte[] iv_block = new byte[2 * iv_size];
                    PRF.computePRF(iv_block, null, IV_BLOCK_LABEL, seed);
                    clientIV = new IvParameterSpec(iv_block, 0, iv_size);
                    serverIV = new IvParameterSpec(iv_block, iv_size, iv_size);
                }
            } else if (is_block_cipher) {
                clientIV = new IvParameterSpec(key_block, 2 * (hash_size + key_size), iv_size);
                serverIV = new IvParameterSpec(key_block, 2 * (hash_size + key_size) + iv_size, iv_size);
            }
            if (logger != null) {
                logger.println("is exportable: " + is_exportabe);
                logger.println("master_secret");
                logger.print(session.master_secret);
                logger.println("client_random");
                logger.print(clientRandom);
                logger.println("server_random");
                logger.print(serverRandom);
                logger.println("client_mac_secret");
                logger.print(client_mac_secret);
                logger.println("server_mac_secret");
                logger.print(server_mac_secret);
                logger.println("client_key");
                logger.print(client_key);
                logger.println("server_key");
                logger.print(server_key);
                if (clientIV == null) {
                    logger.println("no IV.");
                } else {
                    logger.println("client_iv");
                    logger.print(clientIV.getIV());
                    logger.println("server_iv");
                    logger.print(serverIV.getIV());
                }
            }
            encCipher = Cipher.getInstance(algName);
            decCipher = Cipher.getInstance(algName);
            encMac = Mac.getInstance(macName);
            decMac = Mac.getInstance(macName);
            if (is_client) {
                encCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(client_key, algName), clientIV);
                decCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(server_key, algName), serverIV);
                encMac.init(new SecretKeySpec(client_mac_secret, macName));
                decMac.init(new SecretKeySpec(server_mac_secret, macName));
            } else {
                encCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(server_key, algName), serverIV);
                decCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(client_key, algName), clientIV);
                encMac.init(new SecretKeySpec(server_mac_secret, macName));
                decMac.init(new SecretKeySpec(client_mac_secret, macName));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new AlertException(AlertProtocol.INTERNAL_ERROR, new SSLProtocolException("Error during computation of security parameters"));
        }
    }
