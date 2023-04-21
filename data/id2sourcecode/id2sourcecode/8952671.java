    private byte[] storeToFile(DcmParser parser, Dataset ds, final File file, CompressCmd compressCmd, byte[] buffer) throws Exception {
        log.info("M-WRITE file:" + file);
        MessageDigest md = null;
        BufferedOutputStream bos = null;
        FileOutputStream fos = new FileOutputStream(file);
        if (service.isMd5sum()) {
            md = MessageDigest.getInstance("MD5");
            DigestOutputStream dos = new DigestOutputStream(fos, md);
            bos = new BufferedOutputStream(dos, buffer);
        } else {
            bos = new BufferedOutputStream(fos, buffer);
        }
        try {
            DcmDecodeParam decParam = parser.getDcmDecodeParam();
            String tsuid = ds.getFileMetaInfo().getTransferSyntaxUID();
            DcmEncodeParam encParam = DcmEncodeParam.valueOf(tsuid);
            ds.writeFile(bos, encParam);
            if (parser.getReadTag() == Tags.PixelData) {
                int len = parser.getReadLength();
                InputStream in = parser.getInputStream();
                if (encParam.encapsulated) {
                    ds.writeHeader(bos, encParam, Tags.PixelData, VRs.OB, -1);
                    if (decParam.encapsulated) {
                        parser.parseHeader();
                        while (parser.getReadTag() == Tags.Item) {
                            len = parser.getReadLength();
                            ds.writeHeader(bos, encParam, Tags.Item, VRs.NONE, len);
                            bos.copyFrom(in, len);
                            parser.parseHeader();
                        }
                    } else {
                        int read = compressCmd.compress(decParam.byteOrder, parser.getInputStream(), bos, null);
                        skipFully(in, parser.getReadLength() - read);
                    }
                    ds.writeHeader(bos, encParam, Tags.SeqDelimitationItem, VRs.NONE, 0);
                } else {
                    ds.writeHeader(bos, encParam, Tags.PixelData, parser.getReadVR(), len);
                    bos.copyFrom(in, len);
                }
                parser.parseDataset(decParam, -1);
                ds.subSet(Tags.PixelData, -1).writeDataset(bos, encParam);
            }
            bos.flush();
            if (service.isSyncFileBeforeCStoreRSP()) {
                fos.getFD().sync();
            } else if (service.isSyncFileAfterCStoreRSP()) {
                final FileOutputStream fos2 = fos;
                syncFileExecutor().execute(new Runnable() {

                    public void run() {
                        try {
                            fos2.getFD().sync();
                        } catch (Exception e) {
                            log.error("sync of " + file + " failed:", e);
                        } finally {
                            try {
                                fos2.close();
                            } catch (Exception ignore) {
                            }
                        }
                    }
                });
                fos = null;
            }
        } finally {
            if (fos != null) try {
                fos.close();
            } catch (Exception ignore) {
            }
        }
        return md != null ? md.digest() : null;
    }
