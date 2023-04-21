    public void Zip(Integer parentID, Integer rootID, HttpServletRequest request, ZipOutputStream out, String base) throws ApplicationException {
        Connection conn = null;
        SessionContainer sessionContainer = null;
        try {
            conn = this.getConnection(request);
            sessionContainer = this.getSessionContainer(request);
            List documentList = null;
            List docRootListTemp = null;
            List listTemp = null;
            DmsDocument docTemp = null;
            DmsRoot dmsRoot = null;
            DmsDocument doc = null;
            DmsDocumentDAObject dmsDocumentDAO = new DmsDocumentDAObject(sessionContainer, conn);
            DmsRootDAObject dmsRootDAObject = new DmsRootDAObject(sessionContainer, conn);
            if (!Utility.isEmpty(rootID)) {
                documentList = dmsDocumentDAO.getListByParentID(parentID, rootID, DmsDocument.FOLDER_TYPE);
                out.putNextEntry(new ZipEntry(base + "/"));
                base = base.length() == 0 ? "" : base + "/";
                for (int i = 0; i < documentList.size(); i++) {
                    docTemp = (DmsDocument) documentList.get(i);
                    parentID = docTemp.getID();
                    rootID = docTemp.getRootID();
                    Zip(parentID, rootID, request, out, base + docTemp.getDocumentName());
                }
            } else {
                out.putNextEntry(new ZipEntry(base + "/"));
                base = base.length() == 0 ? "" : base + "/";
                docRootListTemp = dmsRootDAObject.getAllPublicRootList();
                for (int j = 0; j < docRootListTemp.size(); j++) {
                    dmsRoot = (DmsRoot) docRootListTemp.get(j);
                    doc = docRetrievalManager.getRootFolderByRootID(dmsRoot.getID());
                    parentID = doc.getID();
                    rootID = doc.getRootID();
                    Zip(parentID, rootID, request, out, base + doc.getDocumentName());
                }
            }
            dmsDocumentDAO = null;
            dmsRootDAObject = null;
        } catch (Exception e) {
            log.error(e, e);
        }
    }
