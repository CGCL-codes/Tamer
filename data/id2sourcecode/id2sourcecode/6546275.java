    @Override
    public void preStorePersistents(ModelRequest request, FormularDescriptor formular, PersistentDescriptor persistents, boolean modified) throws ModelException, PersistenceException {
        String password = (String) persistents.getAttribute("passwordNew");
        if (!StringTools.isTrimEmpty(password)) {
            if (password.equals(persistents.getAttribute("passwordNewRepeat"))) {
                persistents.getPersistent("sysUser").setField("password", StringTools.digest(password));
            }
        }
        if (NumberTools.toBool(persistents.getAttribute("deletePassword"), false)) {
            persistents.getPersistent("sysUser").setField("password", null);
        }
        String pin = (String) persistents.getAttribute("pinNew");
        if (!StringTools.isTrimEmpty(pin)) {
            if (pin.equals(persistents.getAttribute("pinNewRepeat"))) {
                persistents.getPersistent("preferences").setField("pin", pin);
            }
        }
        persistents.getPersistent("sysUser").setField("email", persistents.getPersistent("address").getField("email"));
        persistents.getPersistent("address").setField("internalLastname", StringTools.trim(persistents.getPersistent("address").getField("lastName")).toLowerCase());
        persistents.getPersistent("address").setField("internalCompany", StringTools.trim(persistents.getPersistent("address").getField("company")).toLowerCase());
    }
