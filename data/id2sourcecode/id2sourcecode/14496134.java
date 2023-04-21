    private OpenMBeanAttributeInfoSupport innerMakeAttr() throws OpenDataException {
        OpenMBeanAttributeInfoSupport r;
        if (legal.isEmpty()) {
            if ((min != null) && (max != null)) {
                r = new OpenMBeanAttributeInfoSupport(name, desc, type, read, write, is, def, min, max);
            } else {
                r = new OpenMBeanAttributeInfoSupport(name, desc, type, read, write, is);
            }
        } else {
            Object[] arr = legal.toArray();
            r = new OpenMBeanAttributeInfoSupport(name, desc, type, read, write, is, def, arr);
        }
        return r;
    }
