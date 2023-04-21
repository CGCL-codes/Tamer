    public void testDigest() {
        String data = "abc";
        SHA256Pws hasher = new SHA256Pws();
        byte[] digest = hasher.digest(data.getBytes());
        String result = Util.bytesToHex(digest);
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", result);
    }
