    public IChannel getChannel(String cname) throws ModuleNotFoundException {
        try {
            return getChannel(Class.forName(cname));
        } catch (ClassNotFoundException e) {
            throw new ModuleNotFoundException(e, this.getClass().getCanonicalName(), cname, ErrorCode.ClassNotLoadedError.getCode());
        }
    }
