    public void refresh(String path) {
        try {
            Channel[] chans = (Channel[]) new TreeNodeDAO().getList(path);
            if (chans == null) {
                Channel ch = (Channel) Channel.getInstance(path);
                log.debug("ˢ��Ƶ��" + ch.getAsciiName() + "������Ƶ���µ��ĵ�ҳ��");
                if (ch == null) return;
                chans = new Channel[1];
                chans[0] = ch;
            }
            for (int j = 0; j < chans.length; j++) {
                DocumentPublish[] docs = Documents.getPublishedList(chans[j].getPath(), 0, true);
                for (int i = 0; i < docs.length; i++) {
                    DocumentPublish doc = docs[i];
                    String publishedPath = docs[i].getChannelPath();
                    String sitePath = publishedPath.substring(0, 10);
                    DocType docType = (DocType) DocType.getInstance(doc.getDoctypePath());
                    if (docType == null) {
                        throw new Exception("û���ҵ�pathΪ" + doc.getDoctypePath() + "���ĵ�����!");
                    }
                    Template template = Template.getInstance(docType.getShowTemplateID());
                    if (template == null) {
                        throw new Exception("û���ҵ�idΪ" + docType.getShowTemplateID() + "��ģ��!");
                    }
                    String templateJspUrl = ConfigInfo.getInstance().getInfoplatDataDirContext() + "template/dynamic/" + template.getAsciiName() + "/index_p.jsp?docId=" + doc.getId() + "&publishedPath=" + publishedPath;
                    TreeNode treeNode = TreeNode.getInstance(publishedPath.substring(0, 10));
                    if (treeNode == null) {
                        throw new Exception("�Ҳ�������·��Ϊ" + publishedPath + "��վ��ʵ��(path=" + publishedPath.substring(0, 10) + "!");
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
                    new GenHtml().generateHtmlPage(templateJspUrl, outFile, true);
                    log.debug("����ĵ�ҳ��ɹ���templateJspUrl=" + templateJspUrl);
                }
            }
        } catch (Exception e) {
            log.error("����ˢ���ĵ����?", e);
        }
    }
