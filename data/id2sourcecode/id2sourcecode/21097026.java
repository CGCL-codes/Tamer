    public DrivingLicense() throws GeneralSecurityException {
        List<Integer> tagList = new ArrayList<Integer>();
        comFile = new COMFile(1, 0, tagList, null);
        byte[] comBytes = comFile.getEncoded();
        int fileLength = comBytes.length;
        totalLength += fileLength;
        fileLengths.put(DrivingLicenseService.EF_COM, fileLength);
        rawStreams.put(DrivingLicenseService.EF_COM, new ByteArrayInputStream(comBytes));
        String signatureAlgorithm = "SHA256withRSA";
        X509Certificate certificate = generateDummyCertificate(signatureAlgorithm);
        String digestAlgorithm = "SHA256";
        Map<Integer, byte[]> hashes = new HashMap<Integer, byte[]>();
        MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
        hashes.put(1, digest.digest(new byte[0]));
        hashes.put(2, digest.digest(new byte[0]));
        sodFile = new SODFile(digestAlgorithm, signatureAlgorithm, hashes, signer, certificate);
        byte[] sodBytes = sodFile.getEncoded();
        fileLength = sodBytes.length;
        totalLength += fileLength;
        fileLengths.put(DrivingLicenseService.EF_SOD, fileLength);
        rawStreams.put(DrivingLicenseService.EF_SOD, new ByteArrayInputStream(sodBytes));
    }
