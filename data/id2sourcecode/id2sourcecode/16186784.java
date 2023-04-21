    public AuthListFiles(String[] argv) throws Exception {
        NtlmAuthenticator.setDefault(this);
        SmbFileInputStream in = null;
        try {
            in = new SmbFileInputStream(argv[0] + "1.doc");
            System.out.println(in.toString());
            OutputStream out = System.out;
            int nextChar;
            while ((nextChar = in.read()) != -1) out.write(Character.toUpperCase((char) nextChar));
            out.write('\n');
            out.flush();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
