    private void writeMode() throws SLEEException, ManagementException {
        if (!isProfileWriteable()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Changing state to read-write, for profile mbean with name " + profileName + ", from table with name " + this.profileTable.getProfileTableName());
            }
            ProfileObjectImpl profileObject = profileTable.getProfile(profileName);
            profileObject.getProfileEntity().setReadOnly(false);
            state = State.write;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Already in write state, for profile mbean with name " + profileName + ", from table with name " + this.profileTable.getProfileTableName());
            }
        }
    }
