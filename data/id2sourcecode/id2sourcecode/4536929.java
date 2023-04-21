    public java.util.Set<Declaration> getDeclarations() {
        java.util.Set<Declaration> ret = new java.util.HashSet<Declaration>();
        if (getLocatedName().getRole() != null) {
            ret.add(getLocatedName().getRole());
        }
        java.util.Iterator<Activity> iter = getBlock().getContents().iterator();
        while (iter.hasNext()) {
            Activity act = iter.next();
            if (act instanceof RoleList) {
                ret.addAll(((RoleList) act).getRoles());
            } else if (act instanceof ChannelList) {
                ret.addAll(((ChannelList) act).getChannels());
            }
        }
        return (ret);
    }
