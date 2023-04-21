    private void closeOpeningTag(boolean writeReturn) {
        if (!this.closed) {
            writeAttributes();
            this.closed = true;
            this.buffer.append('>');
            if (writeReturn) nextLine();
        }
    }
