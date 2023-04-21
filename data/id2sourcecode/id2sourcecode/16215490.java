    protected void addTrap(Object name, JSFunction f) throws JSExn {
        if (f.numFormalArgs > 1) throw new JSExn("traps must take either one argument (write) or no arguments (read)");
        boolean isRead = f.numFormalArgs == 0;
        if (!isTrappable(name, isRead)) throw new JSExn("not allowed " + (isRead ? "read" : "write") + " trap on property: " + name);
        for (Trap t = getTrap(name); t != null; t = t.next) if (t.f == f) return;
        putTrap(name, new Trap(this, name.toString(), f, (Trap) getTrap(name)));
    }
