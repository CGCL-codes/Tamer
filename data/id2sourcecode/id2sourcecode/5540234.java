    public static int open_command_file() {
        logger.trace("entering " + cn + ".open_command_file() start\n");
        logger.debug("open_command_file check_external_commands " + blue.check_external_commands);
        if (blue.check_external_commands == common_h.FALSE) return common_h.OK;
        logger.debug("open_command_file command_file_created " + blue.command_file_created);
        if (blue.command_file_created == common_h.TRUE) return common_h.OK;
        try {
            logger.debug("open_command_file command_file_channel " + blue.command_file);
            blue.command_file_channel = new RandomAccessFile(blue.command_file, "rw").getChannel();
        } catch (IOException ioE) {
            logger.fatal(" Error: Could not create external command file '" + blue.command_file + "' as named pipe:  If this file already exists and you are sure that another copy of Blue is not running, you should delete this file.", ioE);
            return common_h.ERROR;
        }
        if (command_file_worker_thread.init_command_file_worker_thread() == common_h.ERROR) {
            logger.fatal("Error: Could not initialize command file worker thread.");
            try {
                blue.command_file_channel.close();
            } catch (IOException ioE) {
            }
            new File(blue.command_file).delete();
            return common_h.ERROR;
        }
        blue.command_file_created = common_h.TRUE;
        logger.trace("exiting " + cn + ".open_command_file");
        return common_h.OK;
    }
