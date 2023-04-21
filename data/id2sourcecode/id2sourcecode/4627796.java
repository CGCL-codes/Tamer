    public void exportZipFolderStructure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        Connection conn = null;
        SessionContainer sessionContainer = null;
        ArrayList resultList = new ArrayList();
        try {
            conn = this.getConnection(request);
            sessionContainer = this.getSessionContainer(request);
            MaintFolderStructureLoaderForm actionForm = (MaintFolderStructureLoaderForm) form;
            this.docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            this.docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            RootRetrievalManager rootRetrievalManager = new RootRetrievalManager(sessionContainer, conn);
            String tempConvertedName = String.valueOf(System.currentTimeMillis());
            StringBuffer tempZipFile = new StringBuffer();
            DmsLocMaster locMaster = rootRetrievalManager.getTargetLocMasterByLocID(new Integer(1));
            String locMasterPath = locMaster.getLocPath();
            log.debug("locMasterPath = " + locMasterPath);
            tempZipFile = tempZipFile.append(locMasterPath).append("/").append(tempConvertedName).append(".zip");
            org.apache.tools.zip.ZipOutputStream out = new ZipOutputStream(new FileOutputStream(tempZipFile.toString()));
            Integer rootID = null;
            Integer parentID = null;
            if (GlobalConstant.PRIORITY_LOW.equals(actionForm.getZipExportFolderType())) {
                parentID = new Integer(actionForm.getZipExpParentID());
                rootID = new Integer(actionForm.getZipExpRootID());
            } else if (GlobalConstant.PRIORITY_MEDIUM.equals(actionForm.getZipExportFolderType())) {
                parentID = new Integer(0);
            }
            if (!Utility.isEmpty(rootID)) {
                String[] zipSourcePath = TextUtility.splitString(actionForm.getZipSourcePath(), "\\");
                out.putNextEntry(new ZipEntry("/"));
                this.Zip(parentID, rootID, request, out, zipSourcePath[zipSourcePath.length - 1]);
            } else {
                this.Zip(parentID, rootID, request, out, "");
            }
            request.getSession().setAttribute("zipExportFileName", tempZipFile.toString());
            out.close();
        } catch (Exception e) {
            log.error(e, e);
        }
    }
