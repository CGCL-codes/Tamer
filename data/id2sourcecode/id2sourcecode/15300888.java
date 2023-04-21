    protected boolean handleManualMapping(final String className, final String readHandler, final String writeHandler) throws ObjectDescriptionException {
        if (!this.manualMappings.containsKey(className)) {
            final Class loadedClass = loadClass(className);
            this.manualMappings.put(loadedClass, new ManualMappingDefinition(loadedClass, readHandler, writeHandler));
            return true;
        }
        return false;
    }
