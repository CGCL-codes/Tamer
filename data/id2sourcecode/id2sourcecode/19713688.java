    public final void readURL(URLConnection url) throws IOException {
        DataInputStream in = new DataInputStream(url.getInputStream());
        printHeader(url);
        try {
            while (true) {
                writeChar((char) in.readUnsignedByte());
            }
        } catch (EOFException e) {
            if (output) {
                verbose("\n");
            }
            verbose(commandName + ": Read " + count + " bytes from " + url.getURL());
        } catch (IOException e) {
            buffer.append(e + ": " + e.getMessage());
            if (output) {
                verbose("\n");
            }
            verbose(commandName + ": Read " + count + " bytes from " + url.getURL());
        }
    }
