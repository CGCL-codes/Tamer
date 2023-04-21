    public void shallowMerge(Object from, Object to) throws PropertyAccessException {
        Object fromValue = accessor.readPropertyDirectly(from);
        if (fromValue == null) {
            writePropertyDirectly(to, accessor.readPropertyDirectly(to), null);
        } else {
            writePropertyDirectly(to, accessor.readPropertyDirectly(to), Fault.getToOneFault());
        }
    }
