    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int len = in.readInt();
        for (int i = 0; i < len; i++) writeObject(in.readObject());
    }
