    public void write(DataOutputStream out) throws IOException {
        out.writeInt(VERSION);
        out.writeUTF(this.level);
        out.writeLong(this.time);
        out.writeUTF(this.className);
        out.writeInt(this.lineNumber);
        out.writeUTF(this.message);
        out.writeUTF(this.exception);
        out.writeUTF(this.thread);
    }
