    public static String computeHash(String qualifiedMethodName, String methodSignature) {
        String fullMethodName = qualifiedMethodName + methodSignature;
        byte[] bytes = fullMethodName.getBytes();
        md5.reset();
        md5.update(bytes);
        byte[] messageBytes = md5.digest();
        StringBuffer message = new StringBuffer();
        for (byte aByte : messageBytes) {
            String expanded = Integer.toHexString(0xFF & aByte);
            if (expanded.length() == 1) {
                message.append("0");
            }
            message.append(expanded);
        }
        return message.toString();
    }
