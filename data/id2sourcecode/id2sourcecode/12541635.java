    public void saveOPL() {
        XUIRequestContext oRequestContext = getRequestContext();
        boObject currentObjectOfPermissions = getXEOObject();
        if (getTargetObject() == null || getCurrentType() == null) {
            oRequestContext.addMessage("Bean", new XUIMessage(XUIMessage.TYPE_POPUP_MESSAGE, XUIMessage.SEVERITY_INFO, MessageLocalizer.getMessage("BAD"), MessageLocalizer.getMessage("CANNOT_SAVE_REQUIRED_FIELDS")));
            oRequestContext.renderResponse();
            return;
        }
        boolean readVal = getBooleanFromString(this.getReadPermission());
        boolean writeVal = getBooleanFromString(this.getWritePermission());
        boolean deleteVal = getBooleanFromString(this.getDeletePermission());
        boolean fullVal = getBooleanFromString(this.getFullControlPermission());
        int securityCode = this.getValueSecurityCode(readVal, writeVal, deleteVal, fullVal);
        try {
            if (getCurrentTarget() != null) {
                bridgeHandler oBridgeHandler = currentObjectOfPermissions.getBridge(KEYS_BRIDGE);
                boBridgeIterator it = oBridgeHandler.iterator();
                while (it.next()) {
                    boObject obj = it.currentRow().getObject();
                    if (getCurrentTarget().getBoui() == obj.getBoui()) {
                        it.currentRow().getAttribute(KEYS_ATT_SECURITY_CODE).setValueLong(securityCode);
                        currentObjectOfPermissions.update();
                        break;
                    }
                }
            } else {
                bridgeHandler oBridgeHandler = currentObjectOfPermissions.getBridge(KEYS_BRIDGE);
                Number target = this.getTargetObject();
                oBridgeHandler.add(target.longValue());
                boBridgeIterator it = oBridgeHandler.iterator();
                it.last();
                oBridgeHandler.getAttribute(KEYS_ATT_SECURITY_CODE).setValueLong(securityCode);
                oBridgeHandler.getAttribute(KEYS_ATT_SECURITY_TYPE).setValueLong(1);
                currentObjectOfPermissions.update();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        oRequestContext = XUIRequestContext.getCurrentContext();
        XVWScripts.closeView(oRequestContext.getViewRoot());
        XUIViewRoot viewRoot = oRequestContext.getSessionContext().createView("netgest/bo/xwc/components/viewers/Dummy.xvw");
        oRequestContext.addMessage("sucessMessage", new XUIMessage(XUIMessage.TYPE_POPUP_MESSAGE, XUIMessage.SEVERITY_INFO, BeansMessages.TITLE_SUCCESS.toString(), BeansMessages.BEAN_SAVE_SUCESS.toString()));
        getViewRoot().getParentView().syncClientView();
        oRequestContext.setViewRoot(viewRoot);
        oRequestContext.renderResponse();
    }
