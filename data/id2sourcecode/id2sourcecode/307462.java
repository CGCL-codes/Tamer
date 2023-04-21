    protected final String getInvokeMethod(World.Type t, List<World.Param> params, World.Decl d, String name) {
        StringBuffer s = new StringBuffer(methodThiz(getGetMethodName(name)));
        s.append("(");
        boolean first = true;
        for (World.Param p : d.getParams()) {
            if (!first) s.append(",");
            first = false;
            if (p.getType() == null) {
            } else {
                s.append(p.getName());
            }
        }
        s.append(")");
        return s.toString();
    }
