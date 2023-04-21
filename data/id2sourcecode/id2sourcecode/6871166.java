    public boolean isEncrypted() {
        return (current_reader.isEncrypted() || current_writer.isEncrypted());
    }
