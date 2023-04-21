    public void shallowMerge(Object from, Object to) throws PropertyAccessException {
        writePropertyDirectly(to, accessor.readPropertyDirectly(to), accessor.readPropertyDirectly(from));
    }
