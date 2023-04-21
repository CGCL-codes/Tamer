    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        pid = in.readInt();
        eid = in.readInt();
        quorumWeaks = (TimestampValuePair) in.readObject();
        writeSet = (HashSet<TimestampValuePair>) in.readObject();
    }
