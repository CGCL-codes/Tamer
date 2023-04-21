    private String Send_SSH_CMSG_SESSION_KEY(byte[] anti_spoofing_cookie, byte[] server_key_public_modulus, byte[] host_key_public_modulus, byte[] supported_ciphers_mask, byte[] server_key_public_exponent, byte[] host_key_public_exponent) throws IOException {
        String str;
        int boffset;
        byte cipher_types;
        byte[] session_key;
        byte[] session_id_byte = new byte[host_key_public_modulus.length + server_key_public_modulus.length + anti_spoofing_cookie.length];
        System.arraycopy(host_key_public_modulus, 0, session_id_byte, 0, host_key_public_modulus.length);
        System.arraycopy(server_key_public_modulus, 0, session_id_byte, host_key_public_modulus.length, server_key_public_modulus.length);
        System.arraycopy(anti_spoofing_cookie, 0, session_id_byte, host_key_public_modulus.length + server_key_public_modulus.length, anti_spoofing_cookie.length);
        byte[] hash_md5 = md5.digest(session_id_byte);
        if ((supported_ciphers_mask[3] & (byte) (1 << SSH_CIPHER_BLOWFISH)) != 0) {
            cipher_types = (byte) SSH_CIPHER_BLOWFISH;
            cipher_type = "Blowfish";
        } else {
            if ((supported_ciphers_mask[3] & (1 << SSH_CIPHER_IDEA)) != 0) {
                cipher_types = (byte) SSH_CIPHER_IDEA;
                cipher_type = "IDEA";
            } else {
                if ((supported_ciphers_mask[3] & (1 << SSH_CIPHER_3DES)) != 0) {
                    cipher_types = (byte) SSH_CIPHER_3DES;
                    cipher_type = "DES3";
                } else {
                    if ((supported_ciphers_mask[3] & (1 << SSH_CIPHER_DES)) != 0) {
                        cipher_types = (byte) SSH_CIPHER_DES;
                        cipher_type = "DES";
                    } else {
                        System.err.println("SshIO: remote server does not supported IDEA, BlowFish or 3DES, support cypher mask is " + supported_ciphers_mask[3] + ".\n");
                        Send_SSH_MSG_DISCONNECT("No more auth methods available.");
                        disconnect();
                        return "\rRemote server does not support IDEA/Blowfish/3DES blockcipher, closing connection.\r\n";
                    }
                }
            }
        }
        if (debug > 0) System.out.println("SshIO: Using " + cipher_type + " blockcipher.\n");
        byte[] random_bits1 = new byte[16], random_bits2 = new byte[16];
        SecureRandom random = new java.security.SecureRandom(random_bits1);
        random.nextBytes(random_bits1);
        random.nextBytes(random_bits2);
        session_key = SshMisc.addArrayOfBytes(random_bits1, random_bits2);
        byte[] session_keyXored = SshMisc.XORArrayOfBytes(random_bits1, hash_md5);
        session_keyXored = SshMisc.addArrayOfBytes(session_keyXored, random_bits2);
        byte[] encrypted_session_key = SshCrypto.encrypteRSAPkcs1Twice(session_keyXored, server_key_public_exponent, server_key_public_modulus, host_key_public_exponent, host_key_public_modulus);
        int protocol_flags = 0;
        SshPacket1 packet = new SshPacket1(SSH_CMSG_SESSION_KEY);
        packet.putByte((byte) cipher_types);
        packet.putBytes(anti_spoofing_cookie);
        packet.putBytes(encrypted_session_key);
        packet.putInt32(protocol_flags);
        sendPacket1(packet);
        crypto = new SshCrypto(cipher_type, session_key);
        return "";
    }
