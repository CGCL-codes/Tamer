    @Override
    public void updatePersistents(ModelRequest request, FormularDescriptor formular, PersistentDescriptor persistents, List<Configuration> persistentConfig, boolean modified) throws ModelException, PersistenceException {
        String password = (String) persistents.getAttribute("passwordNew");
        if (!StringTools.isTrimEmpty(password) && persistents.getPersistent("preferences").getFieldBoolean("canChangePassword")) {
            persistents.getPersistent("sysUser").setField("password", StringTools.digest((String) persistents.getAttribute("passwordNew")));
        }
        String pin = (String) persistents.getAttribute("pinNew");
        if (!StringTools.isTrimEmpty(pin)) {
            if (pin.equals(persistents.getAttribute("pinNewRepeat"))) {
                persistents.getPersistent("preferences").setField("pin", pin);
            }
        }
        persistents.getPersistent("sysUser").setField("email", persistents.getPersistent("address").getField("email"));
        super.updatePersistents(request, formular, persistents, persistentConfig, modified);
        try {
            Properties props = new Properties();
            props.put("userId", persistents.getPersistent("sysUser").getFieldInt("uid"));
            if (!StringTools.isTrimEmpty(password)) {
                props.put("password", StringTools.digest((String) persistents.getAttribute("passwordNew")));
            }
            ModelTools.callModel(request, "aktera.aktario.user.modify-aktario-user", props);
        } catch (ModelException x) {
        }
        FormTools.storeInputValuesToPropertyTable(request, formular, persistents, "aktera.PreferencesConfig");
        int userId = UserTools.getCurrentUserId(request).intValue();
        if (userId == persistents.getPersistent("sysUser").getFieldInt("uid")) {
            preferencesManager.clearCache(UserTools.getCurrentUserId(request));
            UserTools.setUserEnvObject(request, "sessionInfoLoaded", "N");
        }
        EventManager em = (EventManager) (EventManager) SpringTools.getBean(EventManager.ID);
        Properties props = new Properties();
        props.put("id", persistents.getPersistent("sysUser").getFieldInt("uid"));
        props.put("name", persistents.getPersistent("sysUser").getFieldString("name"));
        em.fire("aktera.user.updated", request, log, props);
    }
