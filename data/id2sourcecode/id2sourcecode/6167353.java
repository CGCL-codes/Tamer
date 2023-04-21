    @SuppressWarnings("unchecked")
    public static void signFile(File fileToBeSigned, File keyFile, File signedFile, char[] pass) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, PGPException, SignatureException {
        if (fileToBeSigned.equals(signedFile)) {
            throw new IOException("Source file and signed file are the same");
        }
        Security.addProvider(new BouncyCastleProvider());
        InputStream keyIn = PGPUtil.getDecoderStream(new FileInputStream(keyFile));
        OutputStream out = new FileOutputStream(signedFile);
        int digest = PGPUtil.SHA1;
        PGPSecretKey pgpSecKey = readSecretKey(keyIn);
        PGPPrivateKey pgpPrivKey = pgpSecKey.extractPrivateKey(pass, "BC");
        PGPSignatureGenerator sGen = new PGPSignatureGenerator(pgpSecKey.getPublicKey().getAlgorithm(), digest, "BC");
        PGPSignatureSubpacketGenerator spGen = new PGPSignatureSubpacketGenerator();
        sGen.initSign(PGPSignature.CANONICAL_TEXT_DOCUMENT, pgpPrivKey);
        Iterator it = pgpSecKey.getPublicKey().getUserIDs();
        if (it.hasNext()) {
            spGen.setSignerUserID(false, (String) it.next());
            sGen.setHashedSubpackets(spGen.generate());
        }
        FileInputStream fIn = new FileInputStream(fileToBeSigned);
        ArmoredOutputStream aOut = new ArmoredOutputStream(out);
        aOut.beginClearText(digest);
        ByteArrayOutputStream lineOut = new ByteArrayOutputStream();
        int lookAhead = readInputLine(lineOut, fIn);
        processLine(aOut, sGen, lineOut.toByteArray());
        if (lookAhead != -1) {
            do {
                lookAhead = readInputLine(lineOut, lookAhead, fIn);
                sGen.update((byte) '\r');
                sGen.update((byte) '\n');
                processLine(aOut, sGen, lineOut.toByteArray());
            } while (lookAhead != -1);
        }
        aOut.endClearText();
        BCPGOutputStream bOut = new BCPGOutputStream(aOut);
        sGen.generate().encode(bOut);
        aOut.close();
        bOut.close();
        out.close();
    }
