    public boolean ConvertToMD5ChallengeResponse(String UserName, String Password, byte[] src_mac) {
        if (getEAPOLType() == EAPOL_TYPE_EAPPACKET && getEAPCode() == EAP_CODE_REQUEST && getEAPType() == EAP_REQUEST_MD5_CHALLENGE) {
            byte[] newdata = new byte[82];
            System.arraycopy(data, 0, newdata, 0, data.length);
            data = newdata;
            ((EthernetPacket) this.datalink).src_mac = src_mac;
            setEAPOLLenght((short) (22 + UserName.length()));
            setEAPCode(EAP_CODE_RESPONSE);
            setEAPLenght((short) (22 + UserName.length()));
            java.security.MessageDigest digest;
            try {
                digest = java.security.MessageDigest.getInstance("MD5");
                byte[] pwd = new byte[1 + Password.length() + 16];
                System.arraycopy(Password.getBytes(), 0, pwd, 1, Password.getBytes().length);
                System.arraycopy(getEAPValue(), 0, pwd, 1 + Password.getBytes().length, 16);
                digest.update(pwd);
                setEAPValue(digest.digest());
                setEAPExtra(UserName.getBytes());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
