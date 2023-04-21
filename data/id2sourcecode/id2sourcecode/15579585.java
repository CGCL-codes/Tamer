    public static byte[] compressFile(File inFile, File outFile, String tsuid, int[] planarConfiguration, int[] pxdataVR, byte[] buffer) throws Exception {
        log.info("M-READ file:" + inFile);
        InputStream in = new BufferedInputStream(new FileInputStream(inFile));
        try {
            DcmParser p = DcmParserFactory.getInstance().newDcmParser(in);
            final DcmObjectFactory of = DcmObjectFactory.getInstance();
            Dataset ds = of.newDataset();
            p.setDcmHandler(ds.getDcmHandler());
            p.parseDcmFile(FileFormat.DICOM_FILE, Tags.PixelData);
            if (planarConfiguration != null && planarConfiguration.length != 0) planarConfiguration[0] = ds.getInt(Tags.PlanarConfiguration, 0);
            if (pxdataVR != null && pxdataVR.length != 0) pxdataVR[0] = p.getReadVR();
            FileMetaInfo fmi = of.newFileMetaInfo(ds, tsuid);
            ds.setFileMetaInfo(fmi);
            log.info("M-WRITE file:" + outFile);
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestOutputStream dos = new DigestOutputStream(new FileOutputStream(outFile), md);
            BufferedOutputStream bos = new BufferedOutputStream(dos, buffer);
            try {
                DcmDecodeParam decParam = p.getDcmDecodeParam();
                DcmEncodeParam encParam = DcmEncodeParam.valueOf(tsuid);
                CompressCmd compressCmd = CompressCmd.createCompressCmd(ds, tsuid);
                compressCmd.coerceDataset(ds);
                ds.writeFile(bos, encParam);
                ds.writeHeader(bos, encParam, Tags.PixelData, VRs.OB, -1);
                int read = compressCmd.compress(decParam.byteOrder, in, bos, null);
                ds.writeHeader(bos, encParam, Tags.SeqDelimitationItem, VRs.NONE, 0);
                skipFully(in, p.getReadLength() - read);
                p.parseDataset(decParam, -1);
                ds.subSet(Tags.PixelData, -1).writeDataset(bos, encParam);
            } finally {
                bos.close();
            }
            return md.digest();
        } finally {
            in.close();
        }
    }
