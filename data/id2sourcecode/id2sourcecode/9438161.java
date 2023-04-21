    public static long computeMethodHash(Method m) {
        long hash = 0;
        ByteArrayOutputStream sink = new ByteArrayOutputStream(127);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            DataOutputStream out = new DataOutputStream(new DigestOutputStream(sink, md));
            String s = getMethodNameAndDescriptor(m);
            if (serverRefLog.isLoggable(Log.VERBOSE)) {
                serverRefLog.log(Log.VERBOSE, "string used for method hash: \"" + s + "\"");
            }
            out.writeUTF(s);
            out.flush();
            byte hasharray[] = md.digest();
            for (int i = 0; i < Math.min(8, hasharray.length); i++) {
                hash += ((long) (hasharray[i] & 0xFF)) << (i * 8);
            }
        } catch (IOException ignore) {
            hash = -1;
        } catch (NoSuchAlgorithmException complain) {
            throw new SecurityException(complain.getMessage());
        }
        return hash;
    }
