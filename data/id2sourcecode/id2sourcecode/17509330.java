    void addPermission(IDigitalPersonalIdentifier requestor, ICtxIdentifier ctxIdent, boolean canRead, boolean canWrite) {
        this.log.debug("Adding permission for context identifier " + ctxIdent + ": Grant {READ=" + canRead + ", WRITE=" + canWrite + "} to requestor " + requestor);
        String actions = null;
        if (canRead && !canWrite) actions = "read"; else if (canRead && canWrite) actions = "read,write"; else if (!canRead && canWrite) actions = "write";
        if (actions == null) return;
        CtxPermission perm = new CtxPermission(ctxIdent, actions);
        try {
            IAccessControlDecision decision = this.decisionMgr.retrieve(requestor);
            if (decision == null) decision = this.decisionMgr.create(requestor);
            decision.add(perm);
            this.decisionMgr.update(decision);
        } catch (AccessControlException e) {
            this.log.error("Could not add permission: " + e.getLocalizedMessage(), e);
        }
        jTextFieldStatus.setText("Added permission " + perm);
    }
