    public final void testUpdatebyteArrayintint02() {
        boolean flag;
        byte[] digest1 = null;
        byte[] digest2 = null;
        for (int j = 0; j < LENGTH; j++) {
            byte[] bytes = new byte[j];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (i & 0xFF);
            }
            md.update((byte) 0);
            digest1 = md.digest();
            md.update((byte) 0);
            md.update(bytes, 0, bytes.length);
            digest2 = md.digest();
            flag = true;
            for (int i = 0; i < digest1.length; i++) {
                flag &= digest1[i] == digest2[i];
            }
            if (j == 0) {
                assertTrue("ATTENTION: digests are not the same", flag);
            } else {
                assertFalse("ATTENTION: digests are the same; j=" + j, flag);
            }
        }
    }
