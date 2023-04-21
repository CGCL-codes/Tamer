    NamedByteArrayOutputStream writeFile(DataInputStream is, String name, int bytes) throws IOException {
        byte buf[] = new byte[1024];
        NamedByteArrayOutputStream result = new NamedByteArrayOutputStream(name);
        DataOutputStream os = new DataOutputStream(result);
        while (bytes > 0) {
            int read = is.read(buf, 0, bytes > 1024 ? 1024 : bytes);
            os.write(buf, 0, read);
            bytes -= read;
        }
        os.close();
        return result;
    }
