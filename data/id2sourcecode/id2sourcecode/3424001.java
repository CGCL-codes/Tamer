    public void add(Permission permission) {
        if (isReadOnly()) throw new SecurityException("readonly");
        if (!(permission instanceof PropertyPermission)) throw new IllegalArgumentException();
        PropertyPermission pp = (PropertyPermission) permission;
        String name = pp.getName();
        if (name.equals("*")) all_allowed = true;
        PropertyPermission old = (PropertyPermission) permissions.get(name);
        if (old != null) {
            if ((pp.actions | old.actions) == old.actions) pp = old; else if ((pp.actions | old.actions) != pp.actions) pp = new PropertyPermission(name, "read,write");
        }
        permissions.put(name, pp);
    }
