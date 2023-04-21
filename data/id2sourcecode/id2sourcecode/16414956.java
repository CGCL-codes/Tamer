    public static Ticket issue(User from, User to, InetAddress ipaddr, Random r) {
        int grantedBy = from.getEncryptionPublicKey().keyId();
        int grantedTo = to.getEncryptionPublicKey().keyId();
        long io = System.currentTimeMillis();
        BigInteger R = new BigInteger(256, r);
        BigInteger C = Global.DHgroupA.getG().modPow(R, Global.DHgroupA.getP());
        BigInteger[] E = ElGamal.encrypt(to.getEncryptionPublicKey(), C, r);
        byte[] e1b = Util.MPIbytes(E[0]);
        byte[] e2b = Util.MPIbytes(E[1]);
        byte[] ipt = ipaddr.getAddress();
        int ip = ((ipt[3] & 0xff) << 24) + ((ipt[2] & 0xff) << 16) + ((ipt[1] & 0xff) << 8) + (ipt[0] & 0xff);
        short port = (short) from.getPort();
        SHA1 ctx = new SHA1();
        updint(ctx, grantedBy);
        updint(ctx, grantedTo);
        ctx.update(e1b, 0, e1b.length);
        ctx.update(e2b, 0, e2b.length);
        updint(ctx, (int) (io >> 32));
        updint(ctx, (int) io);
        updint(ctx, ip);
        updsrt(ctx, port);
        byte[] m = ctx.digest();
        DSASignature sig = DSA.sign(from.getSignaturePublicKey().getGroup(), from.getSignaturePrivateKey(), Util.byteArrayToMPI(m), r);
        return new Ticket(grantedBy, grantedTo, io, ip, port, R, C, E, sig);
    }
