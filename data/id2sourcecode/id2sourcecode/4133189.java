    public NodeTransaction download(NodeTransaction transaction) {
        if (transaction == null) {
            throw new RuntimeException(NULL_TRANSACTION);
        }
        if (StringUtils.isBlank(transaction.getNetworkId())) {
            throw new RuntimeException("Null network transaction Id");
        }
        if (transaction.getFlow() == null || StringUtils.isBlank(transaction.getFlow().getName())) {
            throw new RuntimeException("Null data flow name");
        }
        try {
            String token = authenticate();
            logger.debug("token: " + token);
            File targetDir = new File(FilenameUtils.concat(tempDir.getAbsolutePath(), "Temp-" + System.currentTimeMillis()));
            logger.debug("creating temp dir: " + targetDir);
            FileUtils.forceMkdir(targetDir);
            if (!targetDir.exists()) {
                throw new RuntimeException("Unable to create temp dir: " + targetDir);
            }
            Download downloadReq = new Download();
            downloadReq.setDataflow(new NCName(transaction.getFlow().getName()));
            downloadReq.setSecurityToken(token);
            downloadReq.setTransactionId(transaction.getNetworkId());
            if (transaction.getDocuments() != null) {
                for (int d = 0; d < transaction.getDocuments().size(); d++) {
                    Document wnosDoc = (Document) transaction.getDocuments().get(d);
                    NodeDocumentType reqDoc = new NodeDocumentType();
                    logger.debug("Setting document format type from: " + wnosDoc.getType());
                    reqDoc.setDocumentFormat(DocumentFormatType.Factory.fromValue(wnosDoc.getType().getName()));
                    if (StringUtils.isNotBlank(wnosDoc.getDocumentId())) {
                        reqDoc.setDocumentId(new Id(wnosDoc.getDocumentId()));
                        reqDoc.setDocumentName("");
                    } else if (StringUtils.isNotBlank(wnosDoc.getDocumentName())) {
                        reqDoc.setDocumentName(wnosDoc.getDocumentName());
                    } else {
                        throw new RuntimeException("Document Id or Name required");
                    }
                    downloadReq.addDocuments(reqDoc);
                }
            }
            logger.debug("Downloading...");
            NetworkNode2Stub stub = getStub("Download");
            stub._getServiceClient().getOptions().setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
            DownloadResponse downloadResp = stub.Download(downloadReq);
            if (downloadResp == null) {
                throw new RuntimeException("Null DownloadResponse");
            }
            transaction.getDocuments().clear();
            logger.debug("Spooling files...");
            for (int i = 0; i < downloadResp.getDocuments().length; i++) {
                NodeDocumentType nodeDoc = downloadResp.getDocuments()[i];
                File targetFile = new File(FilenameUtils.concat(targetDir.getAbsolutePath(), nodeDoc.getDocumentName()));
                logger.debug("Target : " + targetFile);
                DataHandler docHandler = nodeDoc.getDocumentContent().getBase64Binary();
                FileOutputStream dest = new FileOutputStream(targetFile.getAbsoluteFile());
                docHandler.writeTo(dest);
                dest.flush();
                dest.close();
                if (!targetFile.exists()) {
                    throw new RuntimeException("Null result file: " + targetFile);
                }
                Document doc = new Document();
                FileInputStream decodedStream = new FileInputStream(targetFile);
                doc.setContent(FileUtils.readFileToByteArray(targetFile));
                decodedStream.close();
                if (nodeDoc.getDocumentId() != null && StringUtils.isNotBlank(nodeDoc.getDocumentId().toString())) {
                    doc.setDocumentId(nodeDoc.getDocumentId().toString());
                }
                doc.setDocumentName(nodeDoc.getDocumentName());
                doc.setDocumentStatus(CommonTransactionStatusCode.Received);
                doc.setDocumentStatusDetail("Document downloaded");
                if (nodeDoc.getDocumentFormat() != null && StringUtils.isNotBlank(nodeDoc.getDocumentFormat().getValue())) {
                    doc.setType((CommonContentType) CommonContentType.getEnumMap().get(nodeDoc.getDocumentFormat().getValue()));
                }
                transaction.getDocuments().add(doc);
            }
            TransactionStatus status = new TransactionStatus(transaction.getNetworkId());
            status.setDescription("Notification performed");
            status.setStatus(CommonTransactionStatusCode.Unknown);
            transaction.setStatus(status);
            return transaction;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException("Error while downloading from: " + partnerEndpointUrl + " using transaction Id: " + transaction.getId() + appendExceptionDetail(ex));
        }
    }
