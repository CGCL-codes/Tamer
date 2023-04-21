    public ChildMapper(Child child, Method readMethod, Method writeMethod) throws OdmException {
        super(readMethod, writeMethod, false);
        this.childAnnotation = child;
        this.childOdmPojo = OdmPojo.getInstance(getCoreClass());
    }
