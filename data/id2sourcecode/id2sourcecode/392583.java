        public byte[] chapMD5(byte id, byte[] Password, byte[] Challenge) throws Exception {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(id);
            md.update(Password, 0, Password.length);
            md.update(Challenge, 0, Challenge.length);
            return md.digest();
        }
