    public static String[] splitDataFile(String fs, int blockSize) {
        RandomAccessFile parentFile = null;
        long filenum = 0;
        File[] subFiles = null;
        try {
            File file = new File(fs);
            if (!file.exists() || (file.exists() && file.isDirectory())) throw new java.lang.RuntimeException("file not exist!");
            long index = (long) blockSize * 1024 * 1024;
            long fileSize = file.length();
            if (fileSize <= (index + 1024)) {
                return new String[] { file.getAbsolutePath() };
            }
            if (fileSize % index == 0) filenum = fileSize / index; else filenum = fileSize / index + 1;
            String sourceFile = file.getAbsolutePath();
            parentFile = new RandomAccessFile(sourceFile, "r");
            String fileName = sourceFile.substring(0, sourceFile.lastIndexOf("."));
            subFiles = new File[(int) filenum];
            for (int i = 0; i < filenum; i++) {
                String _tempFileName = new StringBuilder().append(fileName).append("sub").append(i).append(".log").toString();
                new File(_tempFileName).createNewFile();
                subFiles[i] = new File(_tempFileName);
            }
            int beg = 0;
            for (int i = 0; i < filenum; i++) {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(subFiles[i]);
                    FileChannel inChannel = parentFile.getChannel();
                    FileChannel outChannel = outputStream.getChannel();
                    long remain;
                    if (fileSize - beg > index) remain = index; else remain = fileSize - beg;
                    while (remain > 0) {
                        if (remain > 5 * 1024 * 1024) {
                            inChannel.transferTo(beg, 5 * 1024 * 1024, outChannel);
                            remain -= 5 * 1024 * 1024;
                            beg += 5 * 1024 * 1024;
                        } else {
                            inChannel.transferTo(beg, remain, outChannel);
                            beg += remain;
                            break;
                        }
                    }
                    if (i < filenum - 1) {
                        parentFile.seek(beg);
                        String tail = parentFile.readLine();
                        if (tail == null) {
                            for (int j = i + 1; j < filenum; j++) {
                                subFiles[j].delete();
                            }
                            break;
                        }
                        beg += tail.length() + 2;
                        outputStream.write(tail.getBytes());
                    }
                } finally {
                    try {
                        if (outputStream != null) outputStream.close();
                    } catch (Exception e) {
                        logger.error(e, e);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex, ex);
        } finally {
            if (parentFile != null) try {
                parentFile.close();
            } catch (IOException e) {
                logger.error(e, e);
            }
        }
        if (subFiles != null && subFiles.length > 0) {
            String[] result = new String[subFiles.length];
            int i = 0;
            for (File f : subFiles) {
                result[i] = f.getAbsolutePath();
                i += 1;
            }
            return result;
        } else throw new java.lang.RuntimeException("split file error!");
    }
