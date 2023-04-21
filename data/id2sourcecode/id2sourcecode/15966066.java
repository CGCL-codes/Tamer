    public static String computeMD5(InputStream is) {
        if (is == null) return null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String str;
            StringBuffer sb = new StringBuffer();
            while ((str = reader.readLine()) != null) sb.append(str + "\n");
            String byteChars = "0123456789abcdef";
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(sb.toString().getBytes());
            sb = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                int loNibble = bytes[i] & 0xf;
                int hiNibble = (bytes[i] >> 4) & 0xf;
                sb.append(byteChars.charAt(hiNibble));
                sb.append(byteChars.charAt(loNibble));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            s_logger.error("Could not find MD5 digest algorithm.");
        } catch (IOException e) {
            s_logger.error("Exception: ", e);
        }
        return null;
    }
