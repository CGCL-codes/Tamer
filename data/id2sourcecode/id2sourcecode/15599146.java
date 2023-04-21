    public boolean authenticateUser(RSAPrivateKey privateKey_) throws IOException, SSHProtocolException, SSHAuthFailedException {
        if (_phase != PHASE_AUTHENTICATE) {
            throw new RuntimeException("SSH protocol in invalid state for user authentication");
        }
        debug("Trying RSA authentication with key '" + privateKey_.getComment() + "'");
        byte[] modulus = privateKey_.getModulus();
        CMSG_AUTH_RSA auth_rsa = new CMSG_AUTH_RSA(modulus);
        _ssh_out.writePacket(auth_rsa);
        Packet packet = readNextPacket();
        int packet_type = packet.getPacketType();
        if (packet_type == SSH_SMSG_FAILURE) {
            debug("Server refused our key");
            return false;
        } else if (packet_type != SSH_SMSG_AUTH_RSA_CHALLENGE) {
            throw new SSHProtocolException("Received packet type " + packet_type + ", expected SSH_SMSG_AUTH_RSA_CHALLENGE or SSH_SMSG_FAILURE");
        }
        debug("Received RSA challenge from server.");
        SMSG_AUTH_RSA_CHALLENGE challenge_packet = (SMSG_AUTH_RSA_CHALLENGE) packet;
        byte[] challenge = challenge_packet.getChallenge();
        byte[] serverRandomBytes = RSAAlgorithm.encrypt(challenge, privateKey_.getExponent(), privateKey_.getModulus());
        serverRandomBytes = RSAAlgorithm.stripPKCSPadding(serverRandomBytes);
        byte[] toBeHashed = SSHMisc.concatenate(serverRandomBytes, _session_id);
        byte[] response_bytes;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            response_bytes = md5.digest(toBeHashed);
        } catch (NoSuchAlgorithmException e) {
            throw new SSHProtocolException("MD5 algorithm not supported");
        }
        CMSG_AUTH_RSA_RESPONSE response_packet = new CMSG_AUTH_RSA_RESPONSE(response_bytes);
        _ssh_out.writePacket(response_packet);
        packet = readNextPacket();
        packet_type = packet.getPacketType();
        if (packet_type == SSH_SMSG_FAILURE) {
            debug("RSA authentication failed");
            return false;
        } else if (packet_type != SSH_SMSG_SUCCESS) {
            throw new SSHProtocolException("Received packet type " + packet_type + ", expected SSH_MSG_SUCCESS or SSH_SMSG_FAILURE");
        }
        _phase = PHASE_PREPARATORY;
        return true;
    }
