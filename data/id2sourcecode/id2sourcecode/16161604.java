    public static void inputToOutput(InputStream input, OutputStream output) {
        try {
            byte[] byteBuffer = new byte[8192];
            int amount;
            while ((amount = input.read(byteBuffer)) >= 0) output.write(byteBuffer, 0, amount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
