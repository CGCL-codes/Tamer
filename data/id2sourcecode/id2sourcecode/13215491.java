    @Override
    public StaticMemberData extractData(int id, StaticEntityDataIR staticEntityDataIR, SyncAppState<?> appState, MemberDataIR connectedMemberIR) {
        ValueReadAccessor readAccessor = getter.getCollectedMethod() == null ? new FieldReadAccessor(field.getCollectedField()) : new GetterReadAccessor(getter.getCollectedMethod());
        ValueWriteAccessor writeAccessor = setter.getCollectedMethod() == null ? new FieldWriteAccessor(field.getCollectedField()) : new SetterWriteAccessor(setter.getCollectedMethod());
        UpdateState annot = getCollectedAnnotation(UpdateState.class);
        TrackValue track = getCollectedAnnotation(TrackValue.class);
        ReceiveStateFrom receiveFrom = getCollectedAnnotation(ReceiveStateFrom.class);
        boolean allowInbound = ClassFilter.Eval.contains(receiveFrom.value(), staticEntityDataIR.connectedClass);
        boolean allowOutbound = ClassFilter.Eval.contains(((StaticMemberStateDataIR) connectedMemberIR).getCollectedAnnotation(ReceiveStateFrom.class).value(), staticEntityDataIR.localClass);
        if (allowInbound && allowOutbound) {
            throw new IllegalStateException("Can not send and receive state at the same time:\nlocal class: " + staticEntityDataIR.localClass + "\nconnected class: " + staticEntityDataIR.connectedClass);
        } else if (!allowInbound && !allowOutbound) {
            return null;
        }
        if (allowInbound) {
            return new InboundStaticMemberStateData(id, memberKey, appState.getModule(StateUpdateModule.class), appState.entityProvider.getInterpreter(getCollectedAnnotation(IdentityAware.class)), writeAccessor, readAccessor);
        } else if (track.value() == true) {
            return new TrackingOutboundStaticMemberStateData(id, memberKey, appState.getModule(StateUpdateModule.class), appState.entityProvider.getInterpreter(getCollectedAnnotation(IdentityAware.class)), annot.freq(), writeAccessor, readAccessor);
        } else {
            return new OutboundStaticMemberStateData(id, memberKey, appState.getModule(StateUpdateModule.class), appState.entityProvider.getInterpreter(getCollectedAnnotation(IdentityAware.class)), annot.freq(), writeAccessor, readAccessor);
        }
    }
