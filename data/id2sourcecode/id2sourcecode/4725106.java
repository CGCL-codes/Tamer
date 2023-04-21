    private void _readwrite(long start_pos, long stop_pos, UploadResults current_results) throws IOException {
        start_pos += 2;
        stop_pos -= 2;
        int count = 0;
        file.seek(start_pos);
        long file_length = stop_pos - start_pos;
        long num_reads = file_length / buffer.length;
        int last_read = (int) (file_length % buffer.length);
        for (long loop = 0; loop < num_reads; loop++) {
            if ((count = file.read(buffer, 0, buffer.length)) == -1) {
                LOG.error("ERROR! Read operation has failed!");
            } else {
                current_results.write(buffer, 0, buffer.length);
            }
        }
        file.read(buffer, 0, last_read);
        current_results.write(buffer, 0, last_read);
        current_results.close();
    }
