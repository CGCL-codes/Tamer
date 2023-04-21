    public boolean verify(int rt, long io, User from, BigInteger Ca) {
        SHA1 ctx = new SHA1();
        updint(ctx, rt);
        byte[] cab = Util.MPIbytes(Ca);
        ctx.update(cab, 0, cab.length);
        updint(ctx, (int) (io >> 32));
        updint(ctx, (int) io);
        updint(ctx, grantedBy);
        byte[] cbb = Util.MPIbytes(C);
        ctx.update(cbb, 0, cbb.length);
        updint(ctx, ip);
        updsrt(ctx, port);
        byte[] dig = ctx.digest();
        BigInteger M = Util.byteArrayToMPI(dig);
        return DSA.verify(from.getSignaturePublicKey(), sig, M);
    }
