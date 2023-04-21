    private static String computeDigest(MessageDigest currentAlgorithm, byte[] b) {
        currentAlgorithm.reset();
        currentAlgorithm.update(b);
        byte[] hash = currentAlgorithm.digest();
        String d = " ";
        int usbyte = 0;
        for (int i = 0; i < hash.length; i += 2) {
            usbyte = hash[i] & 0xFF;
            if (usbyte < 16) d += "0" + Integer.toHexString(usbyte); else d += Integer.toHexString(usbyte);
            usbyte = hash[i + 1] & 0xFF;
            if (usbyte < 16) d += "0" + Integer.toHexString(usbyte) + " "; else d += Integer.toHexString(usbyte) + " ";
        }
        return d.toUpperCase();
    }
