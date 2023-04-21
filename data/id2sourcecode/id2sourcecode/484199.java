    public static String md5String(BigInteger number) {
        return arrayToString(MD5.digest(number.toByteArray()).clone());
    }
