    public void unPublish(DocumentPublish doc, String userId, boolean isBackProcess) throws Exception {
        DBOperation dbo = null;
        Connection connection = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            boolean isDelRes = true;
            String sql = "select count(doc_id) from t_ip_browse where doc_id=" + doc.getId() + " and channel_path like '" + doc.getChannelPath().substring(0, 10) + "%'";
            dbo = createDBOperation();
            connection = dbo.getConnection();
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(sql);
            if (resultSet.next()) {
                if (resultSet.getInt(1) > 1) {
                    isDelRes = false;
                }
            }
            if (isDelRes) {
                DocType docType = (DocType) DocType.getInstance(doc.getDoctypePath());
                Template template = Template.getInstance(docType.getShowTemplateID());
                String outFile = ConfigInfo.getInstance().getInfoplatDataDir() + "pub" + File.separator + ((Site) TreeNode.getInstance(doc.getChannelPath().substring(0, 10))).getAsciiName() + File.separator + "docs" + File.separator + Function.getNYofDate(doc.getCreateDate()) + File.separator + "d_" + doc.getId();
                File f = new File(new File(outFile + ".html").getPath());
                int i = 2;
                while (f.exists()) {
                    f.delete();
                    f = new File(new File(outFile + "_" + i + ".html").getPath());
                    i++;
                }
                log.debug("ɾ����ɵ�html�ļ���" + f.getPath());
                String publishDocResDir = new File(outFile).getParent() + File.separator + "res" + File.separator;
                String[] resFileNames = DocumentCBF.getInstance(doc.getId()).getAllResourceUri();
                for (int j = 0; j < resFileNames.length; j++) {
                    FileOperation.deleteFile(publishDocResDir + resFileNames[j]);
                    log.debug("ɾ���ĵ���Դ��" + publishDocResDir + resFileNames[j]);
                }
                publishDocResDir = new File(outFile).getParent() + File.separator + "d_" + doc.getId() + ".files" + File.separator;
                FileOperation.deleteDirectory(publishDocResDir);
            }
            delete(doc);
            boolean isBack = true;
            sql = "select count(doc_id) from t_ip_browse where doc_id=" + doc.getId();
            resultSet = stmt.executeQuery(sql);
            if (resultSet.next()) {
                if (resultSet.getInt(1) >= 1) {
                    isBack = false;
                }
            }
            if (isBack && isBackProcess) {
                int docId = doc.getId();
                reCreateProInst(String.valueOf(docId), userId);
            }
        } catch (Exception ex) {
            log.error("ɾ���ĵ�ʧ��!", ex);
            throw ex;
        } finally {
            close(resultSet, stmt, null, connection, dbo);
        }
    }
