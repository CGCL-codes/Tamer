    private boolean createFile(Zip64File zf, String sFileEntry) throws FileNotFoundException, IOException {
        boolean bCreated = false;
        if (!sFileEntry.endsWith("/")) {
            FileEntry feFile = zf.getFileEntry(sFileEntry);
            if (feFile == null) {
                String sFileName = m_fileZipRoot.getAbsolutePath().substring(0, m_fileZipRoot.getAbsolutePath().length() - 1) + sFileEntry.replace('/', File.separatorChar);
                File fileInput = new File(sFileName);
                if (fileInput.isFile()) {
                    EntryOutputStream eos = zf.openEntryOutputStream(sFileEntry, m_bCompress ? FileEntry.iMETHOD_DEFLATED : FileEntry.iMETHOD_STORED, new Date(2000 * (long) Math.ceil(fileInput.lastModified() / 2000.0)));
                    FileInputStream fis = new FileInputStream(fileInput);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    for (int iRead = fis.read(buffer); iRead >= 0; iRead = fis.read(buffer)) eos.write(buffer, 0, iRead);
                    fis.close();
                    eos.close();
                    bCreated = true;
                } else throw new FileNotFoundException("File " + fileInput.getAbsolutePath() + " cannot be injected, because it does not exist or is not a file!");
            } else System.err.println("File entry " + sFileEntry + " already exists and is left unreplaced!");
        } else throw new IOException("File entry " + sFileEntry + " cannot be created (must not be terminated with a slash)!");
        return bCreated;
    }
