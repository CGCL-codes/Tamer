    @Override
    public StepResults doProcessing(ProcessingErrorHandler processingErrorHandler) throws StepException {
        fireStepProcessingBeginningEvent(getDescription());
        logger.fine("Starting processing in " + this.getClass().getName());
        verifyStepState();
        if (Constants.PASSED_STATE.equals(qfRecord.getManifestReadStatus())) {
            throw new ProcessingException("Can not process step that has already passed successfully!");
        }
        if (Constants.FAILED_STATE.equals(qfRecord.getManifestReadStatus())) {
            throw new ProcessingException("Can not process step that has already passed successfully!");
        }
        if (manifestFileNameProperty != null && manifestFileNameProperty.length() != 0) {
            manifestFileName = manifestFileNameProperty;
        } else {
            manifestFileName = (String) properties.get(StepProperties.MANIFEST_FILE_NAME_PROPERTY_NAME);
        }
        if (manifestFileName == null || manifestFileName.length() == 0) {
            logger.severe("Manifest file name not set.");
            throw new ProcessingException("Manifest file name property has not been set!");
        }
        File manifestFile = new File(manifestFileName);
        if (!manifestFile.exists()) {
            String msg = "The location specified by the manifest file name (" + manifestFileName + ") does not exist.";
            logger.severe(msg);
            throw new ProcessingException(msg);
        }
        if (!manifestFile.isFile()) {
            String msg = "The location specified by the manifest file name (" + manifestFileName + ") is not a file.";
            logger.severe(msg);
            throw new ProcessingException(msg);
        }
        if (!manifestFile.canRead()) {
            String msg = "The file specified by the manifest file name (" + manifestFileName + ") can not be read.";
            logger.severe(msg);
            throw new ProcessingException(msg);
        }
        logger.fine("Manifest file name: " + manifestFileName);
        if (carrierLocationProperty != null && carrierLocationProperty.length() != 0) {
            carrierLocation = carrierLocationProperty;
        } else {
            carrierLocation = properties.getProperty(StepProperties.OUTPUT_CARRIER_LOCATION_PROPERTY_NAME);
        }
        File outputCarrier = new File(carrierLocation);
        if (!outputCarrier.exists() || !outputCarrier.canRead() || !outputCarrier.canWrite() || !outputCarrier.isDirectory()) {
            throw new CarrierNotFoundException();
        }
        if (!carrierLocation.equals(qfRecord.getCarrierDeviceLocation())) {
            dataAccessManager.beginTransaction();
            if (properties.get(StepProperties.OUTPUT_CARRIER_ID_PROPERTY_NAME) != null) {
                qfRecord.setCarrierDeviceId((String) properties.get(StepProperties.OUTPUT_CARRIER_ID_PROPERTY_NAME));
            }
            qfRecord.setCarrierDeviceLocation(carrierLocation);
            transferJobDAO.saveTransferJob(transferJob);
            dataAccessManager.commitTransaction();
        }
        if (carrierIdProperty != null && carrierIdProperty.length() != 0) {
            carrierId = carrierIdProperty;
        } else {
            carrierId = properties.getProperty(StepProperties.OUTPUT_CARRIER_ID_PROPERTY_NAME);
        }
        if (carrierId == null || carrierId.length() == 0) {
            String msg = "Carrier ID has not been set.";
            logger.severe(msg);
            throw new StepPropertiesValidationException(StepProperties.OUTPUT_CARRIER_ID_PROPERTY_NAME, msg);
        }
        if (!carrierId.equals(qfRecord.getCarrierDeviceId())) {
            dataAccessManager.beginTransaction();
            qfRecord.setCarrierDeviceId(carrierId);
            dataAccessManager.commitTransaction();
        }
        StepResults results = new StepResults();
        dataAccessManager.beginTransaction();
        qfRecord.setManifestReadStatus(Constants.IN_PROGRESS_STATE);
        transferJobDAO.saveTransferJob(transferJob);
        dataAccessManager.commitTransaction();
        try {
            String newJobDirName = transferJob.getTransferJobNumber().getPath();
            File newJobDir = new File(carrierLocation, newJobDirName);
            if (!newJobDir.exists() && !newJobDir.mkdirs()) {
                logger.severe("Could not create transfer job base directory");
                throw new ProcessingException("Could not create transfer job base directory");
            }
            File checkDir = new File(newJobDir, Constants.CHECK_DIR_NAME);
            if (!checkDir.exists() && !checkDir.mkdir()) {
                logger.severe("Could not create transfer job check directory");
                throw new ProcessingException("Could not create transfer job check directory");
            }
            File dataDir = new File(newJobDir, Constants.QF_DATA_DIR_NAME);
            if (!dataDir.exists() && !dataDir.mkdir()) {
                logger.severe("Could not create transfer job data directory");
                throw new ProcessingException("Could not create transfer job data directory");
            }
            File manifestOnCarrier = new File(checkDir, Constants.MANIFEST_FILENAME);
            try {
                FileUtils.fileCopy(manifestFile, manifestOnCarrier);
            } catch (IOException e) {
                logger.severe("Could not copy manifest file to carrier.");
                throw new ProcessingException("Could not copy manifest file to carrier", e);
            }
            String newManifestFileChecksum;
            try {
                newManifestFileChecksum = FileUtils.getChecksum(manifestFile, Constants.DEFAULT_CHECKSUM_ALGORITHM);
            } catch (IOException e) {
                logger.severe("Could not get checksum of manifest file on carrier.");
                throw new ProcessingException("Could not get checksum of manifest file on carrier.", e);
            }
            if (transferJob.getNumDataObjects() == 0) {
                List<Map<String, Object>> warnings;
                try {
                    dataAccessManager.beginTransaction();
                    warnings = processManifestFile(manifestFile);
                } catch (IOException e) {
                    logger.severe("Exception processing manifest file - IO error.");
                    throw new ProcessingException("Error processing manifest file.", e);
                }
                if (warnings.size() != 0) {
                    results.getErrorData().addAll(warnings);
                    results.setErrorCount(warnings.size());
                    results.setErrorsStored(warnings.size());
                    results.setErrorOccurred(true);
                }
            } else {
                if (!newManifestFileChecksum.equals(transferJob.getManifestFileChecksum())) {
                    logger.severe("Manifest file is corrupt of has been modified since previous processing attempt.");
                    throw new ProcessingException("Manifest File is corrupt or has been modified since previous processing attempt.");
                }
            }
            if (!results.isErrorOccurred()) {
                qfRecord.setManifestReadStatus(Constants.PASSED_STATE);
                qfRecord.setManifestReadBy(currentUser);
                qfRecord.setManifestReadDate(new Date());
                transferJob.setManifestFileChecksum(newManifestFileChecksum);
                transferJob.setManifestFileChecksumAlgorithm(Constants.DEFAULT_CHECKSUM_ALGORITHM);
                transferJob.setJobStatus(JobStatus.QF_INITIALISED_FROM_MANIFEST_PASSED);
                transferJobDAO.saveTransferJob(transferJob);
                dataAccessManager.commitTransaction();
                logger.fine("Processing Completed successfully");
                results.setResultsMessage(transferJob.getNumDataObjects() + " data objects successfully loaded from manifest.");
            } else {
                results.setResultsMessage(results.getErrorCount() + " errors when loading manifest file.");
                ProcessingErrorAction actionToTake = processingErrorHandler.determineAction(getStepName(), results);
                switch(actionToTake) {
                    case RESET:
                        logger.warning("Errors detected during processing, attempting to abort step.");
                        results.setStepReset(true);
                        abort();
                        break;
                    case STOP_PROCESSING:
                        logger.warning("Errors detected during processing, saving state and then stopping processing.");
                        failStep();
                        break;
                    case SAVE:
                        throw new IllegalStateException("This step cannot be saved in a state of error.");
                }
            }
        } catch (StepException stepEx) {
            abort();
            throw stepEx;
        }
        fireCompletedStepProcessingEvent(results);
        return results;
    }
