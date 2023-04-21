    protected Update computeDiffUpdate(FileRecipe old_recipe, FileRecipe new_recipe, FileChannel fchannel) throws IOException {
        String[] old_diff_st = old_recipe.toDiffString();
        String[] new_diff_st = new_recipe.toDiffString();
        Diff diff = new Diff(old_diff_st, new_diff_st);
        Diff.change change = diff.diff(Diff.forwardScript);
        int bytes_common = 0;
        int bytes_different = 0;
        int old_chunk_index = 0;
        int new_chunk_index = 0;
        int new_file_offset = 0;
        List<Action> actions = new LinkedList<Action>();
        while (change != null) {
            if (logger.isDebugEnabled()) logger.debug("processing change:" + " inserted=" + change.inserted + " deleted=" + change.deleted + " line_num_deleted=" + change.line1 + " line_num_inserted=" + change.line0);
            while (old_chunk_index < change.line0) {
                ++old_chunk_index;
            }
            while (new_chunk_index < change.line1) {
                FileRecipe.Chunk chunk = new_recipe.getChunk(new_chunk_index);
                new_file_offset += chunk.length;
                bytes_common += chunk.length;
                ++new_chunk_index;
            }
            int remove_length = 0;
            for (int i = 0; i < change.deleted; ++i) {
                FileRecipe.Chunk chunk = old_recipe.getChunk(old_chunk_index);
                remove_length += chunk.length;
                ++old_chunk_index;
            }
            if (remove_length > 0) {
                Action delete = new Action();
                delete.type = ActionType.DELETE;
                delete.offset = new_file_offset;
                delete.length = remove_length;
                delete.data = new UserData[1];
                delete.data[0] = new UserData();
                delete.data[0].type = UserDataType.DIRECT;
                delete.data[0].block = MoxieUtils.MOXIE_USER_DATA_BLOCK_NULL;
                actions.add(delete);
            }
            for (int i = 0; i < change.inserted; ++i) {
                FileRecipe.Chunk chunk = new_recipe.getChunk(new_chunk_index);
                ByteBuffer buffer = ByteBuffer.allocate(chunk.length);
                fchannel.position(chunk.offset);
                while (buffer.hasRemaining()) {
                    int bytes_read = fchannel.read(buffer);
                    if (bytes_read == -1) {
                        BUG("Failed to read data to write back");
                        return ((Update) null);
                    }
                }
                UserDataBlock block = new UserDataBlock();
                block.iv = new byte[] { 0 };
                block.data = buffer.array();
                block.user_length = chunk.length;
                block.data_length = chunk.length;
                Action insert = new Action();
                insert.type = ActionType.INSERT;
                insert.offset = new_file_offset;
                insert.length = chunk.length;
                insert.data = new UserData[1];
                insert.data[0] = new UserData();
                insert.data[0].type = UserDataType.DIRECT;
                insert.data[0].block = block;
                actions.add(insert);
                new_file_offset += chunk.length;
                bytes_different += chunk.length;
                ++new_chunk_index;
            }
            change = change.link;
        }
        while (new_chunk_index < new_recipe.numChunks()) {
            FileRecipe.Chunk chunk = new_recipe.getChunk(new_chunk_index);
            bytes_common += chunk.length;
            new_chunk_index++;
        }
        double fraction = ((double) bytes_common) / ((double) bytes_common + bytes_different);
        logger.fatal("commonality: " + " bytes_common=" + bytes_common + " bytes_different=" + bytes_different + " fraction=" + fraction);
        Update update = null;
        if (actions.size() > 0) {
            update = new Update();
            update.prev_ver = old_recipe.getVersion();
            update.actions = actions.toArray(new Action[1]);
        }
        return update;
    }
