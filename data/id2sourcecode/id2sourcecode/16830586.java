    CMSG_SESSION_KEY(ITrueRandom trueRandom_, SMSG_PUBLIC_KEY keypacket_) throws SSHSetupException {
        super();
        try {
            _md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new SSHSetupException("MD5 hash algorithm not supported");
        }
        byte[] anti_spoofing_cookie = keypacket_.getAntiSpoofingCookie();
        byte[] server_key_public_modulus = keypacket_.getServerKeyPublicModulus();
        byte[] host_key_public_modulus = keypacket_.getHostKeyPublicModulus();
        byte[] server_key_public_exponent = keypacket_.getServerKeyPublicExponent();
        byte[] host_key_public_exponent = keypacket_.getHostKeyPublicExponent();
        byte[] supported_ciphers_mask = keypacket_.getSupportedCiphersMask();
        _session_id = new byte[host_key_public_modulus.length + server_key_public_modulus.length + anti_spoofing_cookie.length];
        System.arraycopy(host_key_public_modulus, 0, _session_id, 0, host_key_public_modulus.length);
        System.arraycopy(server_key_public_modulus, 0, _session_id, host_key_public_modulus.length, server_key_public_modulus.length);
        System.arraycopy(anti_spoofing_cookie, 0, _session_id, host_key_public_modulus.length + server_key_public_modulus.length, anti_spoofing_cookie.length);
        _md5.reset();
        _session_id = _md5.digest(_session_id);
        if ((supported_ciphers_mask[3] & (byte) (1 << Cipher.SSH_CIPHER_BLOWFISH)) != 0) {
            _cipher_type = Cipher.SSH_CIPHER_BLOWFISH;
            _cipher_name = "Blowfish";
        } else {
            if ((supported_ciphers_mask[3] & (1 << Cipher.SSH_CIPHER_IDEA)) != 0) {
                _cipher_type = Cipher.SSH_CIPHER_IDEA;
                _cipher_name = "IDEA";
            } else {
                if ((supported_ciphers_mask[3] & (1 << Cipher.SSH_CIPHER_3DES)) != 0) {
                    _cipher_type = Cipher.SSH_CIPHER_3DES;
                    _cipher_name = "DES3";
                } else {
                    throw new SSHSetupException("server does not support IDEA, BlowFish or 3DES, " + "cipher mask is 0x" + Integer.toString(supported_ciphers_mask[3], 16));
                }
            }
        }
        byte[] random_bits1 = new byte[16];
        byte[] random_bits2 = new byte[16];
        trueRandom_.getRandomBytes(random_bits1);
        trueRandom_.getRandomBytes(random_bits2);
        _session_key = SSHMisc.concatenate(random_bits1, random_bits2);
        byte[] session_keyXored = SSHMisc.XORByteArrays(random_bits1, _session_id);
        session_keyXored = SSHMisc.concatenate(session_keyXored, random_bits2);
        byte[] encrypted_session_key;
        if (server_key_public_modulus.length < host_key_public_modulus.length) {
            byte[] server_key_encrypted_data = RSAAlgorithm.publicKeyEncrypt(session_keyXored, server_key_public_exponent, server_key_public_modulus, _md5);
            encrypted_session_key = RSAAlgorithm.publicKeyEncrypt(server_key_encrypted_data, host_key_public_exponent, host_key_public_modulus, _md5);
        } else {
            byte[] server_key_encrypted_data = RSAAlgorithm.publicKeyEncrypt(session_keyXored, host_key_public_exponent, host_key_public_modulus, _md5);
            encrypted_session_key = RSAAlgorithm.publicKeyEncrypt(server_key_encrypted_data, server_key_public_exponent, server_key_public_modulus, _md5);
        }
        byte[] protocol_flags = new byte[4];
        protocol_flags[0] = protocol_flags[1] = protocol_flags[2] = protocol_flags[3] = 0;
        int block_length = 1 + 1 + anti_spoofing_cookie.length + 2 + encrypted_session_key.length + protocol_flags.length;
        super._data = new byte[block_length];
        int offset = 0;
        super._data[offset++] = (byte) SSH_CMSG_SESSION_KEY;
        super._data[offset++] = (byte) _cipher_type;
        for (int i = 0; i < 8; i++) super._data[offset++] = anti_spoofing_cookie[i];
        super._data[offset++] = (byte) (((8 * encrypted_session_key.length) >> 8) & 0xff);
        super._data[offset++] = (byte) ((8 * encrypted_session_key.length) & 0xff);
        for (int i = 0; i < encrypted_session_key.length; i++) super._data[offset++] = encrypted_session_key[i];
        for (int i = 0; i < 4; i++) super._data[offset++] = protocol_flags[i];
    }
