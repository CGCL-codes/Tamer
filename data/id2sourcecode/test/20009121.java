    public static void splitStreamToFiles(final InputStream ao_input, final String as_base_file_name, final long an_split_size) throws IOException, FileNotFoundException {
        long ln_position = 0;
        int ln_part = 1;
        int ln_readed = -1, ln_temp = 0;
        final byte[] lh_buffer = new byte[32768];
        OutputStream lo_out = new FileOutputStream(as_base_file_name + "part" + ln_part);
        while ((ln_readed = ao_input.read(lh_buffer)) != -1) {
            ln_position += ln_readed;
            do {
                if (ln_position > an_split_size) {
                    lo_out.write(lh_buffer, 0, ln_temp = ln_readed - (int) (ln_position - an_split_size));
                    lo_out.flush();
                    lo_out.close();
                    ln_part++;
                    lo_out = new FileOutputStream(as_base_file_name + "part" + ln_part);
                    lo_out.write(lh_buffer, ln_temp, ln_readed - ln_temp);
                    ln_position = ln_readed - ln_temp;
                } else {
                    lo_out.write(lh_buffer, 0, ln_readed);
                }
            } while (ln_position > an_split_size);
        }
        lo_out.flush();
        lo_out.close();
    }
