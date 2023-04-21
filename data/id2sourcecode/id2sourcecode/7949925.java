    private void doCStore(ActiveAssociation assoc, Dimse rq, Command rspCmd) throws DcmServiceException, IOException {
        Command rqCmd = rq.getCommand();
        InputStream in = rq.getDataAsStream();
        if (spoolDir.isArchiveHighWater()) {
            in.close();
            throw new DcmServiceException(Status.OutOfResources);
        }
        String cuid = rqCmd.getAffectedSOPClassUID();
        String iuid = rqCmd.getAffectedSOPInstanceUID();
        String tsuid = rq.getTransferSyntaxUID();
        DcmDecodeParam decParam = DcmDecodeParam.valueOf(tsuid);
        String fileTS = decParam.encapsulated ? tsuid : UIDs.ExplicitVRLittleEndian;
        DcmEncodeParam encParam = DcmEncodeParam.valueOf(fileTS);
        DcmObjectFactory dof = DcmObjectFactory.getInstance();
        Dataset ds = dof.newDataset();
        DcmParserFactory pf = DcmParserFactory.getInstance();
        DcmParser parser = pf.newDcmParser(in);
        parser.setDcmHandler(ds.getDcmHandler());
        parser.parseDataset(decParam, Tags.PixelData);
        checkDataset(rqCmd, ds, parser);
        ds.setFileMetaInfo(dof.newFileMetaInfo(cuid, iuid, fileTS));
        File file = spoolDir.getInstanceFile(iuid);
        File md5file = MD5Utils.makeMD5File(file);
        try {
            spoolDir.delete(file);
            spoolDir.delete(md5file);
            log.info("M-WRITE " + file);
            MessageDigest digest = MessageDigest.getInstance("MD5");
            OutputStream out = new BufferedOutputStream(new DigestOutputStream(new FileOutputStream(file), digest));
            try {
                ds.writeFile(out, encParam);
                if (parser.getReadTag() == Tags.PixelData) {
                    writePixelData(in, encParam, ds, parser, out);
                    parser.parseDataset(decParam, -1);
                    ds.subSet(Tags.PixelData, -1).writeDataset(out, encParam);
                }
            } finally {
                try {
                    out.close();
                } catch (IOException ignore) {
                }
                spoolDir.register(file);
            }
            log.info("M-WRITE " + md5file);
            Writer md5out = new FileWriter(md5file);
            try {
                md5out.write(MD5Utils.toHexChars(digest.digest()));
            } finally {
                try {
                    md5out.close();
                } catch (IOException ignore) {
                }
                spoolDir.register(md5file);
            }
            Association a = assoc.getAssociation();
            String sourceAET = a.getCallingAET();
            if (emulateMediaCreationRequestForAET(sourceAET)) {
                File f = spoolDir.getEmulateRequestFile(sourceAET, ds.getString(Tags.PatientID), ds.getString(Tags.IssuerOfPatientID));
                boolean firstObject;
                if (firstObject = !f.exists()) {
                    File dir = f.getParentFile();
                    if (dir.mkdirs()) log.info("M-WRITE " + dir);
                }
                FileWriter fw = new FileWriter(f, true);
                try {
                    if (firstObject) {
                        fw.write(lookupMediaWriterName(a.getCalledAET()) + '\n');
                    }
                    fw.write(cuid + '\t' + iuid + '\n');
                } finally {
                    fw.close();
                    log.info((firstObject ? "M-WRITE " : "M-UPDATE ") + f);
                }
            }
        } catch (Throwable t) {
            log.error("Processing Failure during receive of instance[uid=" + iuid + "]:", t);
            spoolDir.delete(md5file);
            spoolDir.delete(file);
            throw new DcmServiceException(Status.ProcessingFailure, t);
        } finally {
            in.close();
        }
    }
