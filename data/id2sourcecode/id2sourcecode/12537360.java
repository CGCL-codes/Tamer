    public StaticMemberStateData(int id, MemberDataKey memberKey, StateUpdateModule updateModule, ParameterInterpreter interpreter, ValueWriteAccessor writeAccessor, ValueReadAccessor readAccessor) {
        this.id = id;
        this.memberKey = memberKey;
        this.updateModule = updateModule;
        this.interpreter = interpreter;
        this.writeAccessor = writeAccessor;
        this.readAccessor = readAccessor;
    }
