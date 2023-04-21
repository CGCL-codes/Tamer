    public OutboundStaticMemberStateData(int id, MemberDataKey memberKey, StateUpdateModule updateModule, ParameterInterpreter interpreter, float updateRate, ValueWriteAccessor writeAccessor, ValueReadAccessor readAccessor) {
        super(id, memberKey, updateModule, interpreter, writeAccessor, readAccessor);
        this.updateRate = updateRate;
        sinceLastUpdate = 0f;
    }
