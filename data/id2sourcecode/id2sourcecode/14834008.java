    public static void verifyMD5(File file, String originalMd5) throws Exception {
        log.info("M-READ file:" + file);
        MessageDigest md = MessageDigest.getInstance("MD5");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        DigestInputStream dis = new DigestInputStream(in, md);
        try {
            DcmParser parser = DcmParserFactory.getInstance().newDcmParser(dis);
            parser.parseDcmFile(FileFormat.DICOM_FILE, Tags.PixelData);
            if ((parser.getReadTag() & 0xFFFFFFFFL) >= Tags.PixelData) {
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
        if (!Arrays.equals(md5, MD5.toBytes(originalMd5))) {
            log.error("MD5 for " + file.getAbsolutePath() + " is different that expected.  Has the file been changed or corrupted?");
            throw new IllegalStateException("MD5 mismatch");
        }
    }
