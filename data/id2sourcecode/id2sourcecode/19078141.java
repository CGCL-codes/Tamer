    public void setRewrite(String in) throws Exception {
        if (!in.contains(",")) throw new Exception("Property set must be user@domain,user2@domain2");
        String[] args = in.split(",");
        EmailAddress from = EmailAddress.parseAddress(args[0]);
        EmailAddress to;
        if (args[1].equals("remove")) to = null; else to = EmailAddress.parseAddress(args[1]);
        if (rewriteMap.containsKey(from)) throw new Exception("Alias already defined");
        rewriteMap.put(from, to);
    }
