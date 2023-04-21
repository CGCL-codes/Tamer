    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeRepositoryLocation(ELEMENT_TO_RENAME, "Entry that should be renamed", true, true, false));
        types.add(new ParameterTypeString(NEW_ELEMENT_NAME, "New entry name", false, false));
        types.add(new ParameterTypeBoolean(OVERWRITE, "Overwrite already existing entry with same name?", false, false));
        return types;
    }
