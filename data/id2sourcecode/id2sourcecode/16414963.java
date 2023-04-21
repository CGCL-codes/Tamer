    public TicketResponse issueResponse(User from, InetAddress ipaddr, Random r) {
        SHA1 ctx = new SHA1();
        C = ElGamal.decrypt(from.getEncryptionPublicKey().getGroup(), from.getEncryptionPrivateKey(), E);
        System.err.println(C);
        updint(ctx, grantedBy);
        byte[] cab = Util.MPIbytes(C);
        ctx.update(cab, 0, cab.length);
        updint(ctx, (int) (issuedOn >> 32));
        updint(ctx, (int) issuedOn);
        updint(ctx, grantedTo);
        BigInteger Ra = new BigInteger(256, r);
        BigInteger Ca = Global.DHgroupA.getG().modPow(Ra, Global.DHgroupA.getP());
        byte[] cbb = Util.MPIbytes(Ca);
        ctx.update(cbb, 0, cbb.length);
        byte[] ipt = ipaddr.getAddress();
        int ip = ((ipt[3] & 0xff) << 24) + ((ipt[2] & 0xff) << 16) + ((ipt[1] & 0xff) << 8) + (ipt[0] & 0xff);
        updint(ctx, ip);
        short port = (short) from.getPort();
        updsrt(ctx, port);
        byte[] dig = ctx.digest();
        BigInteger M = Util.byteArrayToMPI(dig);
        DSASignature sig = DSA.sign(from.getSignaturePublicKey().getGroup(), from.getSignaturePrivateKey(), M, r);
        return new TicketResponse(grantedTo, grantedBy, ip, port, Ra, Ca, sig);
    }
