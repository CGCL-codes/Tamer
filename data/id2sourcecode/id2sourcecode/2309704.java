    public InputStream getResourceContent(String resourceUri, String methodName) throws ServiceAccessException, AccessDeniedException, ObjectNotFoundException {
        InputStream dataStream = null;
        InputStream inputStream = null;
        ByteArrayOutputStream bot = new ByteArrayOutputStream();
        ZipOutputStream sos = new ZipOutputStream(bot);
        sos.setMethod(ZipOutputStream.DEFLATED);
        Connection conn = null;
        try {
            Integer contentID = new Integer(0);
            DmsDocument dmsDocument = getMappingDocumentByUrl(resourceUri);
            checkDocumentRight(resourceUri, dmsDocument, null, methodName);
            Integer targetID = dmsDocument.getID();
            conn = DataSourceFactory.getConnection();
            DocumentRetrievalManager docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            DocumentOperationManager docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            AlertManager alertManager = new AlertManager(sessionContainer, conn);
            DmsContentManager dmsContentManager = new DmsContentManager(sessionContainer, conn);
            if (DmsDocument.COMPOUND_DOC_TYPE.equals(dmsDocument.getDocumentType())) {
                List list = docOperationManager.getSubDocumentByParentID(dmsDocument.getID());
                if (DmsDocument.COMPOUND_DOC_TYPE.equals(dmsDocument.getDocumentType())) {
                    list.add(dmsDocument);
                }
                if (!Utility.isEmpty(list)) {
                    for (int i = 0; i < list.size(); i++) {
                        DmsDocument document = (DmsDocument) list.get(i);
                        if ("L".equals(document.getDocumentType())) {
                            continue;
                        }
                        String fName = DocumentRetrievalManager.getEncodeStringByEncodeCode(document.getDocumentName(), SystemParameterFactory.getSystemParameter(SystemParameterConstant.DMS_ZIP_FILE_DEFAULT_ENCODING));
                        DmsVersion sVersion = docRetrievalManager.getTopVersionByDocumentID(document.getID());
                        contentID = sVersion.getContentID();
                        if (!GlobalConstant.RECORD_STATUS_INACTIVE.equals(document.getRecordStatus())) {
                            DmsContent docContent = docRetrievalManager.getContentByContentID(contentID);
                            ZipEntry theEntry = new ZipEntry(fName);
                            sos.putNextEntry(theEntry);
                            byte[] buffer = new byte[8192];
                            int length = -1;
                            inputStream = dmsContentManager.readDmsDocumentStoreContent(document, docContent);
                            while ((length = inputStream.read(buffer, 0, 8192)) != -1) {
                                sos.write(buffer, 0, length);
                            }
                        }
                    }
                }
                sos.flush();
                sos.close();
                dataStream = new DataInputStream(new ByteArrayInputStream(bot.toByteArray()));
            } else {
                String version = "TOP";
                MtmDocumentRelationshipDAObject docRelationshipDAO = new MtmDocumentRelationshipDAObject(sessionContainer, conn);
                DmsVersion dmsVersion = new DmsVersion();
                if (!Utility.isEmpty(version) && !DmsOperationConstant.DMS_TOP_VERSION.equals(version)) {
                    Integer versionID = TextUtility.parseIntegerObj(version);
                    dmsVersion = docRetrievalManager.getVersionByVersionID(versionID);
                    contentID = dmsVersion.getContentID();
                } else if (DmsOperationConstant.DMS_TOP_VERSION.equals(version)) {
                    dmsVersion = docRetrievalManager.getTopVersionByDocumentID(targetID);
                    contentID = dmsVersion.getContentID();
                }
                if (sessionContainer.getUserRecordID() != null && !methodName.equalsIgnoreCase("copy") && !methodName.equalsIgnoreCase("move")) {
                    AdapterMaster am = AdapterMasterFactory.getAdapterMaster(sessionContainer, conn);
                    DmsDocument parentDoc = docRetrievalManager.getDocument(dmsDocument.getParentID());
                    try {
                        am.call(UpdateAlert.DOCUMENT_TYPE, dmsDocument.getID(), UpdateAlert.VIEW_ACTION, dmsDocument.getDocumentName(), null, null, null);
                        am.call(UpdateAlert.DOCUMENT_TYPE, parentDoc.getID(), UpdateAlert.VIEW_ACTION, dmsDocument.getDocumentName(), null, null, null, dmsDocument.getID());
                        if (docRetrievalManager.hasRelationship(dmsDocument.getID(), null)) {
                            List relationList = docRelationshipDAO.getListByIDRelationType(dmsDocument.getID(), null);
                            List inRelationList = docRelationshipDAO.getListByRelationIDRelationType(dmsDocument.getID(), null);
                            List alertList = new ArrayList();
                            if (!Utility.isEmpty(relationList)) {
                                for (int i = 0; i < relationList.size(); i++) {
                                    alertList = alertManager.listUpdateAlertByObjectTypeObjectIDAndAction(UpdateAlert.DOCUMENT_TYPE, ((MtmDocumentRelationship) relationList.get(i)).getDocumentID(), UpdateAlert.MODIFY_RELATED_DOC);
                                    if (!Utility.isEmpty(alertList)) {
                                        am.call(UpdateAlert.DOCUMENT_TYPE, ((MtmDocumentRelationship) relationList.get(i)).getDocumentID(), UpdateAlert.MODIFY_RELATED_DOC, dmsDocument.getDocumentName(), null, null, null, dmsDocument.getID());
                                    }
                                }
                            }
                            if (!Utility.isEmpty(inRelationList)) {
                                for (int i = 0; i < inRelationList.size(); i++) {
                                    alertList = alertManager.listUpdateAlertByObjectTypeObjectIDAndAction(UpdateAlert.DOCUMENT_TYPE, ((MtmDocumentRelationship) inRelationList.get(i)).getRelatedDocumentID(), UpdateAlert.MODIFY_RELATED_DOC);
                                    if (!Utility.isEmpty(alertList)) {
                                        am.call(UpdateAlert.DOCUMENT_TYPE, ((MtmDocumentRelationship) inRelationList.get(i)).getRelatedDocumentID(), UpdateAlert.MODIFY_RELATED_DOC, dmsDocument.getDocumentName(), null, null, null, dmsDocument.getID());
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error(e, e);
                    }
                    AuditTrailManager auditManager = new AuditTrailManager(sessionContainer, conn);
                    auditManager.auditTrail(GlobalConstant.OBJECT_TYPE_DOCUMENT, dmsDocument, AuditTrailConstant.ACCESS_TYPE_VIEW, dmsVersion.getVersionNumber());
                }
                conn.commit();
                DmsDocument targetDoc = null;
                if (DmsDocument.DOCUMENT_LINK.equals(dmsDocument.getDocumentType())) {
                    Integer targetDocID = docRelationshipDAO.getTargetDocIDByRelatedDocID(dmsDocument.getID(), dmsDocument.getDocumentType());
                    targetDoc = docRetrievalManager.getDocument(targetDocID);
                    if (targetDoc != null) {
                        targetDoc.setDocumentName(dmsDocument.getDocumentName());
                        targetDoc.setDocumentType(dmsDocument.getDocumentType());
                        targetID = targetDocID;
                        dmsDocument = targetDoc;
                        dmsVersion = docRetrievalManager.getTopVersionByDocumentID(targetID);
                        contentID = dmsVersion.getContentID();
                    } else {
                        dmsDocument.setRecordStatus(GlobalConstant.RECORD_STATUS_INACTIVE);
                    }
                }
                DmsContent docContent = docRetrievalManager.getContentByContentID(contentID);
                dataStream = new BufferedInputStream(dmsContentManager.readDmsDocumentStoreContent(dmsDocument, docContent));
            }
            if (dataStream == null) {
                log.info("dataStream==null is true");
            }
            return dataStream;
        } catch (Exception e) {
            log.error(e);
            throw new ObjectNotFoundException(resourceUri);
        } finally {
            closeConnection(conn);
        }
    }
