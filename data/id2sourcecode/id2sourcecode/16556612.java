    public void saveInternalAccount(InternalAuthorizationBean_v03 bean) {
        ch.unibe.a3ubAdmin.control.DatabaseManager man = new ch.unibe.a3ubAdmin.control.DatabaseManager();
        try {
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
                InternalAuthorizationBean_v03 old = (InternalAuthorizationBean_v03) this.getInternalAccount(bean.getUserIdentifier());
                if (old != null && old.getPassword() != null && !old.getPassword().equals("")) {
                    newPassword = old.getPassword();
                }
            }
            bean.setPassword(newPassword);
            String uidnS = bean.getUserIdentifierNumber();
            int uidn = 0;
            if (uidnS == null || uidnS.equals("")) {
                uidn = PersistenceManager.getInstance().getNextInternalUidNumber();
            } else {
                uidn = Integer.parseInt(uidnS);
            }
            ViewUser user = null;
            try {
                user = man.loadViewUser(uidn);
            } catch (ObjectNotFoundException e) {
                user = new ViewUser();
                user.setUidnumber(uidn);
            }
            bean.setUserIdentifierNumber("" + user.getUidnumber());
            user.setCn(bean.getCommonName());
            Group group = man.loadGroup(-2);
            if (log.isDebugEnabled()) {
                log.debug("group: " + group);
            }
            user.setGroup(group);
            user.setUid(bean.getUserIdentifier());
            user.setUidnumber(Integer.parseInt(bean.getUserIdentifierNumber()));
            if (user.getCustommap() == null) {
                user.setCustommap(new HashMap<String, Object>());
            }
            user.getCustommap().put("internalauthbean", bean);
            man.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            if (log.isErrorEnabled()) {
                log.error("error saving internal user", e);
            }
        }
    }
