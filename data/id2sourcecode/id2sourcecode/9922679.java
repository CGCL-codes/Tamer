    protected FileRecipe computeRecipe(String fs_path, String cache_path) {
        FileRecipe recipe = new FileRecipe();
        try {
            File f = new File(cache_path);
            FileInputStream f_in = new FileInputStream(f);
            FileChannel f_channel = f_in.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(BLOCK_SIZE);
            int num_blocks = 0;
            int fpos = 0;
            boolean eof = false;
            while (!eof) {
                do {
                    int read_length = f_channel.read(buffer);
                    eof = (read_length == -1);
                } while (buffer.hasRemaining() && (!eof));
                if (buffer.remaining() < BLOCK_SIZE) {
                    int bytes_read = buffer.position();
                    SecureHash hash = new SHA1Hash(buffer.array());
                    recipe.addChunk(hash, bytes_read, fpos);
                    fpos += bytes_read;
                    num_blocks++;
                }
                buffer.clear();
            }
            if (logger.isInfoEnabled()) logger.info("computed block recipe: path=" + fs_path + " file_size=" + fpos + " num_blocks=" + num_blocks);
        } catch (Exception e) {
            BUG("Failed to compute file recipe of file in local cache: " + "path=" + fs_path + " exception=" + e);
        }
        return recipe;
    }
