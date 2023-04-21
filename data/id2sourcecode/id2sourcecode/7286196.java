    public boolean open() throws HL7IOException {
        if (this.statusValue == HL7SocketStream.OPEN) {
            return true;
        }
        if (this.directive == HL7SocketStream.WRITER) {
            return this.openWriter();
        } else if (this.directive == HL7SocketStream.READER) {
            return this.openReader();
        }
        throw new HL7IOException("HL7SocketStream.open():Not a reader. Not a writer.", HL7IOException.INCONSISTENT_STATE);
    }
