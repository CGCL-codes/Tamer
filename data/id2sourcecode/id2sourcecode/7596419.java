    private void saveContent(DocumentCBF docBF, String docContent, String[] retAffixs, String blRtype, boolean blRte) throws Exception {
        try {
            CesGlobals cesGlobals = new CesGlobals();
            String uploadPath;
            cesGlobals.setConfigFile("platform.xml");
            uploadPath = cesGlobals.getCesXmlProperty("platform.datadir");
            uploadPath = new File(uploadPath).getPath();
            if (uploadPath.endsWith("\\")) {
                uploadPath = uploadPath.substring(0, uploadPath.length() - 1);
            }
            uploadPath = uploadPath + "/infoplat/workflow/docs/" + Function.getNYofDate(docBF.getCreateDate()) + "/";
            if (!new File(uploadPath).isDirectory()) {
                new File(uploadPath).mkdirs();
            }
            if (blRtype.equals("-1")) {
                if (docContent != null && docContent.length() > 0) {
                    String[] reFile = { docContent };
                    if (blRte && retAffixs != null && retAffixs.length > 0) {
                        BufferedInputStream in = new BufferedInputStream(new FileInputStream(docContent));
                        StringBuffer sCon = new StringBuffer();
                        byte[] b = new byte[1024];
                        int iLen = 0;
                        while (iLen != -1) {
                            iLen = in.read(b);
                            sCon.append(new String(b));
                        }
                        in.close();
                        String content = sCon.toString();
                        if (!content.trim().toLowerCase().startsWith("<body>")) content = "<body>" + content + "</body>";
                        DocumentCBFDAO docCBFDAO = new DocumentCBFDAO();
                        DocContentResource[] docResource = docCBFDAO.getDocContentResourceList(docBF.getId());
                        List tmp = new ArrayList();
                        for (int j = 0; j < docResource.length; j++) {
                            tmp.add(docResource[j].getUri());
                        }
                        String fileName = "";
                        File ff;
                        uploadPath += "res/";
                        if (!new File(uploadPath).isDirectory()) {
                            new File(uploadPath).mkdirs();
                        }
                        for (int i = 0; i < retAffixs.length && i < tmp.size(); i++) {
                            ff = new File(retAffixs[i]);
                            if (ff.isFile()) {
                                FileOperation.copy(retAffixs[i], uploadPath + tmp.get(i));
                            }
                        }
                    }
                } else {
                    String newFileName = "d_" + docBF.getId() + ".data";
                    FileOperation.copy(docContent, uploadPath + newFileName);
                }
            } else {
                if (docContent != null && !docContent.equals("")) {
                    if (!docContent.trim().toLowerCase().startsWith("<body>")) docContent = "<body>" + docContent + "</body>";
                    if (blRte) {
                    } else {
                    }
                    if (blRte && retAffixs != null && retAffixs.length > 0) {
                        DocumentCBFDAO docCBFDAO = new DocumentCBFDAO();
                        DocContentResource[] docResource = docCBFDAO.getDocContentResourceList(docBF.getId());
                        List tmp = new ArrayList();
                        for (int j = 0; j < docResource.length; j++) {
                            tmp.add(docResource[j].getUri());
                        }
                        String fileName = "";
                        File ff;
                        uploadPath += "res/";
                        if (!new File(uploadPath).isDirectory()) {
                            new File(uploadPath).mkdirs();
                        }
                        for (int i = 0; i < retAffixs.length && i < tmp.size(); i++) {
                            ff = new File(retAffixs[i]);
                            if (ff.isFile()) {
                                FileOperation.copy(retAffixs[i], uploadPath + tmp.get(i));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("������������ʧ��!" + e.getMessage());
        }
    }
