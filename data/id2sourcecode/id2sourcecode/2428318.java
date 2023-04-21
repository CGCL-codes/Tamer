    public static long transfer(Reader rdr, Writer wtr, int buffSize) throws IOException {
        long ret = 0;
        char[] buff = new char[buffSize];
        BufferedReader br = new BufferedReader(rdr, 2048);
        try {
            int readIn;
            while ((readIn = br.read(buff)) > 0) {
                ret += readIn;
                wtr.write(buff, 0, readIn);
            }
        } finally {
            br.close();
        }
        return ret;
    }
