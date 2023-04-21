    @Transactional(propagation = Propagation.REQUIRED)
    private void addUserInternal(User newUser) {
        Date now = new Date();
        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setFullname(newUser.getFullname());
        user.setEmail(newUser.getEmail());
        user.setActiveFlag(newUser.isActiveFlag());
        user.updateAuthnPasswordValue(digester.digest(newUser.getAuthnPasswordValue()));
        user.setDateCreated(now);
        userDAO.makePersistent(user);
        Group everyoneGroup = getEveryoneGroup();
        everyoneGroup.addUser(user);
        for (Group newGroup : newUser.getGroups()) {
            Group group = groupDAO.findById(newGroup.getId());
            group.addUser(user);
        }
        auditLogger.log(now, ServerSessionUtil.getUsername(), ServerSessionUtil.getIP(), "add user", userTarget(user), true, "");
    }
