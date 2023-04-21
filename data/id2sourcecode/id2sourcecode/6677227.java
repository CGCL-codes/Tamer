    private byte[] doPhase4(byte[] comparison, boolean[] doModify) throws NoSuchAlgorithmException {
        byte[] magicKey = new byte[4];
        for (int i = 0; i < 4; i++) {
            magicKey[i] = comparison[i];
        }
        int depth = 0;
        int table = 0;
        depthLoop: for (depth = 0; depth < 65535; depth++) {
            tableLoop: for (table = 0; table < 5; table++) {
                byte[] test = new byte[3];
                test[0] = (byte) depth;
                test[1] = (byte) (depth >> 8);
                test[2] = (byte) table;
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(magicKey);
                md5.update(test);
                byte[] result = md5.digest();
                for (int i = 0; i < 16; i++) {
                    if (result[i] != comparison[4 + i]) {
                        continue tableLoop;
                    }
                }
                break depthLoop;
            }
        }
        if (table != 0) {
            long updatedKey = (magicKey[0] & 0xff) | ((magicKey[1] & 0xff) << 8) | ((magicKey[2] & 0xff) << 16) | ((magicKey[3] & 0xff) << 24);
            updatedKey = ChallengeResponseTable.yahoo_auth_finalCountdown(updatedKey, 0x60, table, depth);
            updatedKey = ChallengeResponseTable.yahoo_auth_finalCountdown(updatedKey, 0x60, table, depth);
            magicKey[0] = (byte) (updatedKey & 0xff);
            magicKey[1] = (byte) ((updatedKey >> 8) & 0xff);
            magicKey[2] = (byte) ((updatedKey >> 16) & 0xff);
            magicKey[3] = (byte) ((updatedKey >>> 24) & 0xff);
        }
        if (table >= 3) {
            doModify[0] = true;
        }
        return magicKey;
    }
