    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(extensions.size());
        for (SpreadsheetExtension extension : extensions.values()) stream.writeObject(extension);
    }
