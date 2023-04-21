    public void put(String name, String value, String readPerm, String writePerm) {
        SysConfig cfg = null;
        if (prefix != null) name = prefix + name;
        try {
            Transaction tx = db.beginTransaction();
            try {
                cfg = (SysConfig) db.session().get(SysConfig.class, name);
            } catch (ObjectNotFoundException e) {
                cfg = new SysConfig();
                cfg.setId(name);
                cfg.setReadPerm(readPerm);
                cfg.setWritePerm(writePerm);
                db.session().save(cfg);
            }
            cfg.setValue(value);
            tx.commit();
        } catch (HibernateException e) {
            db.getLog().warn(e);
        }
    }
