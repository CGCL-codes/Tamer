    public String save() {
        final User userFromDb = userService.getUser(this.user.getId());
        if (userFromDb == null) {
            throw new IllegalStateException("User with id " + user.getId() + " not found but was expected to exist");
        }
        final User duplicateUserFromDb = userService.getUser(user.getUsername());
        if (duplicateUserFromDb != null && !userFromDb.getId().equals(duplicateUserFromDb.getId())) {
            LOGGER.warn("Duplicate user name (" + user.getUsername() + ") for id " + userFromDb.getId());
            addFieldError("username", getText("error.duplicateUsername"));
            return INPUT;
        }
        if (!StringUtils.isBlank(this.password)) {
            userFromDb.setPassword(this.stringDigester.digest(this.password));
        } else {
            addActionMessage("Your password has not been changed.");
        }
        userFromDb.setCompany(this.user.getCompany());
        userFromDb.setEmail(this.user.getEmail());
        userFromDb.setUsername(this.user.getEmail());
        userFromDb.setFax(this.user.getFax());
        userFromDb.setFirstName(this.user.getFirstName());
        userFromDb.setLastName(this.user.getLastName());
        userFromDb.setPhone(this.user.getPhone());
        userService.updateUser(userFromDb);
        addActionMessage(getText("class.EditUserAcion.success"));
        User securityContextUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        securityContextUser.setCompany(this.user.getCompany());
        securityContextUser.setEmail(this.user.getEmail());
        securityContextUser.setFax(this.user.getFax());
        securityContextUser.setFirstName(this.user.getFirstName());
        securityContextUser.setLastName(this.user.getLastName());
        securityContextUser.setPassword(this.user.getPassword());
        securityContextUser.setPhone(this.user.getPhone());
        securityContextUser.setUsername(this.user.getUsername());
        return SUCCESS;
    }
