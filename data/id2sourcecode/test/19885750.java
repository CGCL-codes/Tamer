    protected void writeFile(FileDTO fileDTO, Dataset dataset) throws Exception {
        File file = FileUtils.toFile(fileDTO.getDirectoryPath(), fileDTO.getFilePath());
        String tsUID = UIDs.ExplicitVRLittleEndian;
        boolean deleteFile = true;
        try {
            log.info("M-WRITE file: " + file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestOutputStream dos = new DigestOutputStream(new FileOutputStream(file), md);
            BufferedOutputStream out = new BufferedOutputStream(dos, new byte[bufferSize]);
            try {
                DcmEncodeParam encParam = DcmEncodeParam.valueOf(tsUID);
                dataset.writeFile(out, encParam);
            } finally {
                out.close();
            }
            fileDTO.setFileMd5(md.digest());
            fileDTO.setFileSize(file.length());
            fileDTO.setFileTsuid(tsUID);
            importFile(fileDTO, dataset);
            deleteFile = false;
        } finally {
            if (deleteFile) {
                log.info("M-DELETE file:" + file);
                if (!file.delete()) {
                    log.error("Failed to delete " + file);
                }
            }
        }
    }
