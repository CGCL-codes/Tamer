    protected ConnectionStateSSLv3(SSLSessionImpl session) {
        try {
            CipherSuite cipherSuite = session.cipherSuite;
            boolean is_exportabe = cipherSuite.isExportable();
            hash_size = cipherSuite.getMACLength();
            int key_size = (is_exportabe) ? cipherSuite.keyMaterial : cipherSuite.expandedKeyMaterial;
            int iv_size = cipherSuite.getBlockSize();
            String algName = cipherSuite.getBulkEncryptionAlgorithm();
            String hashName = cipherSuite.getHashName();
            if (logger != null) {
                logger.println("ConnectionStateSSLv3.create:");
                logger.println("  cipher suite name: " + session.getCipherSuite());
                logger.println("  encryption alg name: " + algName);
                logger.println("  hash alg name: " + hashName);
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
            PRF.computePRF_SSLv3(key_block, session.master_secret, seed);
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
                if (logger != null) {
                    logger.println("ConnectionStateSSLv3: is_exportable");
                }
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(client_key);
                md5.update(clientRandom);
                md5.update(serverRandom);
                client_key = md5.digest();
                md5.update(server_key);
                md5.update(serverRandom);
                md5.update(clientRandom);
                server_key = md5.digest();
                key_size = cipherSuite.expandedKeyMaterial;
                if (is_block_cipher) {
                    md5.update(clientRandom);
                    md5.update(serverRandom);
                    clientIV = new IvParameterSpec(md5.digest(), 0, iv_size);
                    md5.update(serverRandom);
                    md5.update(clientRandom);
                    serverIV = new IvParameterSpec(md5.digest(), 0, iv_size);
                }
            } else if (is_block_cipher) {
                clientIV = new IvParameterSpec(key_block, 2 * hash_size + 2 * key_size, iv_size);
                serverIV = new IvParameterSpec(key_block, 2 * hash_size + 2 * key_size + iv_size, iv_size);
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
                logger.print(client_key, 0, key_size);
                logger.println("server_key");
                logger.print(server_key, 0, key_size);
                if (clientIV != null) {
                    logger.println("client_iv");
                    logger.print(clientIV.getIV());
                    logger.println("server_iv");
                    logger.print(serverIV.getIV());
                } else {
                    logger.println("no IV.");
                }
            }
            encCipher = Cipher.getInstance(algName);
            decCipher = Cipher.getInstance(algName);
            messageDigest = MessageDigest.getInstance(hashName);
            if (is_client) {
                encCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(client_key, 0, key_size, algName), clientIV);
                decCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(server_key, 0, key_size, algName), serverIV);
                mac_write_secret = client_mac_secret;
                mac_read_secret = server_mac_secret;
            } else {
                encCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(server_key, 0, key_size, algName), serverIV);
                decCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(client_key, 0, key_size, algName), clientIV);
                mac_write_secret = server_mac_secret;
                mac_read_secret = client_mac_secret;
            }
            if (hashName.equals("MD5")) {
                pad_1 = SSLv3Constants.MD5pad1;
                pad_2 = SSLv3Constants.MD5pad2;
            } else {
                pad_1 = SSLv3Constants.SHApad1;
                pad_2 = SSLv3Constants.SHApad2;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new AlertException(AlertProtocol.INTERNAL_ERROR, new SSLProtocolException("Error during computation of security parameters"));
        }
    }
