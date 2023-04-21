    public Boolean analyze(SessionContainer sessionContainer, Connection conn, MaintDmsDocumentForm docForm, java.io.InputStream infile) throws ApplicationException {
        Boolean analyzedResult = new Boolean(true);
        String createFolderDateFormat = SystemParameterFactory.getSystemParameter(SystemParameterConstant.DMS_SCAN_FOLDER_DATE_FORMAT);
        SimpleDateFormat dateFolderFormat = new SimpleDateFormat(createFolderDateFormat, Utility.getLocaleByString(SystemParameterFactory.getSystemParameter(SystemParameterConstant.LOCALE)));
        DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        org.w3c.dom.Document indexFile = null;
        try {
            docBuilder = dfactory.newDocumentBuilder();
            indexFile = docBuilder.parse(infile);
        } catch (Exception e) {
            log.error(e, e);
        }
        fileExten = docForm.getTrueFileName();
        this.documentName = this.resolveDocumentName(indexFile);
        this.description = this.resolverDocumentInfo(indexFile, DmsDocumentAnalyzer.DESCRIPTION_TAG);
        this.referenceNo = this.resolverDocumentInfo(indexFile, DmsDocumentAnalyzer.REFERENCE_NUM_TAG);
        this.userDef1 = this.resolverDocumentInfo(indexFile, DmsDocumentAnalyzer.USER_DEF_1);
        this.userDef2 = this.resolverDocumentInfo(indexFile, DmsDocumentAnalyzer.USER_DEF_2);
        this.userDef3 = this.resolverDocumentInfo(indexFile, DmsDocumentAnalyzer.USER_DEF_3);
        this.userDef4 = this.resolverDocumentInfo(indexFile, DmsDocumentAnalyzer.USER_DEF_4);
        this.userDef5 = this.resolverDocumentInfo(indexFile, DmsDocumentAnalyzer.USER_DEF_5);
        this.userDef6 = this.resolverDocumentInfo(indexFile, DmsDocumentAnalyzer.USER_DEF_6);
        this.userDef7 = this.resolverDocumentInfo(indexFile, DmsDocumentAnalyzer.USER_DEF_7);
        this.userDef8 = this.resolverDocumentInfo(indexFile, DmsDocumentAnalyzer.USER_DEF_8);
        this.userDef9 = this.resolverDocumentInfo(indexFile, DmsDocumentAnalyzer.USER_DEF_9);
        this.userDef10 = this.resolverDocumentInfo(indexFile, DmsDocumentAnalyzer.USER_DEF_10);
        this.fullText = this.resolverFullText(indexFile);
        this.workflowRecordID = this.resolverDocumentInfo(indexFile, DmsDocumentAnalyzer.WORKFLOW_RECORD_ID);
        String scanDefaultWorkflowRecordID = SystemParameterFactory.getSystemParameter(SystemParameterConstant.DMS_SCAN_DEFAULT_WORKFLOW_RECORD_ID);
        if (!Utility.isEmpty(scanDefaultWorkflowRecordID)) {
            this.workflowRecordID = scanDefaultWorkflowRecordID;
        }
        if (!Utility.isEmpty(this.workflowRecordID)) {
            this.submitSystemWorkflow = GlobalConstant.TRUE;
        }
        if (!"0".equals(this.workflowRecordID)) {
            DocumentOperationManager docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            DocumentRetrievalManager docReterialManager = new DocumentRetrievalManager(sessionContainer, conn);
            PermissionManager permissionManager = sessionContainer.getPermissionManager();
            DmsDocument dmsDocument = (DmsDocument) docForm.getFormData();
            if (!"Y".equals(docForm.getScanSupportFolderStructure())) {
                if (!Utility.isEmpty(resolverSingleDocumentInfoByTagAttribute(indexFile, DmsDocumentAnalyzer.DESCRIPTOR, DmsDocumentAnalyzer.PARENT_ID))) {
                    dmsDocument.setParentID(TextUtility.parseIntegerObj(resolverSingleDocumentInfoByTagAttribute(indexFile, DmsDocumentAnalyzer.DESCRIPTOR, DmsDocumentAnalyzer.PARENT_ID)));
                }
                if (!Utility.isEmpty(resolverSingleDocumentInfoByTagAttribute(indexFile, DmsDocumentAnalyzer.DESCRIPTOR, DmsDocumentAnalyzer.ROOT_ID))) {
                    dmsDocument.setRootID(TextUtility.parseIntegerObj(resolverSingleDocumentInfoByTagAttribute(indexFile, DmsDocumentAnalyzer.DESCRIPTOR, DmsDocumentAnalyzer.ROOT_ID)));
                }
            }
            String documentProfileName = this.getDocumentProfileName(indexFile);
            com.dcivision.framework.dao.SysUserDefinedIndexDAObject sysUDFDAO = new com.dcivision.framework.dao.SysUserDefinedIndexDAObject(sessionContainer, conn);
            com.dcivision.framework.bean.SysUserDefinedIndex udfObj = (com.dcivision.framework.bean.SysUserDefinedIndex) sysUDFDAO.getObjectByUserDefinedType(documentProfileName);
            String folderName = "";
            String createFolderMethod = SystemParameterFactory.getSystemParameter(SystemParameterConstant.DMS_SCAN_FOLDER_CREATION_FORMAT);
            if (udfObj != null && !Utility.isEmpty(udfObj.getDmsScanFolderCreationType())) {
                createFolderMethod = udfObj.getDmsScanFolderCreationType();
            }
            if ("true".equals(docForm.getErrorScanFormat())) {
                createFolderMethod = DmsOperationConstant.SCAN_FOLDER_CREATE_BY_LOGINNAME;
            }
            if (DmsOperationConstant.SCAN_FOLDER_CREATION_BY_UDF.equals(createFolderMethod) || DmsOperationConstant.SCAN_FOLDER_CREATION_BY_UDF_BY_DATE.equals(createFolderMethod)) {
                folderName = this.resolverFolderName(indexFile);
            } else if (DmsOperationConstant.SCAN_FOLDER_CREATION_BY_UDF_VALUE.equals(createFolderMethod) || DmsOperationConstant.SCAN_FOLDER_CREATION_BY_UDF_VALUE_BY_DATE.equals(createFolderMethod)) {
                folderName = this.resolverFolderNameUsingUDFValue(indexFile);
                if ("".equals(folderName)) {
                    folderName = dateFolderFormat.format(new Date());
                }
            } else if (DmsOperationConstant.SCAN_FOLDER_CREATION_BY_UDF_VALUE_BY_UDF_BY_DATE.equals(createFolderMethod)) {
                folderName = this.resolverFolderNameUsingUDFValue(indexFile);
                if ("".equals(folderName)) {
                    folderName = dateFolderFormat.format(new Date());
                }
            } else if (DmsOperationConstant.SCAN_FOLDER_CREATION_BY_DATE.equals(createFolderMethod)) {
                folderName = dateFolderFormat.format(new Date());
            } else if (DmsOperationConstant.SCAN_FOLDER_CREATION_BY_DPF_NAME.equals(createFolderMethod)) {
                Integer defaultProfileID = SystemParameterFactory.getSystemParameterInteger(SystemParameterConstant.DMS_SCAN_FOLDER_CREATION_DPF_ID);
                String tagName = "user_def_" + defaultProfileID;
                if (!Utility.isEmpty(resolverDocumentInfo(indexFile, tagName))) {
                    folderName = resolverDocumentInfo(indexFile, tagName);
                }
                if ("".equals(folderName)) {
                    folderName = dateFolderFormat.format(new Date());
                }
            } else if ("SCAN.FOLDER_DEMO_DP1_DATE".equals(createFolderMethod)) {
                folderName = this.userDef1;
            } else if ("SCAN.FOLDER_DEMO_DP1_DP2_DATE".equals(createFolderMethod)) {
                folderName = this.userDef1;
            } else if (DmsOperationConstant.SCAN_FOLDER_CREATE_BY_LOGINNAME.equals(createFolderMethod)) {
                folderName = sessionContainer.getUserRecord().getLoginName();
            }
            if (udfObj != null && !Utility.isEmpty(udfObj.getDmsParentID()) && !DmsOperationConstant.SCAN_FOLDER_CREATE_BY_LOGINNAME.equals(createFolderMethod)) {
                dmsDocument.setParentID(udfObj.getDmsParentID());
                dmsDocument.setRootID(udfObj.getDmsRootID());
            }
            dmsDocument.setDocumentName(folderName);
            dmsDocument.setDocumentType(DmsDocument.FOLDER_TYPE);
            dmsDocument.setRecordStatus(GlobalConstant.STATUS_ACTIVE);
            dmsDocument.setItemStatus(DmsVersion.AVAILABLE_STATUS);
            dmsDocument.setCreateType(DmsOperationConstant.DMS_CREATE_BY_UPLOAD);
            DmsDocument newParentDocument = docReterialManager.getDocument(dmsDocument.getParentID());
            DmsDocument parentDocument = docReterialManager.getDocumentByNameParentID(folderName, dmsDocument.getParentID());
            String permission = permissionManager.getPermission(GlobalConstant.OBJECT_TYPE_DOCUMENT, dmsDocument.getParentID());
            boolean createFolderBypassSecurity = SystemParameterFactory.getSystemParameterBoolean(SystemParameterConstant.DMS_SCAN_FOLDER_CREATION_BYPASS_SECURITY);
            boolean canCreateFolderFlag = false;
            if (!createFolderBypassSecurity && permission.indexOf("F") < 0) {
                canCreateFolderFlag = false;
            } else {
                canCreateFolderFlag = true;
            }
            if (DmsOperationConstant.SCAN_FOLDER_CREATION_BY_UDF.equals(createFolderMethod) || DmsOperationConstant.SCAN_FOLDER_CREATION_BY_UDF_VALUE.equals(createFolderMethod) || DmsOperationConstant.SCAN_FOLDER_CREATION_BY_DATE.equals(createFolderMethod) || DmsOperationConstant.SCAN_FOLDER_CREATION_BY_DPF_NAME.equals(createFolderMethod) || DmsOperationConstant.SCAN_FOLDER_CREATE_BY_LOGINNAME.equals(createFolderMethod)) {
                if (parentDocument == null) {
                    if (!Utility.isEmpty(folderName) && DmsDocument.FOLDER_TYPE.equals(newParentDocument.getDocumentType())) {
                        if (canCreateFolderFlag) {
                            parentDocument = docOperationManager.createFolder(dmsDocument);
                        } else {
                            throw new ApplicationException(DmsErrorConstant.NO_RIGHT_CREATE_FOLDER);
                        }
                    } else {
                        parentDocument = docReterialManager.getDocument(dmsDocument.getParentID());
                    }
                }
                this.parentID = parentDocument.getID();
                this.rootID = parentDocument.getRootID();
            } else if (DmsOperationConstant.SCAN_FOLDER_CREATION_BY_UDF_BY_DATE.equals(createFolderMethod)) {
                String DateFolder = dateFolderFormat.format(new Date());
                if (parentDocument == null) {
                    if (!Utility.isEmpty(folderName) && DmsDocument.FOLDER_TYPE.equals(newParentDocument.getDocumentType())) {
                        if (canCreateFolderFlag) {
                            parentDocument = docOperationManager.createFolder(dmsDocument);
                        } else {
                            throw new ApplicationException(DmsErrorConstant.NO_RIGHT_CREATE_FOLDER);
                        }
                    } else {
                        parentDocument = docReterialManager.getDocument(dmsDocument.getParentID());
                    }
                }
                DmsDocument dateFolder = docReterialManager.getDocumentByNameParentID(DateFolder, parentDocument.getID());
                if (dateFolder == null && DmsDocument.FOLDER_TYPE.equals(parentDocument.getDocumentType())) {
                    dateFolder = (DmsDocument) docForm.getFormData();
                    dateFolder.setDocumentName(DateFolder);
                    dateFolder.setParentID(parentDocument.getID());
                    dateFolder.setRootID(parentDocument.getRootID());
                    dateFolder.setDocumentType(DmsDocument.FOLDER_TYPE);
                    dateFolder.setRecordStatus(GlobalConstant.STATUS_ACTIVE);
                    dateFolder.setItemStatus(DmsVersion.AVAILABLE_STATUS);
                    dateFolder.setCreateType(DmsOperationConstant.DMS_CREATE_BY_UPLOAD);
                    dateFolder = docOperationManager.createFolder(dateFolder);
                }
                if (dateFolder != null) {
                    this.parentID = dateFolder.getID();
                    this.rootID = dateFolder.getRootID();
                    log.debug(" New Parent ID = " + parentID + " rootID = " + rootID);
                } else {
                    this.parentID = parentDocument.getID();
                    this.rootID = parentDocument.getRootID();
                }
            } else if (DmsOperationConstant.SCAN_FOLDER_CREATION_BY_UDF_VALUE_BY_UDF_BY_DATE.equals(createFolderMethod)) {
                String DateFolder = dateFolderFormat.format(new Date());
                if (parentDocument == null || parentDocument.getID() == null) {
                    if (!Utility.isEmpty(folderName) && DmsDocument.FOLDER_TYPE.equals(newParentDocument.getDocumentType())) {
                        if (canCreateFolderFlag) {
                            parentDocument = docOperationManager.createFolder(dmsDocument);
                        } else {
                            throw new ApplicationException(DmsErrorConstant.NO_RIGHT_CREATE_FOLDER);
                        }
                    } else {
                        parentDocument = docReterialManager.getDocument(dmsDocument.getParentID());
                    }
                }
                DmsDocument udfFolder = docReterialManager.getDocumentByNameParentID(this.resolverFolderName(indexFile), parentDocument.getID());
                if (udfFolder == null || udfFolder.getID() == null && DmsDocument.FOLDER_TYPE.equals(parentDocument.getDocumentType())) {
                    udfFolder = (DmsDocument) docForm.getFormData();
                    udfFolder.setDocumentName(this.resolverFolderName(indexFile));
                    udfFolder.setParentID(parentDocument.getID());
                    udfFolder.setRootID(parentDocument.getRootID());
                    udfFolder.setDocumentType(DmsDocument.FOLDER_TYPE);
                    udfFolder.setRecordStatus(GlobalConstant.STATUS_ACTIVE);
                    udfFolder.setItemStatus(DmsVersion.AVAILABLE_STATUS);
                    udfFolder.setCreateType(DmsOperationConstant.DMS_CREATE_BY_UPLOAD);
                    if (canCreateFolderFlag) {
                        udfFolder = docOperationManager.createFolder(udfFolder);
                    } else {
                        throw new ApplicationException(DmsErrorConstant.NO_RIGHT_CREATE_FOLDER);
                    }
                }
                DmsDocument dateFolder = docReterialManager.getDocumentByNameParentID(DateFolder, udfFolder.getID());
                if (dateFolder == null || dateFolder.getID() == null && DmsDocument.FOLDER_TYPE.equals(udfFolder.getDocumentType())) {
                    dateFolder = (DmsDocument) docForm.getFormData();
                    dateFolder.setDocumentName(DateFolder);
                    dateFolder.setParentID(udfFolder.getID());
                    dateFolder.setRootID(udfFolder.getRootID());
                    dateFolder.setDocumentType(DmsDocument.FOLDER_TYPE);
                    dateFolder.setRecordStatus(GlobalConstant.STATUS_ACTIVE);
                    dateFolder.setItemStatus(DmsVersion.AVAILABLE_STATUS);
                    dateFolder.setCreateType(DmsOperationConstant.DMS_CREATE_BY_UPLOAD);
                    if (canCreateFolderFlag) {
                        dateFolder = docOperationManager.createFolder(dateFolder);
                    } else {
                        throw new ApplicationException(DmsErrorConstant.NO_RIGHT_CREATE_FOLDER);
                    }
                }
                if (dateFolder != null) {
                    this.parentID = dateFolder.getID();
                    this.rootID = dateFolder.getRootID();
                } else {
                    this.parentID = parentDocument.getID();
                    this.rootID = parentDocument.getRootID();
                }
            } else if (DmsOperationConstant.SCAN_FOLDER_CREATION_BY_UDF_VALUE_BY_DATE.equals(createFolderMethod)) {
                String DateFolder = dateFolderFormat.format(new Date());
                if (parentDocument == null || parentDocument.getID() == null) {
                    if (!Utility.isEmpty(folderName) && DmsDocument.FOLDER_TYPE.equals(newParentDocument.getDocumentType())) {
                        if (canCreateFolderFlag) {
                            parentDocument = docOperationManager.createFolder(dmsDocument);
                        } else {
                            throw new ApplicationException(DmsErrorConstant.NO_RIGHT_CREATE_FOLDER);
                        }
                    } else {
                        parentDocument = docReterialManager.getDocument(dmsDocument.getParentID());
                    }
                }
                DmsDocument udfFolder = parentDocument;
                DmsDocument dateFolder = docReterialManager.getDocumentByNameParentID(DateFolder, udfFolder.getID());
                if (dateFolder == null || dateFolder.getID() == null && DmsDocument.FOLDER_TYPE.equals(udfFolder.getDocumentType())) {
                    dateFolder = (DmsDocument) docForm.getFormData();
                    dateFolder.setDocumentName(DateFolder);
                    dateFolder.setParentID(udfFolder.getID());
                    dateFolder.setRootID(udfFolder.getRootID());
                    dateFolder.setDocumentType(DmsDocument.FOLDER_TYPE);
                    dateFolder.setRecordStatus(GlobalConstant.STATUS_ACTIVE);
                    dateFolder.setItemStatus(DmsVersion.AVAILABLE_STATUS);
                    dateFolder.setCreateType(DmsOperationConstant.DMS_CREATE_BY_UPLOAD);
                    if (canCreateFolderFlag) {
                        dateFolder = docOperationManager.createFolder(dateFolder);
                    } else {
                        throw new ApplicationException(DmsErrorConstant.NO_RIGHT_CREATE_FOLDER);
                    }
                }
                if (dateFolder != null) {
                    this.parentID = dateFolder.getID();
                    this.rootID = dateFolder.getRootID();
                } else {
                    this.parentID = parentDocument.getID();
                    this.rootID = parentDocument.getRootID();
                }
            } else if (DmsOperationConstant.SCAN_FOLDER_DEMO_DP1_DATE.equals(createFolderMethod)) {
                String DateFolder = dateFolderFormat.format(new Date());
                if (parentDocument == null) {
                    if (!Utility.isEmpty(folderName) && DmsDocument.FOLDER_TYPE.equals(newParentDocument.getDocumentType())) {
                        if (canCreateFolderFlag) {
                            parentDocument = docOperationManager.createFolder(dmsDocument);
                        } else {
                            throw new ApplicationException(DmsErrorConstant.NO_RIGHT_CREATE_FOLDER);
                        }
                    } else {
                        parentDocument = docReterialManager.getDocument(dmsDocument.getParentID());
                    }
                }
                DmsDocument dateFolder = docReterialManager.getDocumentByNameParentID(DateFolder, parentDocument.getID());
                if (dateFolder == null && DmsDocument.FOLDER_TYPE.equals(parentDocument.getDocumentType())) {
                    dateFolder = (DmsDocument) docForm.getFormData();
                    dateFolder.setDocumentName(DateFolder);
                    dateFolder.setParentID(parentDocument.getID());
                    dateFolder.setRootID(parentDocument.getRootID());
                    dateFolder.setDocumentType(DmsDocument.FOLDER_TYPE);
                    dateFolder.setRecordStatus(GlobalConstant.STATUS_ACTIVE);
                    dateFolder.setItemStatus(DmsVersion.AVAILABLE_STATUS);
                    dateFolder.setCreateType(DmsOperationConstant.DMS_CREATE_BY_UPLOAD);
                    if (canCreateFolderFlag) {
                        dateFolder = docOperationManager.createFolder(dateFolder);
                    } else {
                        throw new ApplicationException(DmsErrorConstant.NO_RIGHT_CREATE_FOLDER);
                    }
                }
                if (dateFolder != null) {
                    this.parentID = dateFolder.getID();
                    this.rootID = dateFolder.getRootID();
                    log.debug(" New Parent ID = " + parentID + " rootID = " + rootID);
                } else {
                    this.parentID = parentDocument.getID();
                    this.rootID = parentDocument.getRootID();
                }
            } else if (DmsOperationConstant.SCAN_FOLDER_DEMO_DP1_DP2_DATE.equals(createFolderMethod)) {
                String DateFolder = dateFolderFormat.format(new Date());
                if (parentDocument == null) {
                    if (!Utility.isEmpty(folderName) && DmsDocument.FOLDER_TYPE.equals(newParentDocument.getDocumentType())) {
                        if (canCreateFolderFlag) {
                            parentDocument = docOperationManager.createFolder(dmsDocument);
                        } else {
                            throw new ApplicationException(DmsErrorConstant.NO_RIGHT_CREATE_FOLDER);
                        }
                    } else {
                        parentDocument = docReterialManager.getDocument(dmsDocument.getParentID());
                    }
                }
                DmsDefaultProfileSettingDAObject dmsDefaultProfileDAO = new DmsDefaultProfileSettingDAObject(sessionContainer, conn);
                DmsDefaultProfileSetting defaultProfile = (DmsDefaultProfileSetting) dmsDefaultProfileDAO.getObjectByID(new Integer(2));
                DmsDocument dpfFolder = docReterialManager.getDocumentByNameParentID(this.userDef2, parentDocument.getID());
                if (dpfFolder == null || dpfFolder.getID() == null && DmsDocument.FOLDER_TYPE.equals(parentDocument.getDocumentType())) {
                    dpfFolder = (DmsDocument) docForm.getFormData();
                    dpfFolder.setDocumentName(this.userDef2);
                    dpfFolder.setParentID(parentDocument.getID());
                    dpfFolder.setRootID(parentDocument.getRootID());
                    dpfFolder.setDocumentType(DmsDocument.FOLDER_TYPE);
                    dpfFolder.setRecordStatus(GlobalConstant.STATUS_ACTIVE);
                    dpfFolder.setItemStatus(DmsVersion.AVAILABLE_STATUS);
                    dpfFolder.setCreateType(DmsOperationConstant.DMS_CREATE_BY_UPLOAD);
                    if (canCreateFolderFlag) {
                        dpfFolder = docOperationManager.createFolder(dpfFolder);
                    } else {
                        throw new ApplicationException(DmsErrorConstant.NO_RIGHT_CREATE_FOLDER);
                    }
                }
                DmsDocument dateFolder = docReterialManager.getDocumentByNameParentID(DateFolder, dpfFolder.getID());
                if (dateFolder == null || dateFolder.getID() == null && DmsDocument.FOLDER_TYPE.equals(dpfFolder.getDocumentType())) {
                    dateFolder = (DmsDocument) docForm.getFormData();
                    dateFolder.setDocumentName(DateFolder);
                    dateFolder.setParentID(dpfFolder.getID());
                    dateFolder.setRootID(dpfFolder.getRootID());
                    dateFolder.setDocumentType(DmsDocument.FOLDER_TYPE);
                    dateFolder.setRecordStatus(GlobalConstant.STATUS_ACTIVE);
                    dateFolder.setItemStatus(DmsVersion.AVAILABLE_STATUS);
                    dateFolder.setCreateType(DmsOperationConstant.DMS_CREATE_BY_UPLOAD);
                    if (canCreateFolderFlag) {
                        dateFolder = docOperationManager.createFolder(dateFolder);
                    } else {
                        throw new ApplicationException(DmsErrorConstant.NO_RIGHT_CREATE_FOLDER);
                    }
                }
                if (dateFolder != null) {
                    this.parentID = dateFolder.getID();
                    this.rootID = dateFolder.getRootID();
                } else {
                    this.parentID = parentDocument.getID();
                    this.rootID = parentDocument.getRootID();
                }
            } else {
                this.parentID = dmsDocument.getParentID();
                this.rootID = dmsDocument.getRootID();
            }
            docOperationManager.release();
            docReterialManager.release();
        }
        log.debug(" New Parent ID = " + parentID + " rootID = " + rootID);
        analyzedResult = this.analyzeForCustomizationAction(sessionContainer, conn, docForm, infile);
        return analyzedResult;
    }
