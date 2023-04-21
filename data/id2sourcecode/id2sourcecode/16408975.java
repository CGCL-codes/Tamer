    private long uploadIfHashesDontMatch(IProgressMonitor monitor, BrowsableItem fileSource, HashSet hashSet, WritableFileSystem target, String relativePath) throws IOException, NoSuchAlgorithmException {
        DigestInputStream sourceInput = new DigestInputStream(((BrowsableFile) fileSource).getInputStream(), MessageDigest.getInstance("MD5"));
        byte[] currentHash = null;
        byte[] currentData = null;
        long totalTransferred = 0;
        int read = 0;
        int readCurrent = 0;
        int currentSection = 0;
        while (readCurrent != -1 && !monitor.isCanceled()) {
            currentData = new byte[SECTION_SIZE];
            read = 0;
            readCurrent = 0;
            while (read != SECTION_SIZE && readCurrent != -1) {
                readCurrent = sourceInput.read(currentData, read, SECTION_SIZE - read);
                if (readCurrent != -1) {
                    read += readCurrent;
                }
            }
            currentHash = sourceInput.getMessageDigest().digest();
            if (!hashSet.sectionEquals(currentSection, currentHash)) {
                target.putFileContents(new ByteArrayInputStream(currentData, 0, read), read, relativePath + getName() + '.' + currentSection, null, 0);
                totalTransferred += read;
                hashSet.setHash(currentSection, currentHash);
            }
            if (!monitor.isCanceled() && currentSection < getNumberOfSections()) {
                monitor.worked(1);
            }
            currentSection++;
        }
        if (!monitor.isCanceled()) {
            BrowsableItem file = target.getFile(relativePath + getName() + '.' + currentSection);
            while (file != null) {
                target.deleteFile(file);
                currentSection++;
                file = target.getFile(relativePath + getName() + '.' + currentSection);
            }
        }
        sourceInput.close();
        return totalTransferred;
    }
