    private String handlePacket1(SshPacket1 p) throws IOException {
        byte b;
        if (debug > 0) System.out.println("1 packet to handle, type " + p.getType());
        switch(p.getType()) {
            case SSH_MSG_IGNORE:
                return "";
            case SSH_MSG_DISCONNECT:
                String str = p.getString();
                disconnect();
                return str;
            case SSH_SMSG_PUBLIC_KEY:
                byte[] anti_spoofing_cookie;
                byte[] server_key_bits;
                byte[] server_key_public_exponent;
                byte[] server_key_public_modulus;
                byte[] host_key_bits;
                byte[] host_key_public_exponent;
                byte[] host_key_public_modulus;
                byte[] protocol_flags;
                byte[] supported_ciphers_mask;
                byte[] supported_authentications_mask;
                anti_spoofing_cookie = p.getBytes(8);
                server_key_bits = p.getBytes(4);
                server_key_public_exponent = p.getMpInt();
                server_key_public_modulus = p.getMpInt();
                host_key_bits = p.getBytes(4);
                host_key_public_exponent = p.getMpInt();
                host_key_public_modulus = p.getMpInt();
                protocol_flags = p.getBytes(4);
                supported_ciphers_mask = p.getBytes(4);
                supported_authentications_mask = p.getBytes(4);
                String ret = Send_SSH_CMSG_SESSION_KEY(anti_spoofing_cookie, server_key_public_modulus, host_key_public_modulus, supported_ciphers_mask, server_key_public_exponent, host_key_public_exponent);
                if (ret != null) return ret;
                if (hashHostKey != null && hashHostKey.compareTo("") != 0) {
                    byte[] Md5_hostKey = md5.digest(host_key_public_modulus);
                    String hashHostKeyBis = "";
                    for (int i = 0; i < Md5_hostKey.length; i++) {
                        String hex = "";
                        int[] v = new int[2];
                        v[0] = (Md5_hostKey[i] & 240) >> 4;
                        v[1] = (Md5_hostKey[i] & 15);
                        for (int j = 0; j < 1; j++) switch(v[j]) {
                            case 10:
                                hex += "a";
                                break;
                            case 11:
                                hex += "b";
                                break;
                            case 12:
                                hex += "c";
                                break;
                            case 13:
                                hex += "d";
                                break;
                            case 14:
                                hex += "e";
                                break;
                            case 15:
                                hex += "f";
                                break;
                            default:
                                hex += String.valueOf(v[j]);
                                break;
                        }
                        hashHostKeyBis = hashHostKeyBis + hex;
                    }
                    if (hashHostKeyBis.compareTo(hashHostKey) != 0) {
                        login = password = "";
                        return "\nHash value of the host key not correct \r\n" + "login & password have been reset \r\n" + "- erase the 'hashHostKey' parameter in the Html\r\n" + "(it is used for auhentificating the server and " + "prevent you from connecting \r\n" + "to any other)\r\n";
                    }
                }
                break;
            case SSH_SMSG_SUCCESS:
                if (debug > 0) System.out.println("SSH_SMSG_SUCCESS (last packet was " + lastPacketSentType + ")");
                if (lastPacketSentType == SSH_CMSG_SESSION_KEY) {
                    Send_SSH_CMSG_USER();
                    break;
                }
                if (lastPacketSentType == SSH_CMSG_USER) {
                    Send_SSH_CMSG_REQUEST_PTY();
                    return "\nEmpty password login.\r\n";
                }
                if (lastPacketSentType == SSH_CMSG_AUTH_PASSWORD) {
                    if (debug > 0) System.out.println("login succesful");
                    Send_SSH_CMSG_REQUEST_PTY();
                    return "\nLogin & password accepted\r\n";
                }
                if (lastPacketSentType == SSH_CMSG_REQUEST_PTY) {
                    cansenddata = true;
                    if (dataToSend != null) {
                        Send_SSH_CMSG_STDIN_DATA(dataToSend);
                        dataToSend = null;
                    }
                    Send_SSH_CMSG_EXEC_SHELL();
                    break;
                }
                if (lastPacketSentType == SSH_CMSG_EXEC_SHELL) {
                }
                break;
            case SSH_SMSG_FAILURE:
                if (debug > 1) System.err.println("SSH_SMSG_FAILURE");
                if (lastPacketSentType == SSH_CMSG_AUTH_PASSWORD) {
                    System.out.println("failed to log in");
                    Send_SSH_MSG_DISCONNECT("Failed to log in.");
                    disconnect();
                    return "\nLogin & password not accepted\r\n";
                }
                if (lastPacketSentType == SSH_CMSG_USER) {
                    Send_SSH_CMSG_AUTH_PASSWORD();
                    break;
                }
                if (lastPacketSentType == SSH_CMSG_REQUEST_PTY) {
                    break;
                }
                break;
            case SSH_SMSG_STDOUT_DATA:
                return p.getString();
            case SSH_SMSG_STDERR_DATA:
                str = "Error : " + p.getString();
                System.out.println("SshIO.handlePacket : " + "STDERR_DATA " + str);
                return str;
            case SSH_SMSG_EXITSTATUS:
                int value = p.getInt32();
                Send_SSH_CMSG_EXIT_CONFIRMATION();
                System.out.println("SshIO : Exit status " + value);
                disconnect();
                break;
            case SSH_MSG_DEBUG:
                str = p.getString();
                if (debug > 0) {
                    System.out.println("SshIO.handlePacket : " + " DEBUG " + str);
                    return str;
                }
                return "";
            default:
                System.err.print("SshIO.handlePacket1: Packet Type unknown: " + p.getType());
                break;
        }
        return "";
    }
