    public void save() throws IOException {
        try {
            readerWriter.write(this);
        } catch (SettingsException e) {
            e.printStackTrace();
        }
    }
