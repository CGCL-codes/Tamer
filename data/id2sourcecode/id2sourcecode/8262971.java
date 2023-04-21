    GitElement(Random random, GitComponent parent) throws NoSuchAlgorithmException {
        _parent = parent;
        UID uid = new UID();
        String tempid = "TTinitializer [" + uid.toString() + "]42[" + random.nextLong() + "]";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] msg = tempid.getBytes();
            md.update(msg);
            byte[] digest = md.digest();
            _id = "";
            String tempstr;
            byte[] c = new byte[2];
            int j;
            for (int i = 0; i < digest.length; i++) {
                j = digest[i];
                if (j < 0) j = 256 + j;
                c[0] = (byte) ((j / 16) + 48);
                if (c[0] > 57) c[0] += 7;
                c[1] = (byte) ((j % 16) + 48);
                if (c[1] > 57) c[1] += 7;
                tempstr = new String(c);
                _id = _id + tempstr;
            }
        } catch (NoSuchAlgorithmException e) {
            if (category.isEnabledFor(Priority.ERROR)) category.error(e);
            throw new NoSuchAlgorithmException("Could not find hash algorithm SHA-1.");
        }
    }
