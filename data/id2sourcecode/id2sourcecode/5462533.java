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
            TreeNode[] tns = TreeNode.getSiteChannelTree();
            if (userId == 1 || flagSA.trim().equals("1")) {
                tns = TreeNode.getSiteChannelTree();
            } else {
                tns = TreeNode.getSiteChannelTree(userId, "1");
            }
            if (tns == null) {
                tns = new TreeNode[0];
            }
            if (SHOW_COPY_JSP.equalsIgnoreCase(operate)) {
                String orgChanPath = (String) request.getParameter("orgChanPath");
                String temp = "";
                for (int i = 0; i < tns.length; i++) {
                    if (tns[i].getPath().indexOf(orgChanPath) > -1) {
                        temp += tns[i].getPath() + ",";
                    }
                }
                String[] disabledPaths = Function.stringToArray(temp);
                String siteChannelTreeCode = getTreeJsCode(SITE_CHANNEL_TREE, TreeNode.getSiteChannelTreeRoot(), tns, new String[0], disabledPaths, user);
                String docTypeTreeCode = getTreeJsCode(TreeNode.DOC_TYPE_TREE, TreeNode.getDocTypeTreeRoot(), TreeNode.getDocTypeTree(), new String[0], new String[0], user);
                request.setAttribute("siteChannelTreeCode", siteChannelTreeCode);
                request.setAttribute("docTypeTreeCode", docTypeTreeCode);
                return mapping.findForward("showCopyJsp");
            } else if (MOVE.equalsIgnoreCase(operate)) {
                String orgChanPath = (String) request.getParameter("orgChanPath");
                String aimChanPath = (String) request.getParameter("aimChanPath");
                if (null == orgChanPath || "".equals(orgChanPath) || null == aimChanPath || "".equals(aimChanPath)) {
                    throw new Exception("�ƶ�ԭƵ������Ŀ��Ƶ��Ϊ�գ�");
                }
                if (aimChanPath.indexOf(orgChanPath) > -1) {
                    throw new Exception("�ƶ�Ŀ��Ƶ��Ϊ�ƶ�Ƶ������Ƶ����");
                }
                int maxLevel = new ChannelDAO().getChannelMaxLevel(orgChanPath);
                if (maxLevel - (orgChanPath.length() - aimChanPath.length()) / 5 > Const.CHANNEL_PATH_MAX_LEVEL) {
                    throw new Exception("�ƶ�Ŀ��Ƶ������̫�Ŀǰϵͳֻ֧�� " + Const.CHANNEL_PATH_MAX_LEVEL + " �㣡");
                }
                int level = aimChanPath.length() / 5;
                int channelId = (int) IdGenerator.getInstance().getId(IdGenerator.GEN_ID_IP_CHANNEL);
                String parentId = aimChanPath.substring((level - 1) * 5, aimChanPath.length());
                String sitePath = aimChanPath.substring(0, 10);
                int siteID = ((Site) TreeNode.getInstance(sitePath)).getSiteID();
                ChannelDAO channelDao = new ChannelDAO();
                Channel orgChannel = channelDao.getInstanceByPath(orgChanPath);
                ChannelPath channelPath = new ChannelPath();
                channelPath.setCPath(aimChanPath);
                String treeId = channelPath.getDefaultValue();
                orgChannel.setPath(aimChanPath + treeId);
                orgChannel.setLevel(level);
                orgChannel.setParentId(parentId);
                orgChannel.setSiteId(siteID);
                List chanlist = getChannelList(orgChannel, orgChanPath);
                List browselist = getBrowseList(orgChannel, orgChanPath);
                List doclist = getDocList(orgChannel, orgChanPath);
                List fieldsetlist = getFieldSetList(orgChannel, orgChanPath);
                changeChannel(orgChannel, orgChanPath, chanlist);
                changeTreeFrame(orgChannel, orgChanPath, aimChanPath, chanlist);
                changeBrowse(orgChannel, orgChanPath, browselist);
                changeDoc(orgChannel, orgChanPath, doclist);
                changeFieldSet(orgChannel, orgChanPath, fieldsetlist);
                request.setAttribute(Const.SUCCESS_MESSAGE_NAME, "Ƶ���ƶ��ɹ�");
                return mapping.findForward("operateSuccess");
            } else {
                throw new Exception("δ֪�Ĳ�������:" + operate + ",����Ƶ���ƶ���صĴ���!");
            }
        } catch (Exception ex) {
            log.error("Ƶ���ƶ�ʧ��!", ex);
            request.setAttribute(Const.ERROR_MESSAGE_NAME, "Ƶ���ƶ�ʧ��!<br>" + ex.getMessage());
            return mapping.findForward("error");
        }
    }
