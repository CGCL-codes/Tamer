    protected URL checkURL(URL _url) {
        try {
            SecurityManager security = System.getSecurityManager();
            if (security != null) {
                URLConnection urlConnection = _url.openConnection();
                Permission perm = urlConnection.getPermission();
                if (perm != null) {
                    security.checkPermission(perm);
                }
            }
        } catch (Exception excp) {
            if (_url != null) {
                this.logger.info(this.messages.getMessage("fr.macymed.modulo.platform.loader.ModuleClassLoader.urlerror", new Object[] { _url }), excp);
            } else {
                this.logger.info(this.messages.getMessage("fr.macymed.modulo.platform.loader.ModuleClassLoader.urlerror", new Object[] { "null" }), excp);
            }
            return null;
        }
        return _url;
    }
