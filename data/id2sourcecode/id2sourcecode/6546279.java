    public int createUserPersistents(ModelRequest request, @SuppressWarnings("unused") FormularDescriptor formular, PersistentDescriptor persistents) throws ModelException, PersistenceException {
        PersistentFactory persistentManager = (PersistentFactory) request.getService(PersistentFactory.ROLE, request.getDomain());
        persistents.getPersistent("sysUser").add();
        Integer userId = new Integer(persistents.getPersistent("sysUser").getFieldInt("uid"));
        updateSystemGroups(persistentManager, userId, (String) persistents.getAttribute("role"));
        persistents.getPersistent("preferences").setField("userId", userId);
        persistents.getPersistent("preferences").add();
        persistents.getPersistent("party").setField("userId", userId);
        persistents.getPersistent("party").add();
        Integer partyId = new Integer(persistents.getPersistent("party").getFieldInt("partyId"));
        persistents.getPersistent("profile").setField("partyId", partyId);
        persistents.getPersistent("profile").add();
        persistents.getPersistent("address").setField("partyId", partyId);
        persistents.getPersistent("address").setField("category", "G");
        persistents.getPersistent("address").add();
        AkteraGroupEntry groupEntry = new AkteraGroupEntry();
        groupEntry.setGroupId(NumberTools.toIntInstance(persistents.getAttribute("newUsersGroup")));
        groupEntry.setUserId(userId);
        userDAO.createAkteraGroupEntry(groupEntry);
        request.setParameter("id", userId);
        KeelPreferencesManager.createDefaultValues(request, userId);
        try {
            Properties params = new Properties();
            params = new Properties();
            params.put("userId", userId);
            params.put("password", StringTools.digest((String) persistents.getAttribute("passwordNew")));
            ModelTools.callModel(request, "aktera.aktario.user.create-aktario-user", params);
        } catch (ModelException x) {
        }
        return userId.intValue();
    }
