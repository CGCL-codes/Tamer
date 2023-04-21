    public boolean chmod(boolean read, boolean write, boolean execute) throws RoctopusException {
        if (!exists()) {
            throw new RoctopusException("A file " + getPath() + " doesn't exist!");
        }
        try {
            VsiteTh vsth = getUnicoreStorage().getUnicoreSite().getVSiteThForThisUnicoreSite();
            ChangePermissions cp = new ChangePermissions("chmod", new ResourceSet(this.getUnicoreStorage().getUnicoreStorageResource()), getPath(), read, write, execute);
            if (cp == null) throw new RoctopusException("The required storage is unknown!");
            ChangePermissions_Outcome cpo = (ChangePermissions_Outcome) executeAction(cp, vsth);
            log.info("the status of the chmod for the file " + getPath() + " is " + cpo.getStatus());
            if (!cpo.getStatus().isEquivalent(AbstractActionStatus.SUCCESSFUL)) {
                log.error("couldn't chmod file " + getPath());
                return false;
            }
        } catch (Exception e) {
            throw new RoctopusException("cannot chmod a file!", e);
        }
        try {
            cache();
        } catch (Exception e) {
            throw new RoctopusException("Couldn't collect information about the file " + getPath() + "!");
        }
        return true;
    }
