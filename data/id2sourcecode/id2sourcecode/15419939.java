    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User updateUser) {
        Date now = new Date();
        String action = "update user";
        User loggedInUser = getLoggedInUser();
        if (authorizer.isAuthorized(loggedInUser, Function.UPDATE_USER)) {
            User user = userDAO.findById(updateUser.getId());
            user.setFullname(updateUser.getFullname());
            user.setEmail(updateUser.getEmail());
            user.setActiveFlag(updateUser.isActiveFlag());
            if (!updateUser.getAuthnPasswordValue().equals("")) {
                user.updateAuthnPasswordValue(digester.digest(updateUser.getAuthnPasswordValue()));
            }
            for (Group oldGroup : user.getGroups()) {
                Group group = groupDAO.findById(oldGroup.getId());
                group.removeUser(user);
            }
            Group everyoneGroup = getEveryoneGroup();
            everyoneGroup.addUser(user);
            for (Group newGroup : updateUser.getGroups()) {
                Group group = groupDAO.findById(newGroup.getId());
                group.addUser(user);
            }
            auditLogger.log(now, ServerSessionUtil.getUsername(), ServerSessionUtil.getIP(), action, userTarget(updateUser), true, "");
        } else {
            auditLogger.log(now, ServerSessionUtil.getUsername(), ServerSessionUtil.getIP(), action, userTarget(updateUser), false, "not authorized");
            throw new RuntimeException("Not Authorized!");
        }
    }
