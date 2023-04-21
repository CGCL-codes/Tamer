    public static byte[] decompressFile(File inFile, File outFile, String outTS, int planarConfiguration, int pxdataVR, byte[] buffer) throws Exception {
        log.info("M-READ file:" + inFile);
        FileImageInputStream fiis = new FileImageInputStream(inFile);
        try {
            DcmParser parser = DcmParserFactory.getInstance().newDcmParser(fiis);
            DcmObjectFactory dof = DcmObjectFactory.getInstance();
            Dataset ds = dof.newDataset();
            parser.setDcmHandler(ds.getDcmHandler());
            parser.parseDcmFile(FileFormat.DICOM_FILE, Tags.PixelData);
            log.info("M-WRITE file:" + outFile);
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestOutputStream dos = new DigestOutputStream(new FileOutputStream(outFile), md);
            BufferedOutputStream bos = new BufferedOutputStream(dos, buffer);
            try {
                DcmEncodeParam encParam = DcmEncodeParam.valueOf(outTS);
                String inTS = getTransferSyntax(ds);
                adjustPhotometricInterpretation(ds, inTS);
                if (planarConfiguration >= 0 && ds.contains(Tags.PlanarConfiguration)) ds.putUS(Tags.PlanarConfiguration, planarConfiguration);
                DecompressCmd cmd = new DecompressCmd(ds, inTS, parser);
                int len = cmd.getPixelDataLength();
                FileMetaInfo fmi = dof.newFileMetaInfo(ds, outTS);
                ds.setFileMetaInfo(fmi);
                ds.writeFile(bos, encParam);
                ds.writeHeader(bos, encParam, Tags.PixelData, pxdataVR, (len + 1) & ~1);
                try {
                    cmd.decompress(encParam.byteOrder, bos);
                } catch (IOException e) {
                    throw e;
                } catch (Throwable e) {
                    throw new RuntimeException("Decompression failed:", e);
                }
                if ((len & 1) != 0) bos.write(0);
                parser.parseDataset(parser.getDcmDecodeParam(), -1);
                ds.subSet(Tags.PixelData, -1).writeDataset(bos, encParam);
            } finally {
                bos.close();
            }
            return md.digest();
        } finally {
            try {
                fiis.close();
            } catch (IOException ignore) {
            }
        }
    }
