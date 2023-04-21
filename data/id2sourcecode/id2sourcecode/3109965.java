    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String sMagazineHeadId = request.getParameter("magazineHeadId");
        if (sMagazineHeadId == null || sMagazineHeadId.trim().equals("")) {
            request.setAttribute(Const.ERROR_MESSAGE_NAME, "û�еõ�Ҫ�༭���ڿ�ͷId!");
            return mapping.findForward("error");
        }
        String channelPath = request.getParameter("channelPath");
        request.setAttribute("channelPath", channelPath);
        try {
            int magazineHeadId = Integer.parseInt(sMagazineHeadId.trim());
            DocumentCBF magazine = DocumentCBF.getInstance(magazineHeadId);
            String title = request.getParameter("title");
            if (title != null && !title.equals("")) title = Function.convertCharset(title, Constant.PARAM_POST);
            String author = request.getParameter("author");
            if (author != null && !author.equals("")) author = Function.convertCharset(author, Constant.PARAM_POST);
            String emitDateF = request.getParameter("emitDateF");
            String emitDateT = request.getParameter("emitDateT");
            String securityLevel = request.getParameter("securityLevel");
            String keywords = request.getParameter("keywords");
            if (keywords != null && !keywords.equals("")) keywords = Function.convertCharset(keywords, Constant.PARAM_POST);
            String createDateF = request.getParameter("createDateF");
            String createDateT = request.getParameter("createDateT");
            String condition = " ";
            if (title != null && !title.equals("")) condition += " and doc.title like '%" + title + "%' ";
            if (author != null && !author.equals("")) condition += " and doc.author like '%" + author + "%' ";
            if (XmlInfo.getInstance().getSysDataBaseType().equalsIgnoreCase(Constant.SQLSERVER) || XmlInfo.getInstance().getSysDataBaseType().equalsIgnoreCase(Constant.MYSQL)) {
                if (emitDateF != null && !emitDateF.equals("")) condition += " doc.emit_date >= '" + emitDateF + "' ";
                if (emitDateT != null && !emitDateT.equals("")) condition += " and doc.emit_date <= '" + emitDateT + "' ";
                if (createDateF != null && !createDateF.equals("")) condition += " and doc.create_date >= '" + createDateF + "' ";
                if (createDateT != null && !createDateT.equals("")) condition += " and doc.create_date <= '" + createDateT + "' ";
            } else {
                if (emitDateF != null && !emitDateF.equals("")) condition += " and to_char(doc.emit_date,'yyyy-mm-dd') >= '" + emitDateF + "' ";
                if (emitDateT != null && !emitDateT.equals("")) condition += " and to_char(doc.emit_date,'yyyy-mm-dd') <= '" + emitDateT + "' ";
                if (createDateF != null && !createDateF.equals("")) condition += " and to_char(doc.create_date,'yyyy-mm-dd') >= '" + createDateF + "' ";
                if (createDateT != null && !createDateT.equals("")) condition += " and to_char(doc.create_date,'yyyy-mm-dd') <= '" + createDateT + "' ";
            }
            if (securityLevel != null && !securityLevel.equals("")) condition += " and doc.security_level_id = '" + securityLevel + "' ";
            if (keywords != null && !keywords.equals("")) condition += " and doc.keywords like '%" + keywords + "%' ";
            DocumentCBF[] magazineList = magazine.getMagazineList(condition);
            request.setAttribute("magazineList", magazineList);
            request.setAttribute("magazineHeadId", sMagazineHeadId);
            String magazineNavigate = request.getParameter("magazineNavigate");
            String[] magazineNavigateArray = null;
            if (magazineNavigate == null || magazineNavigate.trim().equals("")) {
                magazineNavigate = sMagazineHeadId;
            } else {
                magazineNavigateArray = Function.stringToArray(magazineNavigate);
                int index = Function.indexOf(sMagazineHeadId, magazineNavigateArray);
                if (index == -1) {
                    magazineNavigate = magazineNavigate + "," + sMagazineHeadId;
                } else if (index == magazineNavigateArray.length - 1) {
                } else if (index == 0) {
                    magazineNavigate = sMagazineHeadId;
                } else {
                    magazineNavigateArray = Function.subarray(magazineNavigateArray, 0, index);
                    magazineNavigate = Function.arrayToStr(magazineNavigateArray);
                }
            }
            request.setAttribute("magazineNavigate", magazineNavigate);
            if (null != magazineNavigateArray && magazineNavigateArray.length > 0) {
                if (!magazineNavigateArray[0].equals(sMagazineHeadId)) {
                    magazine = DocumentCBF.getInstance(Integer.parseInt(magazineNavigateArray[0]));
                }
            }
            DocType[] docTypes = null;
            TreeNode[] dts = SiteChannelDocTypeRelation.getDocTypes(magazine.getChannelPath(), true);
            List vDocType = new ArrayList();
            for (int i = 0; i < dts.length; i++) {
                if (((DocType) dts[i]).getPath().startsWith(magazine.getDoctypePath()) && ((DocType) dts[i]).getLevel() > 2) {
                    vDocType.add(dts[i]);
                }
            }
            docTypes = new DocType[vDocType.size()];
            for (int i = 0; i < docTypes.length; i++) {
                docTypes[i] = (DocType) vDocType.get(i);
            }
            request.setAttribute("docTypes", docTypes);
            Template[] templates = Template.getAllTemplates();
            request.setAttribute("tmplatePages", templates);
            return mapping.findForward("magazineList");
        } catch (Exception ex) {
            log.error("�õ��ڿ��б�ʧ��!", ex);
            request.setAttribute(Const.ERROR_MESSAGE_NAME, "�õ��ڿ�ͷid=" + sMagazineHeadId + "���ڿ��ĵ�ʧ��!");
            return mapping.findForward("error");
        }
    }
