    public int doStartTag() throws JspException {
        DocumentTag papa = (DocumentTag) TagSupport.findAncestorWithClass(this, DocumentTag.class);
        if (papa == null) {
            throw new JspException("DocumentFieldTag:�����ҵ�����ǩ ces.platform.infoplat.taglib.ds.document.DocumentFieldTag");
        }
        Document document = papa.getDocument();
        boolean isPreview = papa.isPreview();
        HashMap map = new HashMap();
        String docId = String.valueOf(document.getId());
        map.put("docId", docId);
        map.put("yearNo", new Integer(document.getYearNo()).toString());
        map.put("periodicalNo", new Integer(document.getPeriodicalNo()).toString());
        map.put("wordNo", new Integer(document.getWordNo()).toString());
        map.put("title", document.getTitle());
        map.put("hyperlink", document.getHyperlink());
        map.put("titleColor", document.getTitleColor());
        map.put("subTitle", document.getSubTitle());
        map.put("author", document.getAuthor());
        map.put("emitUnit", document.getEmitUnit());
        map.put("editorRemark", document.getEditorRemark());
        map.put("abstractWords", document.getAbstractWords());
        map.put("sourceId", new Integer(document.getSourceId()).toString());
        map.put("securityLevelId", new Integer(document.getSecurityLevelId()).toString());
        map.put("lastestModifyDate", document.getLastestModifyDate());
        map.put("remarkProp", new Integer(document.getRemarkProp()).toString());
        map.put("notes", document.getNotes());
        map.put("createrId", new Integer(document.getCreater()).toString());
        map.put("createrName", document.getCreaterName());
        map.put("emitDate", dateToString(document.getEmitDate()));
        map.put("createDate", dateToString(document.getCreateDate()));
        if (!isPreview) {
            DocumentPublish doc = (DocumentPublish) document;
            map.put("publisherId", new Integer(doc.getPublisher()).toString());
            map.put("publishDate", dateToString(doc.getPublishDate()));
            map.put("validEndDate", doc.getValidEndDate());
            map.put("validStartDate", doc.getValidStartDate());
            map.put("docContent", doc.getContent());
        } else {
            DocumentCBF doc = (DocumentCBF) document;
            String sContent = doc.getContent();
            int iInd = sContent.indexOf("src=\"res/");
            if (iInd > -1) {
                String sRep = "../../../workflow/docs/" + Function.getNYofDate(doc.getCreateDate()) + "/";
                for (; iInd > -1; ) {
                    sContent = sContent.substring(0, iInd + 5) + sRep + sContent.substring(iInd + 5);
                    iInd = sContent.indexOf("src=\"res/");
                }
            }
            RE re = new RE();
            RECompiler compiler = new RECompiler();
            re.setProgram(compiler.compile("d_" + docId + ".files"));
            sContent = re.subst(sContent, "../../../workflow/docs/" + Function.getNYofDate(doc.getCreateDate()) + "/d_" + docId + ".files");
            map.put("docContent", sContent);
        }
        map.put("attachState", document.getAttachStatus());
        map.put("channelPath", document.getChannelPath());
        map.put("doctypePath", document.getDoctypePath());
        map.put("pertinentWords", document.getPertinentWords());
        map.put("reservation1", document.getReservation1());
        map.put("reservation2", document.getReservation2());
        map.put("reservation3", document.getReservation3());
        map.put("reservation4", document.getReservation4());
        map.put("reservation5", document.getReservation5());
        map.put("reservation6", document.getReservation6());
        JspWriter jspwriter = super.pageContext.getOut();
        try {
            if (fieldName != null && fieldName.equals("publisherName")) {
                map.put("publisherName", ((DocumentPublish) document).getPublisherName());
            }
            String field = (String) map.get(fieldName);
            if (field != null) {
                if (style != null) {
                    field = new StringBuffer("<span style=>").append(style).append(field).append("</span>").toString();
                }
                if (autoPrint) {
                    jspwriter.print(field);
                    return SKIP_BODY;
                } else {
                    pageContext.setAttribute(fieldName, field);
                    return EVAL_BODY_INCLUDE;
                }
            }
        } catch (IOException ioexception) {
            throw new JspException("DocumentFieldTag: ���������ݵ��������");
        }
        return SKIP_BODY;
    }
