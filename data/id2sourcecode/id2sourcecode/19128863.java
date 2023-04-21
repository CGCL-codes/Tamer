    public static byte[] readBodyBytes() throws Exception {
        BufferedInputStream is = new BufferedInputStream(ActionHelper.getRequest().getInputStream());
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(boas);
        int c;
        while ((c = is.read()) != -1) out.write(c);
        out.close();
        return boas.toByteArray();
    }
