    public final void testGetDigestLength() throws DigestException {
        int digestlength = md.getDigestLength();
        byte[] bytes = new byte[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            bytes[i] = (byte) (i & 0xFF);
        }
        for (int i = 0; i < 8; i++) {
            switch(i) {
                case 0:
                    md.digest();
                    break;
                case 1:
                    md.digest(bytes, 0, bytes.length);
                    break;
                case 2:
                    md.reset();
                    break;
                case 3:
                    md.update(bytes[i]);
                    break;
                case 4:
                    md.update(bytes, 0, LENGTH);
                    break;
                case 5:
                    md.update(bytes[i]);
                    break;
                default:
            }
            assertEquals(" !=digestlength", md.getDigestLength(), digestlength);
        }
    }
