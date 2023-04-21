    public byte[] digest() {
        SHA1 ctx = new SHA1();
        updint(ctx, grantedBy());
        updint(ctx, grantedTo());
        byte[] e1b = Util.MPIbytes(E[0]);
        byte[] e2b = Util.MPIbytes(E[1]);
        ctx.update(e1b, 0, e1b.length);
        ctx.update(e2b, 0, e2b.length);
        updint(ctx, (int) (issuedOn >> 32));
        updint(ctx, (int) issuedOn);
        updint(ctx, ip);
        updsrt(ctx, port);
        byte[] m = ctx.digest();
        return m;
    }
