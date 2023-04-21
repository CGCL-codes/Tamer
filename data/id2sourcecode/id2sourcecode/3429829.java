    public void generate() throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        int sizecontent = ((int) src.length());
        byte[] contentbytes = new byte[sizecontent];
        FileInputStream freader = new FileInputStream(src);
        System.out.println("\nContent Bytes: " + freader.read(contentbytes, 0, sizecontent));
        freader.close();
        MessageDigest md = MessageDigest.getInstance("SHA-1", "BC");
        byte[] fingerprint = md.digest(contentbytes);
        System.out.println("Digest : " + ApduData.toHexString(fingerprint));
        TimeStampToken tst = null;
        TimeStampTokenGetter tstGetter = new TimeStampTokenGetter(new PostMethod(url), fingerprint, BigInteger.valueOf(0));
        tst = tstGetter.getTimeStampToken();
        if (tst == null) {
            System.out.println("NO TST");
            return;
        }
        byte[] tsrdata = tst.getEncoded();
        System.out.println("Got tsr " + tsrdata.length + " bytes");
        FileOutputStream efos = new FileOutputStream(dest);
        efos.write(tsrdata);
        efos.close();
    }
