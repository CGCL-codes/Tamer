    public BaseDnMapper(BaseDn baseDn, Method readMethod, Method writeMethod) throws OdmException {
        super(readMethod, writeMethod);
        this.baseDn = baseDn;
    }
