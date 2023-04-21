    public void save() {
        try {
            StringWriter writer = new StringWriter();
            readerWriter.write(writer, userDefinedEntries.values());
            String result = writer.toString();
            preferences.setValue(PreferenceKeys.USER_DEFINED_ENTRIES, result);
        } catch (IOException e) {
            throw new RuntimeException("Can't save preferences", e);
        }
    }
