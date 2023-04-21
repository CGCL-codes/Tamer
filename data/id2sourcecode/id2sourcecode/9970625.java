    public void write(byte[] arg0, int arg1, int arg2) {
        if (log.isDebugEnabled()) {
            log.debug("Canonicalized SignedInfo:");
            StringBuilder sb = new StringBuilder(arg2);
            for (int i = arg1; i < (arg1 + arg2); i++) {
                sb.append((char) arg0[i]);
            }
            log.debug(sb.toString());
        }
        try {
            System.err.println("SignerOutputStream.write():" + new String(arg0, arg1, arg2));
            try {
                MessageDigest hash = MessageDigest.getInstance("SHA-256", "BC");
                hash.reset();
                byte[] toHash = new byte[arg2];
                System.arraycopy(arg0, arg1, toHash, 0, arg2);
                EbicsUtil.saveToFile(toHash, new File("toHash.bin"));
                ByteArrayOutputStream o = new ByteArrayOutputStream();
                for (int i = 0; i < toHash.length; i++) {
                    byte b = toHash[i];
                    o.write(b);
                }
                o.close();
                EbicsUtil.saveToFile(toHash, new File("toHash.bin"));
                byte[] r = hash.digest(toHash);
                System.err.println("== toHash");
                System.err.println(new String(toHash));
                System.err.println(HexDump.toHex(toHash));
                System.err.println("== r");
                System.err.println(HexDump.toHex(r));
            } catch (Exception e) {
                e.printStackTrace();
            }
            sa.update(arg0, arg1, arg2);
        } catch (XMLSignatureException e) {
            throw new RuntimeException("" + e);
        }
    }
