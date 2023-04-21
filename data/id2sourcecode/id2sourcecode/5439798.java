    private byte[] readEntry(String name) throws FileNotFoundException {
        try {
            ZipEntry entry = jar.getEntry(name);
            if (entry == null) throw new FileNotFoundException(name);
            InputStream in = jar.getInputStream(entry);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[BUFFER_SIZE];
            while (true) {
                int read = in.read(buf);
                if (read == -1) break;
                out.write(buf, 0, read);
            }
            return out.toByteArray();
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            MinigolfException.throwToDisplay(e);
        }
        return null;
    }
