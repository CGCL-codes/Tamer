    private boolean validate() throws Exception {
        boolean valid = false;
        LogOperationFacade log = null;
        Digest digest = new Digest();
        User pUser = new User();
        pUser.setDescription(user);
        if (conn == null) {
            DaoOperationFacade connLog = new DaoJpaHibernate(pUser, "log_post", null);
            log = new LogDao(connLog, "login");
            conn = new DaoJpaHibernate(pUser, "ayto_post", log);
        }
        conn.begin();
        pUser = (User) conn.findObjectDescription(pUser);
        conn.commit();
        if (pUser == null) {
            return false;
        }
        String hash = pUser.getPassword();
        if (hash != null && password != null && password.length > 0 && pUser.isActive()) {
            valid = hash.equals(digest.digest(new String(password)));
        }
        if (valid) {
            Context ctx = new Context();
            ctx.setPerson(pUser);
            ctx.setConn(conn);
            ctx.setLog(log);
            ContextSessioI ctxS = new ContextSessio();
            ctxS.altaSessio(ctx);
        }
        return valid;
    }
