    public void saveLocalAccount(LocalAuthorizationBean_v03 bean) {
        String appid = bean.getApplicationIdentifier();
        String uid = bean.getUserIdentifier();
        String newPassword = "";
        if (!"".equals(bean.getPassword())) {
            try {
                newPassword = ch.unibe.a3ubAdmin.persistence.localaccounts.HashUtil.getInstance().digest(bean.getPassword());
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("error calculating hash: ", e);
                }
            }
        } else {
            LocalAuthorizationBean_v03 old = this.getLocalAccountWithPassword(bean.getApplicationIdentifier(), bean.getUserIdentifier());
            if (old != null && old.getPassword() != null && !old.getPassword().equals("")) {
                newPassword = old.getPassword();
            }
        }
        bean.setPassword(newPassword);
        if (bean.getUserIdentifierNumber() == null || bean.getUserIdentifierNumber().equals("")) {
            bean.setUserIdentifierNumber("" + this.getNextUid());
        }
        locMan.saveObject(appid + "-" + uid, bean);
        List lte = (List) locMan.loadObject("applist." + appid, new ArrayList());
        if (lte == null) {
            lte = new ArrayList();
        }
        if (!lte.contains(appid + "-" + uid)) {
            lte.add(appid + "-" + uid);
            locMan.saveObject("applist." + bean.getApplicationIdentifier(), lte);
        }
    }
