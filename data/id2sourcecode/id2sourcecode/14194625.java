    public void write(ObjectOutputStream out) throws IOException {
        out.writeInt(feedEventIndex);
        out.writeBoolean(ready);
        out.writeInt(feedletPositions.length);
        for (int i = 0; i < feedletPositions.length; i++) {
            feedletPositions[i].write(out);
        }
    }
