    public void run() {
        try {
            outer_loop: while (something_to_do) {
                fte.finishedAWrite(this);
                if (chunk != null) {
                    if (chunk.hasFailed()) {
                        logger.logComment("Ignoring a read chunk that is marked as failed.");
                        continue outer_loop;
                    }
                    if (chunk.getPortfolio() != null) {
                        File marker = new File(chunk.getFileName() + "UCNOOVERWRITE");
                        marker.createNewFile();
                        logger.logComment("Writing no-overwrite marker to <" + marker + ">");
                        continue outer_loop;
                    }
                    FileChunk target = null;
                    synchronized (open_files) {
                        Iterator iterator = open_files.iterator();
                        while (iterator.hasNext()) {
                            FileChunk fc = (FileChunk) iterator.next();
                            if (fc.name.equals(chunk.getFileName())) {
                                while (fc != null) {
                                    if (fc.position == chunk.getStartByte()) {
                                        fc.appendChunk(chunk);
                                        target = fc;
                                        fc = null;
                                    } else if (fc.end_of_current_write == chunk.getStartByte()) {
                                        fc.appendChunk(chunk);
                                        chunk = null;
                                        continue outer_loop;
                                    } else {
                                        fc = fc.next;
                                    }
                                }
                                break;
                            }
                        }
                        if (target == null) {
                            target = new FileChunk(chunk);
                            target.actual_file = new File(base_dir, target.name);
                            target.actual_file = new File(target.actual_file.getParent(), "CHUNK_" + chunk.getStartByte() + "_" + target.actual_file.getName());
                            if (!target.actual_file.getParentFile().exists()) {
                                if (!target.actual_file.getParentFile().mkdirs()) {
                                    throw new FileNotFoundException("Failed to create directory <" + target.actual_file.getParentFile() + ">");
                                }
                            }
                            target.stream = new FileOutputStream(target.actual_file);
                            target.position = chunk.getStartByte();
                            iterator = open_files.iterator();
                            FileChunk existing = null;
                            while (iterator.hasNext()) {
                                FileChunk fc = (FileChunk) iterator.next();
                                if (fc.name.equals(target.name)) {
                                    existing = fc;
                                    break;
                                }
                            }
                            if (existing == null) {
                                open_files.add(target);
                            } else {
                                if (existing.start_byte > target.start_byte) {
                                    target.next = existing;
                                    open_files.remove(existing);
                                    open_files.add(target);
                                } else {
                                    while (existing.next != null && existing.next.start_byte <= target.start_byte) {
                                        existing = existing.next;
                                    }
                                    if (existing.start_byte == target.start_byte) throw new Exception("A received chunk has same start byte as an existing chunk <" + existing.start_byte + ">");
                                    FileChunk temp = existing.next;
                                    existing.next = target;
                                    target.next = temp;
                                }
                            }
                        }
                    }
                    Chunk to_write = target.getChunk();
                    while (to_write != null) {
                        logger.logComment(Thread.currentThread().getName() + " is writing <" + to_write.getFileName() + "> <" + to_write.getStartByte() + "> <" + chunk.getChunkLength() + ">");
                        target.stream.write(to_write.getBuffer(), 0, (int) to_write.getChunkLength());
                        target.stream.flush();
                        target.position += to_write.getChunkLength();
                        synchronized (open_files) {
                            to_write = target.getChunk();
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            terminate(true);
            fte.notifyError(chunk, e);
        } catch (IOException e) {
            terminate(true);
            fte.notifyError(chunk, e);
        } catch (Exception e) {
            terminate(true);
            fte.notifyError(chunk, e);
        } finally {
            eru.notifyTerminated(this);
            eru = null;
            fte = null;
            chunk = null;
        }
    }
