    private void readFromRemote(String path) throws IOException {
        String cmd;
        String[] cmdParts = new String[3];
        writeOk();
        while (true) {
            log.debug("Waiting for command");
            try {
                cmd = readString();
            } catch (EOFException e) {
                return;
            }
            log.debug("Got command '" + cmd + "'");
            char cmdChar = cmd.charAt(0);
            switch(cmdChar) {
                case 'E':
                    writeOk();
                    return;
                case 'T':
                    log.error("SCP time not currently supported");
                    writeError("WARNING: This server does not currently support the SCP time command");
                    break;
                case 'C':
                case 'D':
                    parseCommand(cmd, cmdParts);
                    FileAttributes attr = null;
                    try {
                        log.debug("Getting attributes for current destination (" + path + ")");
                        attr = nfs.getFileAttributes(path);
                    } catch (FileNotFoundException fnfe) {
                        log.debug("Current destination not found");
                    }
                    String targetPath = path;
                    String name = cmdParts[2];
                    if ((attr != null) && attr.isDirectory()) {
                        log.debug("Target is a directory");
                        targetPath += ('/' + name);
                    }
                    FileAttributes targetAttr = null;
                    try {
                        log.debug("Getting attributes for target destination (" + targetPath + ")");
                        targetAttr = nfs.getFileAttributes(targetPath);
                    } catch (FileNotFoundException fnfe) {
                        log.debug("Target destination not found");
                    }
                    if (cmdChar == 'D') {
                        log.debug("Got directory request");
                        if (targetAttr != null) {
                            if (!targetAttr.isDirectory()) {
                                String msg = "Invalid target " + name + ", must be a directory";
                                writeError(msg);
                                throw new IOException(msg);
                            }
                        } else {
                            try {
                                log.debug("Creating directory " + targetPath);
                                if (!nfs.makeDirectory(targetPath)) {
                                    String msg = "Could not create directory: " + name;
                                    writeError(msg);
                                    throw new IOException(msg);
                                } else {
                                    log.debug("Setting permissions on directory");
                                    attr.setPermissionsFromMaskString(cmdParts[0]);
                                }
                            } catch (FileNotFoundException e1) {
                                writeError("File not found");
                                throw new IOException("File not found");
                            } catch (PermissionDeniedException e1) {
                                writeError("Permission denied");
                                throw new IOException("Permission denied");
                            }
                        }
                        readFromRemote(targetPath);
                        continue;
                    }
                    log.debug("Opening file for writing");
                    byte[] handle = null;
                    try {
                        handle = nfs.openFile(targetPath, new UnsignedInteger32(NativeFileSystemProvider.OPEN_CREATE | NativeFileSystemProvider.OPEN_WRITE | NativeFileSystemProvider.OPEN_TRUNCATE), attr);
                        log.debug("NFS file opened");
                        writeOk();
                        log.debug("Reading from client");
                        int count = 0;
                        int read;
                        long length = Long.parseLong(cmdParts[1]);
                        while (count < length) {
                            read = pipeOut.read(buffer, 0, (int) (((length - count) < buffer.length) ? (length - count) : buffer.length));
                            if (read == -1) {
                                throw new EOFException("ScpServer received an unexpected EOF during file transfer");
                            }
                            log.debug("Got block of " + read);
                            nfs.writeFile(handle, new UnsignedInteger64(String.valueOf(count)), buffer, 0, read);
                            count += read;
                        }
                        log.debug("File transfer complete");
                    } catch (InvalidHandleException ihe) {
                        writeError("Invalid handle.");
                        throw new IOException("Invalid handle.");
                    } catch (FileNotFoundException e) {
                        writeError("File not found");
                        throw new IOException("File not found");
                    } catch (PermissionDeniedException e) {
                        writeError("Permission denied");
                        throw new IOException("Permission denied");
                    } finally {
                        if (handle != null) {
                            try {
                                log.debug("Closing handle");
                                nfs.closeFile(handle);
                            } catch (Exception e) {
                            }
                        }
                    }
                    waitForResponse();
                    if (preserveAttributes) {
                        attr.setPermissionsFromMaskString(cmdParts[0]);
                        log.debug("Setting permissions on directory to " + attr.getPermissionsString());
                        try {
                            nfs.setFileAttributes(targetPath, attr);
                        } catch (Exception e) {
                            writeError("Failed to set file permissions.");
                            break;
                        }
                    }
                    writeOk();
                    break;
                default:
                    writeError("Unexpected cmd: " + cmd);
                    throw new IOException("SCP unexpected cmd: " + cmd);
            }
        }
    }
