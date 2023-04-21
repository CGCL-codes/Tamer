    private void recomputeDigest() {
        if (isEncrypted) {
            if ((this.password != null) && (getUrl() != null)) {
                digest = Engine.digest(password, getUrl());
            }
        }
    }
