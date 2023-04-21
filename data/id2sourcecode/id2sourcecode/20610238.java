    public static void createDigest(List<Resource> resources, File output) throws IOException {
        MessageDigest md = getMessageDigest();
        StringBuilder data = new StringBuilder();
        PrintWriter pout = new PrintWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8"));
        for (Resource rsrc : resources) {
            String path = rsrc.getPath();
            try {
                String digest = rsrc.computeDigest(md, null);
                note(data, path, digest);
                pout.println(path + " = " + digest);
            } catch (Throwable t) {
                throw (IOException) new IOException("Error computing digest for: " + rsrc).initCause(t);
            }
        }
        md.reset();
        byte[] contents = data.toString().getBytes("UTF-8");
        pout.println(DIGEST_FILE + " = " + StringUtil.hexlate(md.digest(contents)));
        pout.close();
    }
