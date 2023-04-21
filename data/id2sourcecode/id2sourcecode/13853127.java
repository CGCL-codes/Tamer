    private void upgradeToEMF(SeriesStored seriesStored) throws Exception {
        Dataset ian = seriesStored.getIAN();
        Dataset refSeriesSeq = ian.getItem(Tags.RefSeriesSeq);
        DcmElement refSOPSeq = refSeriesSeq.get(Tags.RefSOPSeq);
        int numFrames = refSOPSeq.countItems();
        EnhancedMFBuilder builder = newEMFBuilder(seriesStored, numFrames);
        File[] files = new File[numFrames];
        for (int i = 0; i < numFrames; i++) {
            Dataset refSOP = refSOPSeq.getItem(i);
            String iuid = refSOP.getString(Tags.RefSOPInstanceUID);
            builder.add(files[i] = locateInstance(iuid));
        }
        Dataset mfds = builder.build();
        if (mergePatientStudySeriesAttributesFromDB) {
            mfds.putAll(seriesStored.getSeriesAttrs().exclude(SERIES_IUID));
            mfds.putAll(seriesStored.getStudyAttrs());
            mfds.putAll(seriesStored.getPatientAttrs());
        }
        String tsUID = mfds.getFileMetaInfo().getTransferSyntaxUID();
        FileDTO fileDTO = makeFile(mfds);
        File mffile = FileUtils.toFile(fileDTO.getDirectoryPath(), fileDTO.getFilePath());
        boolean deleteFile = true;
        try {
            log.info("M-WRITE file:" + mffile);
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestOutputStream dos = new DigestOutputStream(new FileOutputStream(mffile), md);
            BufferedOutputStream out = new BufferedOutputStream(dos, new byte[bufferSize]);
            try {
                DcmEncodeParam encParam = DcmEncodeParam.valueOf(tsUID);
                mfds.writeFile(out, encParam);
                if (!noPixelData) {
                    mfds.writeHeader(out, encParam, Tags.PixelData, builder.getPixelDataVR(), builder.getPixelDataLength());
                    if (encParam.encapsulated) {
                        mfds.writeHeader(out, encParam, Tags.Item, VRs.NONE, 0);
                    }
                    for (int i = 0; i < numFrames; i++) {
                        long off = builder.getPixelDataOffset(i);
                        int len = builder.getPixelDataLength(i);
                        if (encParam.encapsulated) {
                            mfds.writeHeader(out, encParam, Tags.Item, VRs.NONE, len);
                        }
                        FileInputStream in = new FileInputStream(files[i]);
                        try {
                            while (off > 0) {
                                off -= in.skip(off);
                            }
                            out.copyFrom(in, len);
                        } finally {
                            in.close();
                        }
                    }
                    if (encParam.encapsulated) {
                        mfds.writeHeader(out, encParam, Tags.SeqDelimitationItem, VRs.NONE, 0);
                    }
                }
            } finally {
                out.close();
            }
            fileDTO.setFileMd5(md.digest());
            fileDTO.setFileSize(mffile.length());
            fileDTO.setFileTsuid(tsUID);
            importFile(fileDTO, mfds);
            deleteFile = false;
        } finally {
            if (deleteFile) {
                log.info("M-DELETE file:" + mffile);
                if (!mffile.delete()) {
                    log.error("Failed to delete " + mffile);
                }
            }
        }
    }
