    public JAASCallbackHandler(JAASRealm realm, String username, String password) {
        super();
        this.realm = realm;
        this.username = username;
        if (realm.hasMessageDigest()) {
            this.password = realm.digest(password);
        } else {
            this.password = password;
        }
    }
