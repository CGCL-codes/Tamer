    public void rePublish(DocumentPublish tmpDoc) throws Exception {
        DocumentCBF doc = DocumentCBF.getInstance(tmpDoc.getId());
        String publishedPath[] = { tmpDoc.getChannelPath() };
        tmpDoc = new DocumentPublish(doc, publishedPath[0], tmpDoc.getPublisher(), tmpDoc.getPublishDate(), tmpDoc.getValidStartDate(), tmpDoc.getValidEndDate(), tmpDoc.getOrderNo());
        tmpDoc.delete();
        tmpDoc.add();
        String publishPath = tmpDoc.getChannelPath();
        String sitePath = null;
        if (publishPath == null || publishPath.length() < 10) {
            log.error("Ҫ����ķ���·����,path=" + publishPath + ",��path�ϵķ���ʧ��!");
        }
        if (!publishPath.substring(0, 10).equals(sitePath)) {
            sitePath = publishPath.substring(0, 10);
            int showTemplateId = -1;
            showTemplateId = SiteChannelDocTypeRelation.getShowTemplateId(publishPath, doc.getDoctypePath());
            if (showTemplateId == -1 || showTemplateId == 0) {
                DocType docType = (DocType) DocType.getInstance(doc.getDoctypePath());
                if (docType == null) {
                    throw new Exception("û���ҵ�pathΪ" + doc.getDoctypePath() + "���ĵ�����!");
                }
                showTemplateId = docType.getShowTemplateID();
            }
            Template template = Template.getInstance(showTemplateId);
            if (template == null) {
                throw new Exception("û���ҵ�idΪ" + showTemplateId + "��ģ��!");
            }
            String templateJspUrl = ConfigInfo.getInstance().getInfoplatDataDirContext() + "template/dynamic/" + template.getAsciiName() + "/index_p.jsp?docId=" + doc.getId() + "&publishedPath=" + publishPath;
            log.debug("ģ��jsp������url=" + templateJspUrl);
            TreeNode treeNode = TreeNode.getInstance(publishPath.substring(0, 10));
            if (treeNode == null) {
                throw new Exception("�Ҳ�������·��Ϊ" + publishPath + "��վ��ʵ��(path=" + publishPath.substring(0, 10) + "!");
            }
            Site site = null;
            try {
                site = (Site) treeNode;
            } catch (Exception ex) {
                throw new Exception("pathΪ" + treeNode.getPath() + "����һ��վ��ʵ��,��������ݶ������!");
            }
            String outFile = ConfigInfo.getInstance().getInfoplatDataDir() + "pub/" + site.getAsciiName() + "/docs/" + Function.getNYofDate(doc.getCreateDate()) + "/d_" + doc.getId() + ".html";
            String outFileDir = new File(outFile).getParent();
            log.debug("����ģ��jsp��ɵ�html��ȫ·��=" + outFile);
            File f = new File(outFileDir);
            if (!f.exists()) {
                f.mkdirs();
            }
            try {
                new GenHtml().generateHtmlPage(templateJspUrl, outFile, true);
            } catch (Exception ex2) {
                log.error("���HTML�ļ�����");
                log.error(ex2.toString());
            }
            String templateResDir = new File(ConfigInfo.getInstance().getInfoplatDataDir()).getPath() + "/template/dynamic/" + template.getAsciiName() + "/res_" + template.getAsciiName() + "/";
            log.debug("ģ����Դ��·��=" + templateResDir);
            String docResDir = new File(ConfigInfo.getInstance().getInfoplatDataDir()).getPath() + "/workflow/docs/" + Function.getNYofDate(doc.getCreateDate()) + "/res/";
            log.debug("�ĵ���Դ(ͼƬ,����,������Դ)��·��=" + docResDir);
            String publishTemplateResDir = outFileDir + "/res_" + template.getAsciiName() + "/";
            String publishDocResDir = outFileDir + "/res/";
            File fTmp = new File(publishTemplateResDir);
            if (!fTmp.exists()) {
                fTmp.mkdirs();
            }
            fTmp = new File(publishDocResDir);
            if (!fTmp.exists()) {
                fTmp.mkdirs();
            }
            fTmp = null;
            log.debug("����ʱģ����Դ·��=" + publishTemplateResDir);
            log.debug("����ʱ�ĵ���Դ·��=" + publishDocResDir);
            String[] resFileNames = doc.getAllResourceUri();
            for (int j = 0; j < resFileNames.length; j++) {
                ces.coral.file.FileOperation.copy(docResDir + resFileNames[j], publishDocResDir + resFileNames[j], true);
            }
            try {
                templateResDir = new File(templateResDir).getPath() + File.separator;
                publishTemplateResDir = new File(publishTemplateResDir).getPath() + File.separator;
                ces.coral.file.FileOperation.copyDir(templateResDir, publishTemplateResDir);
            } catch (Exception ex1) {
                log.error("templateResDir=" + templateResDir);
                log.error("publishTemplateResDir=" + publishTemplateResDir);
                log.error("copyģ����Դʧ��!", ex1);
            }
        }
    }
