    public void getDocInfo(HttpServletRequest request, HttpServletResponse response) {
        BufferedReader info = null;
        try {
            String id = request.getParameter("docId");
            if (StringUtils.isBlank(id)) {
                response.setStatus(404);
                return;
            }
            DocuBean docuBean = DocuUtils.applicationModule.getBean(DocuBean.class, id);
            Permissions permissions = Permissions.READ_ONLY;
            if (permissions.equals(Permissions.NONE)) {
                return;
            }
            response.setHeader("Cache-Control", "private");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Connection", "Keep-Alive");
            response.setHeader("Proxy-Connection", "Keep-Alive");
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.flushBuffer();
            final PrintWriter writer = response.getWriter();
            writer.flush();
            int pageCount = docuBean.getFileNum();
            if (pageCount == 0) {
                final StorageBean storageBean = DocumentConfigMgr.getDocuMgr().getStorageMap().get(docuBean.getPath2());
                if (storageBean != null) {
                    pageCount = storageBean.getPageCounter(DocuUtils.getDatabase(docuBean.getUserId()) + docuBean.getFileName());
                }
                if (pageCount > 0) {
                    docuBean.setFileNum(pageCount);
                    DocuUtils.applicationModule.doUpdate(new Object[] { "fileNum" }, docuBean);
                } else {
                    response.setStatus(404);
                    return;
                }
            }
            String docUri = null;
            String url = "/docviewer?";
            int readNum = DocuUtils.allowPages(docuBean);
            final StorageBean storageBean = DocumentConfigMgr.getDocuMgr().getStorageMap().get(docuBean.getPath2());
            boolean split = false;
            if (storageBean != null) {
                split = storageBean.split(DocuUtils.getDatabase(docuBean.getUserId()) + docuBean.getId() + "/");
            }
            if (readNum == 0 || split) {
                docUri = request.getContextPath() + url + "doc=" + id;
            } else docUri = request.getContextPath() + url + "doc={" + id + "-[*,0]," + (readNum >= docuBean.getFileNum() ? docuBean.getFileNum() : readNum + 1) + "}";
            String doc_status = "总共:" + docuBean.getFileNum() + "页  可阅读:" + (readNum == 0 ? docuBean.getFileNum() : readNum) + "页  价格:" + docuBean.getPoint();
            writer.write("{\"uri\":\"" + docUri + "\",\"permissions\":" + permissions.ordinal() + ",\"success\":" + docuBean.getSuccess() + ",\"numPages\":" + (readNum == 0 ? docuBean.getFileNum() : readNum) + ",\"doc_status\":" + "\"" + doc_status + "\"}");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(404);
        } finally {
            if (info != null) {
                IOUtils.closeIO(info);
            }
        }
    }
