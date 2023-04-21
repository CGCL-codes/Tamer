    protected void doActualCStore(ActiveAssociation activeAssoc, Dimse rq, Command rspCmd, Dataset ds, DcmParser parser) throws IOException, DcmServiceException {
        File file = null;
        boolean dcm4cheeURIReferenced = rq.getTransferSyntaxUID().equals(UIDs.Dcm4cheURIReferenced);
        try {
            Command rqCmd = rq.getCommand();
            Association assoc = activeAssoc.getAssociation();
            String callingAET = assoc.getCallingAET();
            String calledAET = assoc.getCalledAET();
            String iuid = checkSOPInstanceUID(rqCmd, ds, callingAET);
            checkAppendPermission(assoc, ds);
            if (!checkOnlyWellKnownInstances(assoc, iuid, callingAET)) {
                log.info("StoreSCP only accepts well known instances from AE " + callingAET + " ! Ignored Instance:" + iuid);
                return;
            }
            List duplicates = new QueryFilesCmd(iuid).getFileDTOs();
            if (!(duplicates.isEmpty() || storeDuplicateIfDiffMD5 || storeDuplicateIfDiffHost && !containsLocal(duplicates))) {
                log.info("Received Instance[uid=" + iuid + "] already exists - ignored");
                return;
            }
            service.preProcess(ds);
            if (log.isDebugEnabled()) {
                log.debug("Dataset:\n");
                log.debug(ds);
            }
            perfMon.setProperty(activeAssoc, rq, PerfPropertyEnum.REQ_DATASET, ds);
            service.logDIMSE(assoc, STORE_XML, ds);
            if (isCheckIncorrectWorklistEntry() && checkIncorrectWorklistEntry(ds)) {
                log.info("Received Instance[uid=" + iuid + "] ignored! Reason: Incorrect Worklist entry selected!");
                return;
            }
            String retrieveAET;
            String availability;
            FileSystemDTO fsDTO = null;
            String filePath = null;
            byte[] md5sum = null;
            Dataset coerced = service.getCoercionAttributesFor(callingAET, STORE_XSL, ds, assoc);
            if (coerceBeforeWrite) {
                ds.setPrivateCreatorID(PrivateTags.CreatorID);
                ds.putAE(PrivateTags.CallingAET, callingAET);
                ds.putAE(PrivateTags.CalledAET, calledAET);
                ds.setPrivateCreatorID(null);
                if (coerced != null) {
                    service.coerceAttributes(ds, coerced);
                }
                service.postCoercionProcessing(ds);
            }
            if (dcm4cheeURIReferenced) {
                String uri = ds.getString(Tags.RetrieveURI);
                if (uri == null) {
                    retrieveAET = ds.getString(Tags.RetrieveAET);
                    availability = ds.getString(Tags.InstanceAvailability);
                    if (retrieveAET == null || availability == null) {
                        throw new DcmServiceException(Status.DataSetDoesNotMatchSOPClassError, "Missing (0040,E010) Retrieve URI - required for Dcm4che Retrieve URI Transfer Syntax");
                    }
                } else {
                    String[] selected = selectReferencedDirectoryURI(uri);
                    if (selected == null) {
                        throw new DcmServiceException(Status.DataSetDoesNotMatchSOPClassError, "(0040,E010) Retrieve URI: " + uri + " does not match with configured Referenced Directory Path: " + getReferencedDirectoryPath());
                    }
                    filePath = uri.substring(selected[1].length());
                    if (uri.startsWith("file:/")) {
                        file = new File(new URI(uri));
                        if (!file.isFile()) {
                            throw new DcmServiceException(Status.ProcessingFailure, "File referenced by (0040,E010) Retrieve URI: " + uri + " not found!");
                        }
                    }
                    fsDTO = getFileSystemMgt().getFileSystemOfGroup(refFileSystemGroupID, selected[0].startsWith("file:") ? new URI(selected[0]).getPath() : selected[0]);
                    retrieveAET = fsDTO.getRetrieveAET();
                    availability = Availability.toString(fsDTO.getAvailability());
                    if (file != null && readReferencedFile) {
                        log.info("M-READ " + file);
                        Dataset fileDS = objFact.newDataset();
                        FileInputStream fis = new FileInputStream(file);
                        try {
                            if (md5sumReferencedFile) {
                                MessageDigest digest = MessageDigest.getInstance("MD5");
                                DigestInputStream dis = new DigestInputStream(fis, digest);
                                BufferedInputStream bis = new BufferedInputStream(dis);
                                fileDS.readFile(bis, null, Tags.PixelData);
                                byte[] buf = getByteBuffer(assoc);
                                while (bis.read(buf) != -1) ;
                                md5sum = digest.digest();
                            } else {
                                BufferedInputStream bis = new BufferedInputStream(fis);
                                fileDS.readFile(bis, null, Tags.PixelData);
                            }
                        } finally {
                            fis.close();
                        }
                        fileDS.putAll(ds, Dataset.REPLACE_ITEMS);
                        ds = fileDS;
                    }
                }
                if (ds.getFileMetaInfo() == null) {
                    ds.setPrivateCreatorID(PrivateTags.CreatorID);
                    String tsuid = ds.getString(PrivateTags.Dcm4cheURIReferencedTransferSyntaxUID, UIDs.ImplicitVRLittleEndian);
                    ds.setPrivateCreatorID(null);
                    ds.setFileMetaInfo(objFact.newFileMetaInfo(rqCmd.getAffectedSOPClassUID(), rqCmd.getAffectedSOPInstanceUID(), tsuid));
                }
            } else {
                String fsgrpid = service.selectFileSystemGroup(callingAET, calledAET, ds);
                fsDTO = service.selectStorageFileSystem(fsgrpid);
                retrieveAET = fsDTO.getRetrieveAET();
                availability = Availability.toString(fsDTO.getAvailability());
                File baseDir = FileUtils.toFile(fsDTO.getDirectoryPath());
                file = makeFile(baseDir, ds, callingAET);
                filePath = file.getPath().substring(baseDir.getPath().length() + 1).replace(File.separatorChar, '/');
                CompressCmd compressCmd = null;
                if (parser.getReadTag() == Tags.PixelData && parser.getReadLength() != -1) {
                    compressCmd = compressionRules.getCompressFor(assoc, ds);
                    if (compressCmd != null) compressCmd.coerceDataset(ds);
                }
                ds.setFileMetaInfo(objFact.newFileMetaInfo(ds, compressCmd != null ? compressCmd.getTransferSyntaxUID() : rq.getTransferSyntaxUID()));
                perfMon.start(activeAssoc, rq, PerfCounterEnum.C_STORE_SCP_OBJ_STORE);
                perfMon.setProperty(activeAssoc, rq, PerfPropertyEnum.DICOM_FILE, file);
                md5sum = storeToFile(parser, ds, file, compressCmd, getByteBuffer(assoc));
                perfMon.stop(activeAssoc, rq, PerfCounterEnum.C_STORE_SCP_OBJ_STORE);
            }
            if (md5sum != null && ignoreDuplicate(duplicates, md5sum)) {
                log.info("Received Instance[uid=" + iuid + "] already exists - ignored");
                if (!dcm4cheeURIReferenced) {
                    deleteFailedStorage(file);
                }
                return;
            }
            ds.putAE(Tags.RetrieveAET, retrieveAET);
            if (!coerceBeforeWrite) {
                ds.setPrivateCreatorID(PrivateTags.CreatorID);
                ds.putAE(PrivateTags.CallingAET, callingAET);
                ds.putAE(PrivateTags.CalledAET, calledAET);
                ds.setPrivateCreatorID(null);
                if (coerced != null) {
                    service.coerceAttributes(ds, coerced);
                }
                service.postCoercionProcessing(ds);
            }
            checkPatientIdAndName(ds, callingAET);
            Storage store = getStorage(assoc);
            SeriesStored seriesStored = handleSeriesStored(assoc, store, ds);
            boolean newSeries = seriesStored == null;
            boolean newStudy = false;
            String seriuid = ds.getString(Tags.SeriesInstanceUID);
            if (newSeries) {
                Dataset mwlFilter = service.getCoercionAttributesFor(callingAET, STORE2MWL_XSL, ds, assoc);
                if (mwlFilter != null) {
                    coerced = merge(coerced, mergeMatchingMWLItem(assoc, ds, seriuid, mwlFilter));
                }
                if (!callingAET.equals(calledAET)) {
                    service.ignorePatientIDForUnscheduled(ds, Tags.RequestAttributesSeq, callingAET);
                    service.supplementIssuerOfPatientID(ds, assoc, callingAET, false);
                    service.supplementIssuerOfAccessionNumber(ds, assoc, callingAET, false);
                    service.supplementInstitutionalData(ds, assoc, callingAET);
                    service.generatePatientID(ds, ds, calledAET);
                }
                newStudy = !store.studyExists(ds.getString(Tags.StudyInstanceUID));
            }
            perfMon.start(activeAssoc, rq, PerfCounterEnum.C_STORE_SCP_OBJ_REGISTER_DB);
            long fileLength = file != null ? file.length() : 0L;
            long fspk = fsDTO != null ? fsDTO.getPk() : -1L;
            Dataset coercedElements;
            try {
                coercedElements = updateDB(store, ds, fspk, filePath, fileLength, md5sum, newSeries);
            } catch (NonUniquePatientIDException e) {
                service.coercePatientID(ds);
                coerced.putLO(Tags.PatientID, ds.getString(Tags.PatientID));
                coerced.putLO(Tags.IssuerOfPatientID, ds.getString(Tags.IssuerOfPatientID));
                coercedElements = updateDB(store, ds, fspk, filePath, fileLength, md5sum, newSeries);
            }
            if (newSeries) {
                seriesStored = initSeriesStored(ds, callingAET, retrieveAET);
                assoc.putProperty(SERIES_STORED, seriesStored);
                if (newStudy) {
                    service.sendNewStudyNotification(ds);
                }
            }
            appendInstanceToSeriesStored(seriesStored, ds, retrieveAET, availability);
            coerced = merge(coerced, coercedElements);
            try {
                logCoercion(ds, coerced);
            } catch (Exception e) {
                log.warn("Failed to generate audit log for attribute coercion:", e);
            }
            ds.putAll(coercedElements, Dataset.MERGE_ITEMS);
            perfMon.setProperty(activeAssoc, rq, PerfPropertyEnum.REQ_DATASET, ds);
            perfMon.stop(activeAssoc, rq, PerfCounterEnum.C_STORE_SCP_OBJ_REGISTER_DB);
            if (coerced.isEmpty() || !contains(coerceWarnCallingAETs, callingAET)) {
                rspCmd.putUS(Tags.Status, Status.Success);
            } else {
                int[] coercedTags = new int[coerced.size()];
                Iterator it = coerced.iterator();
                for (int i = 0; i < coercedTags.length; i++) {
                    coercedTags[i] = ((DcmElement) it.next()).tag();
                }
                rspCmd.putAT(Tags.OffendingElement, coercedTags);
                rspCmd.putUS(Tags.Status, Status.CoercionOfDataElements);
            }
            service.postProcess(ds);
        } catch (DcmServiceException e) {
            log.warn(e.getMessage(), e);
            if (!dcm4cheeURIReferenced) {
                deleteFailedStorage(file);
            }
            throw e;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            if (!dcm4cheeURIReferenced) {
                deleteFailedStorage(file);
            }
            throw new DcmServiceException(Status.ProcessingFailure, e);
        }
    }
