    public SqlUsingModule(IKernel kernel) throws InvocationTargetException {
        super(kernel);
        try {
            _isql = (ISqlChannel) kernel().getChannel(ISqlChannel.class);
        } catch (ModuleNotFoundException e) {
            throw new InvocationTargetException(e);
        }
    }
