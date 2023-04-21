    private void fromInputToOutputStream(InputStream input, OutputStream output) {
        try {
            int readBytes = 0;
            byte[] buffer = new byte[10000];
            while ((readBytes = input.read(buffer, 0, 10000)) != -1) {
                output.write(buffer, 0, readBytes);
            }
            input.close();
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
