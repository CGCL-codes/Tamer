    public void saveLocalAccount(LocalAuthorizationBean_v03 bean) {
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
                LocalAuthorizationBean_v03 old = this.getLocalAccountWithPassword(bean.getApplicationIdentifier(), bean.getUserIdentifier());
                if (old != null && old.getPassword() != null && !old.getPassword().equals("")) {
                    newPassword = old.getPassword();
                }
            }
            bean.setPassword(newPassword);
            String uidnS = bean.getUserIdentifierNumber();
            int uidn = -1;
            if (uidnS == null || uidnS.equals("")) {
                uidn = PersistenceManager.getInstance().getNextLocalUidNumber();
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
            Group group = man.loadGroup(-1);
            if (log.isDebugEnabled()) {
                log.debug("group: " + group);
            }
            user.setGroup(group);
            user.setUid(bean.getUserIdentifier());
            user.setUidnumber(Integer.parseInt(bean.getUserIdentifierNumber()));
            if (user.getCustommap() == null) {
                user.setCustommap(new HashMap<String, Object>());
            }
            user.getCustommap().put("authbean", bean);
            man.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            if (log.isErrorEnabled()) {
                log.error("error saving local user", e);
            }
        }
    }
