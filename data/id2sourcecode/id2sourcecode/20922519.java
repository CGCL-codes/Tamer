    public Object readObjectDialogMode(String filename) {
        return readObject(filename).getObject();
    }

    public LoaderThread readObject(String filename) {
        return readObject(filename, null);
    }

    public LoaderThread readObject(String filename, Observer observer) {
        LoaderThread thread = new LoaderThread(this, filename);
