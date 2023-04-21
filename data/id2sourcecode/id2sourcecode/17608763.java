    public void copyFrom(InputDocument doc) throws IOException {
        boolean thisWasClosed = closed;
        closed = false;
        if (doc.encoding.equals(this.encoding)) {
            FileUtils.copyFile(doc.file, this.file);
        } else {
            open();
            BufferedReader in = doc.getBufferedReader();
            String line;
            while ((line = in.readLine()) != null) writer.println(line);
            in.close();
            if (thisWasClosed) writer.close();
        }
        closed = thisWasClosed;
    }
