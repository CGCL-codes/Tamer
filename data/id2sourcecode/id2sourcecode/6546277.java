    public void updateUserPersistents(ModelRequest req, @SuppressWarnings("unused") FormularDescriptor formular, PersistentDescriptor persistents, @SuppressWarnings("unused") boolean modified) throws ModelException, PersistenceException {
        PersistentFactory persistentManager = (PersistentFactory) req.getService(PersistentFactory.ROLE, req.getDomain());
        updateSystemGroups(persistentManager, persistents.getPersistent("sysUser").getField("uid"), (String) persistents.getAttribute("role"));
        try {
            Properties props = new Properties();
            props.put("userId", new Integer(persistents.getPersistent("sysUser").getFieldInt("uid")));
            if (persistents.getAttribute("passwordNew") != null) {
                props.put("password", StringTools.digest((String) persistents.getAttribute("passwordNew")));
            }
            ModelTools.callModel(req, "aktera.aktario.user.modify-aktario-user", props);
        } catch (ModelException x) {
        }
        if (persistents.getPersistent("sysUser").getFieldInt("uid") == UserTools.getCurrentUserId(req)) {
            preferencesManager.clearCache(UserTools.getCurrentUserId(req));
            UserTools.setUserEnvObject(req, "sessionInfoLoaded", "N");
        }
    }
