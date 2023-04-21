    public static void main(String[] args) throws Exception {
        if (args.length == 0 || args[0].equals("write")) {
            writeMPI(new BigInteger("9"), System.out);
            writeMPI(new BigInteger("1234567890123456789"), System.out);
            writeMPI(new BigInteger("100200300400500600700800900"), System.out);
        } else if (args[0].equals("read")) {
            System.out.println("9");
            System.out.println(readMPI(System.in));
            System.out.println("1234567890123456789");
            System.out.println(readMPI(System.in));
            System.out.println("100200300400500600700800900");
            System.out.println(readMPI(System.in));
        } else if (args[0].equals("write-mpi")) {
            writeMPI(new BigInteger(args[1]), System.out);
        } else if (args[0].equals("read-mpi")) {
            System.err.println(readMPI(System.in));
        } else if (args[0].equals("keygen")) {
            byte[] entropy = readMPI(System.in).toByteArray();
            byte[] key = new byte[(args.length > 1 ? Integer.parseInt(args[1]) : 16)];
            makeKey(entropy, key, 0, key.length);
            System.err.println(freenet.support.Fields.bytesToHex(key, 0, key.length));
        } else if (args[0].equals("shatest")) {
            synchronized (ctx) {
                ctx.digest();
                ctx.update((byte) 'a');
                ctx.update((byte) 'b');
                ctx.update((byte) 'c');
                byte[] hash = ctx.digest();
                System.err.println(freenet.support.Fields.bytesToHex(hash, 0, hash.length));
            }
        }
    }
