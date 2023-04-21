    private void checkFile(FileInfo info) throws IOException {
        File file = info.toFile();
        service.getLog().info("M-READ file:" + file);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        DigestInputStream dis = new DigestInputStream(in, md);
        try {
            DcmParser parser = pFact.newDcmParser(dis);
            parser.parseDcmFile(FileFormat.DICOM_FILE, Tags.PixelData);
            if (parser.getReadTag() == Tags.PixelData) {
                if (parser.getReadLength() == -1) {
                    while (parser.parseHeader() == Tags.Item) {
                        readOut(parser.getInputStream(), parser.getReadLength());
                    }
                }
                readOut(parser.getInputStream(), parser.getReadLength());
                parser.parseDataset(parser.getDcmDecodeParam(), -1);
            }
        } finally {
            try {
                dis.close();
            } catch (IOException ignore) {
            }
        }
        byte[] md5 = md.digest();
        if (!Arrays.equals(md5, info.getFileMd5())) {
            throw new IOException("MD5 mismatch");
        }
    }
