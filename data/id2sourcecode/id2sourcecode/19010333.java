    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String docId = request.getParameter("docID");
        try {
            LoginUser lu = new LoginUser();
            lu.setRequest(request);
            int userId = Integer.parseInt(lu.getDefaultValue());
            User user = (User) request.getSession().getAttribute("user");
            String flagSA = (user != null ? user.getFlagSA() : "");
            if (docId == null) {
                throw new Exception("û�еõ�Ҫ�������ĵ�id");
            }
            if ("false".equals(request.getParameter("repaixu"))) {
                repaixu = false;
            } else {
                repaixu = true;
            }
            if ("false".equals(request.getParameter("redate"))) {
                redate = false;
            } else {
                redate = true;
            }
            String operate = request.getParameter("operate");
            operate = operate == null ? OPERATE_SHOW_PUBLISH_JSP : operate;
            String workItemId = request.getParameter("workItemId");
            String processId = request.getParameter("processId");
            int[] docIds = Function.strArray2intArray(Function.stringToArray(docId));
            String[] targetChannelPaths = Function.stringToArray(request.getParameter("channelPath"));
            String selfChannelPath = request.getParameter("selfChannelPath");
            if (operate.trim().equalsIgnoreCase(OPERATE_SHOW_PUBLISH_JSP)) {
                showPublishJsp(docIds, request, user);
                return mapping.findForward("showPublishJsp");
            } else if (operate.trim().equalsIgnoreCase(OPERATE_PUBLISH)) {
                if (targetChannelPaths == null || targetChannelPaths.length == 0) {
                    throw new Exception("û�еõ�Ҫ������վ��Ƶ��path!");
                }
                publish(docIds, targetChannelPaths, request, true);
                new Documents().endPublishProcess(String.valueOf(user.getUserID()), Integer.parseInt(processId), workItemId);
                request.setAttribute(Const.SUCCESS_MESSAGE_NAME, "�����ɹ���");
                request.setAttribute(Const.SUCCESS_FRESH_FLAG, "opener");
                return mapping.findForward("operateSuccess");
            } else if (operate.trim().equalsIgnoreCase(OPERATE_COPY_TRANSFER_PUBLISH)) {
                if (targetChannelPaths == null || targetChannelPaths.length == 0) {
                    throw new Exception("û�еõ�Ҫ������վ��Ƶ��path!");
                }
                int iInd = selfChannelPath.indexOf(",");
                if (iInd > -1) selfChannelPath = selfChannelPath.substring(0, iInd);
                new Documents().copyPublish(docIds, selfChannelPath, targetChannelPaths);
                request.setAttribute(Const.SUCCESS_MESSAGE_NAME, "����ת���ɹ���");
                request.setAttribute(Const.SUCCESS_FRESH_FLAG, "opener");
                return mapping.findForward("operateSuccess");
            } else if (operate.trim().equalsIgnoreCase(OPERATE_MOVE_TRANSFER_PUBLISH)) {
                if (targetChannelPaths == null || targetChannelPaths.length == 0) {
                    throw new Exception("û�еõ�Ҫ������վ��Ƶ��path!");
                }
                String[] selfChannelPaths = Function.stringToArray(selfChannelPath);
                if (selfChannelPaths == null || selfChannelPaths.length != docIds.length) {
                    throw new Exception("û�еõ��ĵ����?����path,���ߺ��ĵ�id��һһ��Ӧ!!");
                }
                new Documents().copyPublish(docIds, selfChannelPaths[0], targetChannelPaths);
                unPublish(docIds, selfChannelPaths, String.valueOf(user.getUserID()), false);
                for (int i = 0; i < targetChannelPaths.length; i++) {
                    for (int j = 0; j < docIds.length; j++) {
                        DocumentCBF cbf = DocumentCBF.getInstance(docIds[j]);
                        cbf.setChannelPath(targetChannelPaths[i]);
                        cbf.update();
                    }
                }
                request.setAttribute(Const.SUCCESS_MESSAGE_NAME, "�ƶ�ת���ɹ���");
                request.setAttribute(Const.SUCCESS_FRESH_FLAG, "opener");
                return mapping.findForward("operateSuccess");
            } else if (operate.trim().equalsIgnoreCase(OPERATE_SHOW_TRANSFER_JSP)) {
                showTransferJsp(docIds, request, user);
                return mapping.findForward("showPublishJsp");
            } else if (operate.trim().equalsIgnoreCase(OPERATE_REPUBLISH)) {
                String republishChannel = request.getParameter("republishChannel");
                if (republishChannel == null || republishChannel.trim().equalsIgnoreCase("selfChannel")) {
                    if (targetChannelPaths == null || targetChannelPaths.length == 0) {
                        throw new Exception("û�еõ�Ҫ������վ��Ƶ��path!");
                    }
                    rePublish(docIds, targetChannelPaths, repaixu, redate, request);
                } else {
                    for (int i = 0; i < docIds.length; i++) {
                        String[] publishPaths = Documents.getPublishPaths(docIds[i]);
                        if (publishPaths == null || publishPaths.length == 0) {
                            log.error("û�еõ�docId=" + docIds[i] + "�ķ���Ƶ��path!");
                            continue;
                        }
                        for (int j = 0; j < publishPaths.length; j++) {
                            int[] tmpDocIds = new int[1];
                            tmpDocIds[0] = docIds[i];
                            String[] tmpPublishPaths = new String[1];
                            tmpPublishPaths[0] = publishPaths[j];
                            if (tmpPublishPaths[0] == null) {
                                log.error("�õ��ĵ�������pathʱ,������һ��path=null,���Ը����·�����Ƶ��ʱ����!docId=" + tmpDocIds[0]);
                                continue;
                            }
                            rePublish(tmpDocIds, tmpPublishPaths, repaixu, redate, request);
                        }
                    }
                }
                request.setAttribute(Const.SUCCESS_MESSAGE_NAME, "���·����ɹ���");
                request.setAttribute(Const.SUCCESS_FRESH_FLAG, "opener");
                return mapping.findForward("operateSuccess");
            } else if (operate.trim().equalsIgnoreCase(OPERATE_UNPUBLISH_BYCURRCHANNEL)) {
                if (targetChannelPaths == null || targetChannelPaths.length == 0 || targetChannelPaths.length != docIds.length) {
                    throw new Exception("û�еõ�Ҫ������վ��Ƶ��path,���ߺ��ĵ�id��һһ��Ӧ!");
                }
                unPublish(docIds, targetChannelPaths, String.valueOf(user.getUserID()), true);
                request.setAttribute(Const.SUCCESS_MESSAGE_NAME, "�����ڵ�ǰƵ���ϵ��ĵ��ɹ���");
                request.setAttribute(Const.SUCCESS_FRESH_FLAG, "opener");
                return mapping.findForward("operateSuccess");
            } else if (operate.trim().equalsIgnoreCase(OPERATE_UNPUBLISH_BYALLCHANNEL)) {
                for (int i = 0; i < docIds.length; i++) {
                    String[] publishPaths = Documents.getPublishPaths(docIds[i]);
                    if (publishPaths == null || publishPaths.length == 0) {
                        log.error("û�еõ�docId=" + docIds[i] + "�����з������Ƶ��,���ĵ�����ʧ��!");
                        continue;
                    }
                    int[] tmpDocIds = new int[publishPaths.length];
                    for (int j = 0; j < tmpDocIds.length; j++) {
                        tmpDocIds[j] = docIds[i];
                    }
                    unPublish(tmpDocIds, publishPaths, String.valueOf(user.getUserID()), true);
                }
                request.setAttribute(Const.SUCCESS_MESSAGE_NAME, "����������Ƶ���ϵ��ĵ��ɹ���");
                request.setAttribute(Const.SUCCESS_FRESH_FLAG, "opener");
                return mapping.findForward("operateSuccess");
            } else {
                throw new Exception("δ֪�Ĳ�������:" + operate + "!��������url��");
            }
        } catch (Exception ex) {
            log.error("��������", ex);
            ex.printStackTrace();
            request.setAttribute(Const.ERROR_MESSAGE_NAME, "����!" + ex.getMessage());
            return mapping.findForward("error");
        }
    }
