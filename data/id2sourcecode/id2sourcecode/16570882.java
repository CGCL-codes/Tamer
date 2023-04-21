    private void openNewInputFileChunk() throws Exception {
        ZipEntry entry;
        try {
            entry = connection.getDataInputStream().getNextEntry();
        } catch (IOException e) {
            throw new Exception("Problems reading new ZIPEntry", e);
        }
        if (entry != null) {
            if (entry.isDirectory()) {
                if (entry.getExtra() != null) {
                    current_file_name = entry.getName();
                    offset_in_current_file = 0;
                    portfolio = new Portfolio("Marker");
                    logger.logComment("UPLRreader-" + my_id + " No overwrite marker for <" + current_file_name + ">");
                } else {
                    openNewInputFileChunk();
                }
            } else if (entry.getName().equals("ERROR")) {
                throw new Exception("There was an error sent by the source of the files.");
            } else {
                current_file_name = entry.getName();
                offset_in_current_file = 0;
                portfolio = null;
                if (current_file_name.startsWith("CHUNK_")) {
                    int i = 6;
                    int j = current_file_name.indexOf("_", i);
                    try {
                        offset_in_current_file = Long.parseLong(current_file_name.substring(i, j));
                    } catch (NumberFormatException e) {
                        Exception ex = new Exception("Problems parsing input file chunk name <" + current_file_name + ">");
                        ex.initCause(e);
                        throw ex;
                    }
                    current_file_name = current_file_name.substring(j + 1);
                }
                if (entry.getExtra() != null) {
                    mode = entry.getExtra()[0];
                } else {
                    mode = 6;
                }
                logger.logComment("UPLReader-" + my_id + " Extracting file <" + current_file_name + ">, size <" + entry.getSize() + ">, mode <" + mode + ">, offset <" + offset_in_current_file + ">");
                data_on_stream = true;
            }
        } else {
            data_on_stream = false;
        }
    }
