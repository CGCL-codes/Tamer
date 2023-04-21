    public void setPassword(String username, String oldPassword, String newPassword) throws AoException {
        log.debug("Enter AoSessionImpl::setPassword()");
        if ((username == null) || ("".equals(username))) {
            NameValue nv = getContextByName("USER");
            username = nv.value.u.stringVal();
        }
        ApplicationElement elem1 = this.getApplicationStructure().getElements("AoUser")[0];
        InstanceElement ieuser = elem1.getInstanceByName(username);
        if (ieuser == null) {
            throw new AoException(ErrorCode.AO_BAD_PARAMETER, SeverityFlag.ERROR, 0, "AoSession::setPassword()");
        }
        boolean su = false;
        ApplicationElement elem2 = getApplicationStructure().getElements("AoUserGroup")[0];
        ApplicationRelation ar = getApplicationStructure().getRelations(elem1, elem2)[0];
        InstanceElementIterator iei = ieuser.getRelatedInstances(ar, "*");
        for (int i = 0; i < iei.getCount(); i++) {
            InstanceElement ie = iei.nextOne();
            NameValueUnit nvu = ie.getValueByBaseName("superuser_flag");
            if (nvu.value.u.shortVal() == 1) {
                su = true;
            }
        }
        iei.destroy();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
            throw new AoException(ErrorCode.AO_IMPLEMENTATION_PROBLEM, SeverityFlag.ERROR, 0, "AoSession::setPassword()");
        }
        oldPassword = new String(md.digest(oldPassword.getBytes()));
        NameValueUnit nvu = ieuser.getValueByBaseName("password");
        if (!su) {
            String currpassword = nvu.value.u.stringVal();
            if (!currpassword.equals(oldPassword)) {
                throw new AoException(ErrorCode.AO_BAD_PARAMETER, SeverityFlag.ERROR, 0, "AoSession::setPassword()");
            }
        }
        nvu.value.u.stringVal(newPassword);
        ieuser.setValue(nvu);
        setContextString("PASSWORD", newPassword);
        log.debug("Exit AoSessionImpl::setPassword()");
    }
