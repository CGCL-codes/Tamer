    public ConstantString run(Session session, MaverickString[] args) throws MaverickException {
        String spec = getSpecificationVersion();
        String build = getImplementationVersion();
        session.getChannel(Session.SCREEN_CHANNEL).PRINT(session.getFactory().getConstant("MaVerick version " + spec + " (build " + build + ")"), true, session.getStatus());
        return ConstantString.RETURN_SUCCESS;
    }
