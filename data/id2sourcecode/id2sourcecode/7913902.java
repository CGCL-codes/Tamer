    public static void main(String[] args) {
        SHA1 sha = new SHA1();
        byte[] dig1 = new byte[20];
        byte[] dig2 = new byte[20];
        byte[] dig3 = new byte[20];
        sha.update("abc".getBytes());
        sha.digest(dig1);
        sha.update("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".getBytes());
        sha.digest(dig2);
        for (int i = 0; i < 1000000; i++) sha.update((byte) 'a');
        sha.digest(dig3);
        String dig1_res = toHexString(dig1);
        String dig2_res = toHexString(dig2);
        String dig3_res = toHexString(dig3);
        String dig1_ref = "A9993E364706816ABA3E25717850C26C9CD0D89D";
        String dig2_ref = "84983E441C3BD26EBAAE4AA1F95129E5E54670F1";
        String dig3_ref = "34AA973CD4C4DAA4F61EEB2BDBAD27316534016F";
        if (dig1_res.equals(dig1_ref)) System.out.println("SHA-1 Test 1 OK."); else System.out.println("SHA-1 Test 1 FAILED.");
        if (dig2_res.equals(dig2_ref)) System.out.println("SHA-1 Test 2 OK."); else System.out.println("SHA-1 Test 2 FAILED.");
        if (dig3_res.equals(dig3_ref)) System.out.println("SHA-1 Test 3 OK."); else System.out.println("SHA-1 Test 3 FAILED.");
    }
