    public static byte[] compressFileJPEGLossy(File inFile, File outFile, int[] planarConfiguration, int[] pxdataVR, float quality, String derivationDescription, float estimatedCompressionRatio, float[] actualCompressionRatio, String iuid, String suid, byte[] buffer, Dataset ds, FileInfo fileInfo) throws Exception {
        if (suid != null && iuid == null) throw new IllegalArgumentException("New Series Instance UID requires new SOP Instance UID");
        log.info("M-READ file:" + inFile);
        InputStream in = new BufferedInputStream(new FileInputStream(inFile));
        try {
            DcmParser p = DcmParserFactory.getInstance().newDcmParser(in);
            if (ds == null) ds = DcmObjectFactory.getInstance().newDataset();
            p.setDcmHandler(ds.getDcmHandler());
            p.parseDcmFile(FileFormat.DICOM_FILE, Tags.PixelData);
            if (planarConfiguration != null && planarConfiguration.length != 0) planarConfiguration[0] = ds.getInt(Tags.PlanarConfiguration, 0);
            if (pxdataVR != null && pxdataVR.length != 0) pxdataVR[0] = p.getReadVR();
            if (fileInfo != null) {
                DatasetUtils.fromByteArray(fileInfo.patAttrs, DatasetUtils.fromByteArray(fileInfo.studyAttrs, DatasetUtils.fromByteArray(fileInfo.seriesAttrs, DatasetUtils.fromByteArray(fileInfo.instAttrs, ds))));
            }
            CompressCmd compressCmd = createJPEGLossyCompressCmd(ds, quality, derivationDescription, estimatedCompressionRatio, iuid, suid);
            compressCmd.coerceDataset(ds);
            String tsuid = compressCmd.getTransferSyntaxUID();
            FileMetaInfo fmi = DcmObjectFactory.getInstance().newFileMetaInfo(ds, tsuid);
            ds.setFileMetaInfo(fmi);
            log.info("M-WRITE file:" + outFile);
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestOutputStream dos = new DigestOutputStream(new FileOutputStream(outFile), md);
            BufferedOutputStream bos = new BufferedOutputStream(dos, buffer);
            try {
                DcmDecodeParam decParam = p.getDcmDecodeParam();
                DcmEncodeParam encParam = DcmEncodeParam.valueOf(tsuid);
                ds.writeFile(bos, encParam);
                ds.writeHeader(bos, encParam, Tags.PixelData, VRs.OB, -1);
                int read = compressCmd.compress(decParam.byteOrder, in, bos, actualCompressionRatio);
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
