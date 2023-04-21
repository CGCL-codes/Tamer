    protected String captureRawInput(String prompt) {
        try {
            if (prompt != null) writer.print("\n" + prompt);
            String read = reader.readLine();
            if (read == null) throw new UserInterfaceException(this, "No more input available for CLI");
            if (echo) writer.println(read);
            return read;
        } catch (IOException e) {
            throw ThrowableManagerRegistry.caught(new UserInterfaceException(this, e));
        }
    }
