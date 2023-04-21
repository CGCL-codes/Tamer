    public void recieveFiles(InputStream in, String tempDir) {
        File tempFolder = new File(tempDir);
        tempFolder.mkdir();
        try {
            ObjectInputStream objIn = new ObjectInputStream(in);
            BufferedInputStream bis = new BufferedInputStream(in);
            int fileRemCnt = objIn.readInt();
            int fileCnt = objIn.readInt();
            int total = fileRemCnt + fileCnt;
            int completion = 0;
            client.setProgress(0);
            ArrayList<File> removeFiles = getRemoveFileList(objIn, fileRemCnt, total, completion);
            completion = fileRemCnt;
            ArrayList<FileTransferHeader> fileHeaders = new ArrayList<FileTransferHeader>(fileCnt);
            for (int i = 0; i < fileCnt; ++i) {
                FileTransferHeader header = (FileTransferHeader) objIn.readObject();
                if (header.getFileName().indexOf("..") >= 0 || header.getFileName().indexOf(":") >= 0) {
                    throw new Exception("invalid characters found in file header");
                } else {
                    fileHeaders.add(header);
                }
            }
            for (FileTransferHeader fileHeader : fileHeaders) {
                client.transferEvent("adding " + fileHeader.getFileName() + " (" + fileHeader.getFileLength() / 1024 + "kB)");
                if (cancelled) {
                    throw new UserCancelledException();
                }
                long totalRemaining = fileHeader.getFileLength();
                if (totalRemaining == -1) {
                    new File(tempDir + PATH_SEP + fileHeader.getFileName()).mkdirs();
                } else if (totalRemaining == 0L) {
                    mkdirs(tempDir + PATH_SEP + fileHeader.getFileName());
                    new File(tempDir + PATH_SEP + fileHeader.getFileName()).createNewFile();
                } else {
                    mkdirs(tempDir + PATH_SEP + fileHeader.getFileName());
                    FileOutputStream toFile = new FileOutputStream(new File(tempDir + PATH_SEP + fileHeader.getFileName()));
                    while (totalRemaining > 0) {
                        int readLength = totalRemaining > buffer.length ? buffer.length : (int) totalRemaining;
                        totalRemaining -= readLength;
                        bis.read(buffer, 0, readLength);
                        toFile.write(buffer, 0, readLength);
                        if (cancelled) {
                            throw new UserCancelledException();
                        }
                        client.transferEvent("adding " + fileHeader.getFileName() + " (" + (fileHeader.getFileLength() - totalRemaining) / 1024 + "/" + fileHeader.getFileLength() / 1024 + "kB)");
                    }
                    toFile.close();
                }
                client.setProgress((float) ++completion / total);
            }
            if (total > 0) {
                client.completed("complete", "Update completed");
            } else {
                client.completed("noupdate", "Nothing to update");
            }
            copyToFinalDestination(tempDir, removeFiles, fileHeaders);
            objIn.close();
            bis.close();
        } catch (UserCancelledException e) {
            setErrorStatus(e.getMessage());
            cancelled = false;
        } catch (Exception e) {
            setErrorStatus(e.getMessage());
            e.printStackTrace();
        } finally {
            rmdir(tempFolder);
        }
    }
