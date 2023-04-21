    @Override
    public void open(String name) {
        if (directory == null) drive_status.setStatus(DriveStatus.NO_DISK, 0, 0); else if (name.equals("$")) {
            buffer = ByteBuffer.allocate(MAX_FILE_BUF);
            buffer.clear();
            directory.getDirectory(buffer);
            buffer.flip();
            drive_status.setStatus(DriveStatus.OK, 0, 0);
        } else {
            String file_path = directory.getFilePath(name);
            if (file_path != null) {
                FileInputStream file;
                try {
                    file = new FileInputStream(file_path);
                    if (file != null) {
                        buffer = ByteBuffer.allocate(MAX_FILE_BUF);
                        buffer.clear();
                        file.getChannel().read(buffer);
                        buffer.flip();
                        drive_status.setStatus(DriveStatus.OK, 0, 0);
                        return;
                    }
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                    drive_status.setStatus(DriveStatus.READ_ERROR_SYNC, 0, 0);
                }
            }
            drive_status.setStatus(DriveStatus.FILE_NOT_FOUND, 0, 0);
        }
    }
