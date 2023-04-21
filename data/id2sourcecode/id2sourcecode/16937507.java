    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String operate = (String) request.getParameter("operate");
        if (operate == null || operate.trim().equals("")) {
            operate = SHOW_COPY_JSP;
        }
        try {
            LoginUser lu = new LoginUser();
            lu.setRequest(request);
            int userId = Integer.parseInt(lu.getDefaultValue());
            User user = (User) request.getSession().getAttribute("user");
            String flagSA = (user != null ? user.getFlagSA() : "");
            if (SHOW_COPY_JSP.equalsIgnoreCase(operate)) {
                String orgChanPath = (String) request.getParameter("orgChanPath");
                String temp = "";
                TreeNode[] tns = TreeNode.getSiteChannelTree();
                for (int i = 0; i < tns.length; i++) {
                    if (tns[i].getPath().indexOf(orgChanPath) > -1) {
                        temp += tns[i].getPath() + ",";
                    }
                }
                String[] disabledPaths = Function.stringToArray(temp);
                String siteChannelTreeCode = getTreeJsCode(SITE_CHANNEL_TREE, TreeNode.getSiteChannelTreeRoot(), tns, new String[0], disabledPaths, user);
                request.setAttribute("siteChannelTreeCode", siteChannelTreeCode);
                return mapping.findForward("showCopyJsp");
            } else if (COPY.equalsIgnoreCase(operate)) {
                String orgChanPath = (String) request.getParameter("orgChanPath");
                String aimChanPath = (String) request.getParameter("aimChanPath");
                if (null == orgChanPath || "".equals(orgChanPath) || null == aimChanPath || "".equals(aimChanPath)) {
                    throw new Exception("����ԭƵ������Ŀ��Ƶ��Ϊ�գ�");
                }
                if (aimChanPath.indexOf(orgChanPath) > -1) {
                    throw new Exception("����Ŀ��Ƶ��Ϊ����Ƶ������Ƶ����");
                }
                int maxLevel = new ChannelDAO().getChannelMaxLevel(orgChanPath);
                if (maxLevel - (orgChanPath.length() - aimChanPath.length()) / 5 > Const.CHANNEL_PATH_MAX_LEVEL) {
                    throw new Exception("����Ŀ��Ƶ������̫�Ŀǰϵͳֻ֧�� " + Const.CHANNEL_PATH_MAX_LEVEL + " �㣡");
                }
                new Channel().copy(orgChanPath, aimChanPath);
                request.setAttribute(Const.SUCCESS_MESSAGE_NAME, "Ƶ�����Ƴɹ�");
                return mapping.findForward("operateSuccess");
            } else {
                throw new Exception("δ֪�Ĳ�������:" + operate + ",����Ƶ��������صĴ���!");
            }
        } catch (Exception ex) {
            log.error("Ƶ������ʧ��!", ex);
            request.setAttribute(Const.ERROR_MESSAGE_NAME, "Ƶ������ʧ��!<br>" + ex.getMessage());
            return mapping.findForward("error");
        }
    }
