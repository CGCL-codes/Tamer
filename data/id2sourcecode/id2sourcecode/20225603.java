    protected int readWritePiece(RandomAccessFile raf, DirectByteBuffer[] buffers, int piece_number, int piece_offset, boolean is_read) throws FMFileManagerException {
        String str = is_read ? "read" : "write";
        if (piece_number >= num_pieces) {
            throw (new FMFileManagerException("Attempt to " + str + " piece " + piece_number + ": last=" + num_pieces));
        }
        int this_piece_size = piece_number == 0 ? first_piece_length : (piece_number == (num_pieces - 1) ? last_piece_length : piece_size);
        final int piece_space = this_piece_size - piece_offset;
        if (piece_space <= 0) {
            throw (new FMFileManagerException("Attempt to " + str + " piece " + piece_number + ", offset " + piece_offset + " - no space in piece"));
        }
        int rem_space = piece_space;
        int[] limits = new int[buffers.length];
        for (int i = 0; i < buffers.length; i++) {
            DirectByteBuffer buffer = buffers[i];
            limits[i] = buffer.limit(SS_FILE);
            int rem = buffer.remaining(SS_FILE);
            if (rem > rem_space) {
                buffer.limit(SS_FILE, buffer.position(SS_FILE) + rem_space);
                rem_space = 0;
            } else {
                rem_space -= rem;
            }
        }
        try {
            long piece_start = getPieceOffset(raf, piece_number, !is_read);
            if (TRACE) {
                System.out.println(str + " to " + piece_number + "/" + piece_offset + "/" + this_piece_size + "/" + piece_space + "/" + rem_space + "/" + piece_start);
            }
            if (piece_start == -1) {
                return (0);
            }
            long piece_io_position = piece_start + piece_offset;
            if (is_read) {
                delegate.read(raf, buffers, piece_io_position);
            } else {
                delegate.write(raf, buffers, piece_io_position);
            }
            return (piece_space - rem_space);
        } finally {
            for (int i = 0; i < buffers.length; i++) {
                buffers[i].limit(SS_FILE, limits[i]);
            }
        }
    }
