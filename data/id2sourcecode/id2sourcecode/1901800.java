    public void parseInputStream(InputStream in) throws IOException, InputSizeToBig, BareLFException {
        while (true) {
            readSize = in.read(buffer);
            if (readSize < 0) throw new IOException("Negative byte count read");
            currentSize += readSize;
            bos.write(buffer, 0, readSize);
            if (currentSize > maxMessageSize) throw new InputSizeToBig();
            if (checkEOS()) break;
        }
    }
