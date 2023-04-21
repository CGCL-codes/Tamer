    public void createPrivateProfile(Hashtable<String, String> specificData, String workspaceName) {
        PrivateProfile pp = null;
        try {
            pp = new PrivateProfile(specificData, ToolBox.generateKeyPair());
        } catch (BaseProfileNotInitializedException ex) {
        }
        this.privateProfiles.put(workspaceName, pp);
    }
