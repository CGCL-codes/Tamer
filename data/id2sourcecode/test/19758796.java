    protected void activate(ComponentContext context) {
        this.myPublicDPI = this.pssMgr.getDefaultDPI();
        try {
            List<ICtxIdentifier> ctxIds = this.ctxBroker.lookup(this.myPublicDPI, this.myPublicDPI, CtxModelType.ATTRIBUTE, RoleMatcherServiceImpl.roleAttributeType);
            if (ctxIds.size() > 0) {
                ICtxAttribute roleAttr = (ICtxAttribute) this.ctxBroker.retrieve(this.myPublicDPI, ctxIds.get(0));
                this.myRole = roleAttr.getStringValue();
                if (logService.isDebugEnabled()) logService.debug("Retrieving my role: " + this.myRole);
            }
        } catch (ContextException e1) {
            this.logService.error("Unable to retrieve the 'Role' attribute for the current PSS. Cause:" + e1.getLocalizedMessage());
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            byte[] resultingBytes = md5.digest(this.uuId.getBytes());
            this.merId = Base64.encodeBase64URLSafeString(resultingBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
