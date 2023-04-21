    public void pieceReorderTest(CacheFileManagerImpl manager) {
        try {
            Random random = new Random(0);
            int num_files = 100;
            int piece_size = 1024;
            int file_size_average = piece_size * 30;
            int chunk_fixed_size = 0;
            int chunk_random_size = 1024;
            int write_order = 2;
            int[] file_sizes = new int[num_files];
            for (int i = 0; i < num_files; i++) {
                file_sizes[i] = random.nextInt(2 * file_size_average) + 1;
            }
            final File control_dir = new File("C:\\temp\\filetestcontrol");
            FileUtil.recursiveDelete(control_dir);
            control_dir.mkdirs();
            File torrent_file = new File("C:\\temp\\filetest.torrent");
            torrent_file.delete();
            File source_file_or_dir;
            File target_file_or_dir;
            if (num_files == 1) {
                source_file_or_dir = new File("C:\\temp\\filetest1.dat");
                target_file_or_dir = new File("C:\\temp\\filetest2.dat");
                source_file_or_dir.delete();
                target_file_or_dir.delete();
            } else {
                source_file_or_dir = new File("C:\\temp\\filetest1.dir");
                target_file_or_dir = new File("C:\\temp\\filetest2.dir");
                FileUtil.recursiveDelete(source_file_or_dir);
                FileUtil.recursiveDelete(target_file_or_dir);
                source_file_or_dir.mkdirs();
                target_file_or_dir.mkdirs();
            }
            File[] source_files = new File[num_files];
            File[] target_files = new File[num_files];
            RandomAccessFile[] source_file_rafs = new RandomAccessFile[num_files];
            final TOTorrent torrent;
            for (int i = 0; i < num_files; i++) {
                File source_file;
                File target_file;
                if (num_files == 1) {
                    source_file = source_file_or_dir;
                    target_file = target_file_or_dir;
                } else {
                    source_file = new File(source_file_or_dir, "file" + i);
                    target_file = new File(target_file_or_dir, "file" + i);
                }
                source_files[i] = source_file;
                target_files[i] = target_file;
                FileOutputStream fos = new FileOutputStream(source_file);
                byte[] buffer = new byte[64 * 1024];
                int rem = file_sizes[i];
                while (rem > 0) {
                    random.nextBytes(buffer);
                    int to_write = rem > buffer.length ? buffer.length : rem;
                    fos.write(buffer, 0, to_write);
                    rem -= to_write;
                }
                fos.close();
                source_file_rafs[i] = new RandomAccessFile(source_file, "r");
            }
            torrent = TOTorrentFactory.createFromFileOrDirWithFixedPieceLength(source_file_or_dir, new URL("http://a.b.c/"), piece_size).create();
            final TOTorrentFile[] torrent_files = torrent.getFiles();
            for (int i = 0; i < torrent_files.length; i++) {
                TOTorrentFile tf = torrent_files[i];
                String rel_path = tf.getRelativePath();
                boolean found = false;
                for (int j = 0; j < source_files.length; j++) {
                    if (source_files[j].getName().equals(rel_path)) {
                        found = true;
                        if (j != i) {
                            int temp = file_sizes[i];
                            file_sizes[i] = file_sizes[j];
                            file_sizes[j] = temp;
                            File femp = source_files[i];
                            source_files[i] = source_files[j];
                            source_files[j] = femp;
                            femp = target_files[i];
                            target_files[i] = target_files[j];
                            target_files[j] = femp;
                            RandomAccessFile remp = source_file_rafs[i];
                            source_file_rafs[i] = source_file_rafs[j];
                            source_file_rafs[j] = remp;
                        }
                        break;
                    }
                }
                if (!found) {
                    Debug.out("eh?");
                    return;
                }
            }
            CacheFile[] cache_files = new CacheFile[torrent_files.length];
            for (int i = 0; i < torrent_files.length; i++) {
                final int f_i = i;
                File target_file = target_files[i];
                final File source_file = source_files[i];
                System.out.println("file " + i + ": e_size=" + file_sizes[i] + ", t_size=" + torrent_files[i].getLength() + ", d_size=" + source_file.length());
                cache_files[i] = manager.createFile(new CacheFileOwner() {

                    public String getCacheFileOwnerName() {
                        return (source_file.getAbsolutePath());
                    }

                    public TOTorrentFile getCacheFileTorrentFile() {
                        return (torrent_files[f_i]);
                    }

                    public File getCacheFileControlFileDir() {
                        return (control_dir);
                    }

                    public int getCacheMode() {
                        return (CacheFileOwner.CACHE_MODE_NO_CACHE);
                    }
                }, target_file, CacheFile.CT_PIECE_REORDER);
                cache_files[i].setAccessMode(CacheFile.CF_WRITE);
            }
            List<Chunk> chunks = new ArrayList<Chunk>();
            List<Chunk>[] piece_map = new List[torrent.getNumberOfPieces()];
            {
                long pos = 0;
                int file_index = 0;
                long file_offset = 0;
                long total_size = torrent.getSize();
                long rem = total_size;
                while (rem > 0) {
                    long chunk_length;
                    if (chunk_fixed_size != 0) {
                        chunk_length = chunk_fixed_size;
                    } else {
                        chunk_length = random.nextInt(chunk_random_size) + 1;
                    }
                    if (rem < chunk_length) {
                        chunk_length = (int) rem;
                    }
                    List<ChunkSlice> slices = new ArrayList<ChunkSlice>();
                    Chunk chunk = new Chunk(pos, chunk_length, slices);
                    chunks.add(chunk);
                    while (chunk_length > 0) {
                        long file_size = file_sizes[file_index];
                        long file_rem = file_size - file_offset;
                        long avail = Math.min(file_rem, chunk_length);
                        if (avail > 0) {
                            int piece_start = (int) (pos / piece_size);
                            rem -= avail;
                            pos += avail;
                            int piece_end = (int) ((pos - 1) / piece_size);
                            slices.add(new ChunkSlice(file_index, file_offset, avail, piece_start, piece_end));
                        }
                        chunk_length -= avail;
                        if (chunk_length > 0) {
                            file_offset = 0;
                            file_index++;
                        } else {
                            file_offset += avail;
                            break;
                        }
                    }
                    int piece_start = slices.get(0).getPieceStart();
                    int piece_end = slices.get(slices.size() - 1).getPieceEnd();
                    for (int i = piece_start; i <= piece_end; i++) {
                        if (piece_map[i] == null) {
                            piece_map[i] = new ArrayList<Chunk>();
                        }
                        piece_map[i].add(chunk);
                    }
                    chunk.setPieces(piece_start, piece_end);
                    System.out.println(chunk.getString());
                }
            }
            for (int i = 0; i < piece_map.length; i++) {
                System.out.println(i + ": " + piece_map[i].size());
            }
            while (chunks.size() > 0) {
                Chunk chunk;
                if (write_order == 0) {
                    chunk = chunks.remove(0);
                } else if (write_order == 1) {
                    chunk = chunks.remove(chunks.size() - 1);
                } else {
                    chunk = chunks.remove(random.nextInt(chunks.size()));
                }
                System.out.println("Processing chunk " + chunk.getString());
                List<ChunkSlice> slices = new ArrayList<ChunkSlice>(chunk.getSlices());
                if (write_order == 1) {
                    Collections.reverse(slices);
                }
                for (ChunkSlice slice : slices) {
                    int file_index = slice.getFileIndex();
                    long file_offset = slice.getFileOffset();
                    long length = slice.getLength();
                    System.out.println("Processing slice " + slice.getString() + "[file size=" + file_sizes[file_index]);
                    DirectByteBuffer buffer = DirectByteBufferPool.getBuffer(DirectByteBuffer.AL_OTHER, (int) length);
                    try {
                        RandomAccessFile raf = source_file_rafs[file_index];
                        raf.seek(file_offset);
                        raf.getChannel().read(buffer.getBuffer(DirectByteBuffer.SS_EXTERNAL));
                        buffer.flip(DirectByteBuffer.SS_EXTERNAL);
                        cache_files[file_index].write(buffer, file_offset);
                    } finally {
                        buffer.returnToPool();
                    }
                }
                chunk.setDone();
                int chunk_piece_start = chunk.getPieceStart();
                int chunk_piece_end = chunk.getPieceEnd();
                for (int i = chunk_piece_start; i <= chunk_piece_end; i++) {
                    List<Chunk> pieces = piece_map[i];
                    boolean complete = true;
                    for (Chunk c : pieces) {
                        if (!c.isDone()) {
                            complete = false;
                            break;
                        }
                    }
                    if (complete) {
                        for (ChunkSlice slice : slices) {
                            if (i >= slice.getPieceStart() && i <= slice.getPieceEnd()) {
                                long piece_offset = i * piece_size;
                                int piece_length;
                                if (i < piece_map.length - 1) {
                                    piece_length = piece_size;
                                } else {
                                    long total = torrent.getSize();
                                    piece_length = (int) (total - (total / piece_size) * piece_size);
                                    if (piece_length == 0) {
                                        piece_length = piece_size;
                                    }
                                }
                                DirectByteBuffer piece_data = DirectByteBufferPool.getBuffer(DirectByteBuffer.AL_OTHER, piece_length);
                                long pos = 0;
                                int file_index = 0;
                                int rem = piece_length;
                                while (rem > 0) {
                                    long file_size = file_sizes[file_index];
                                    long file_end = pos + file_size;
                                    long avail = file_end - piece_offset;
                                    if (avail > 0) {
                                        int to_use = (int) Math.min(avail, rem);
                                        long file_offset = piece_offset - pos;
                                        int lim = piece_data.limit(DirectByteBuffer.AL_OTHER);
                                        piece_data.limit(DirectByteBuffer.AL_OTHER, piece_data.position(DirectByteBuffer.AL_OTHER) + to_use);
                                        cache_files[file_index].read(piece_data, file_offset, CacheFile.CP_NONE);
                                        piece_data.limit(DirectByteBuffer.AL_OTHER, lim);
                                        piece_offset += to_use;
                                        rem -= to_use;
                                    }
                                    file_index++;
                                    pos += file_size;
                                }
                                try {
                                    cache_files[slice.getFileIndex()].setPieceComplete(i, piece_data);
                                } finally {
                                    piece_data.returnToPool();
                                }
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < num_files; i++) {
                source_file_rafs[i].close();
                cache_files[i].close();
                byte[] buffer1 = new byte[256 * 1024];
                byte[] buffer2 = new byte[256 * 1024];
                if (source_files[i].length() != target_files[i].length()) {
                    System.err.println("File sizes differ for " + i);
                }
                FileInputStream fis1 = new FileInputStream(source_files[i]);
                FileInputStream fis2 = new FileInputStream(target_files[i]);
                long len = file_sizes[i];
                long pos = 0;
                boolean failed = false;
                while (len > 0) {
                    int avail = (int) Math.min(len, buffer1.length);
                    int r1 = fis1.read(buffer1, 0, avail);
                    int r2 = fis2.read(buffer2, 0, avail);
                    if (r1 != r2) {
                        System.err.println("read lens different: file=" + i + ",pos=" + pos);
                        failed = true;
                        break;
                    } else {
                        if (Arrays.equals(buffer1, buffer2)) {
                            len -= r1;
                            pos += r1;
                        } else {
                            int diff_at = -1;
                            for (int j = 0; j < avail; j++) {
                                if (buffer1[j] != buffer2[j]) {
                                    diff_at = j;
                                    break;
                                }
                            }
                            System.err.println("mismatch: file=" + i + ",pos=" + pos + " + " + diff_at);
                            failed = true;
                            break;
                        }
                    }
                }
                if (!failed) {
                    System.out.println("file " + i + ": matched " + pos + " of " + file_sizes[i]);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
