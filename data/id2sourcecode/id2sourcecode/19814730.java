    protected byte[] enhanceClass(ClassReader reader, YielderInformationContainer info) {
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        CastChecker caster = new CastChecker(writer);
        StateKeeper stateKeeper = new StateKeeper(caster, info);
        LocalVariablePromoter promoter = new LocalVariablePromoter(stateKeeper, info);
        reader.accept(promoter, 0);
        return writer.toByteArray();
    }
