    private static void writeStreamToFile(InputStream input_stream, String output_path) throws IOException {
        (new File(output_path)).createNewFile();
        BufferedOutputStream output_stream = new BufferedOutputStream(new FileOutputStream(output_path), 8 * 1024);
        byte[] buffer = new byte[1024];
        int bytes_read;
        while ((bytes_read = input_stream.read(buffer)) >= 0) output_stream.write(buffer, 0, bytes_read);
        output_stream.close();
    }
