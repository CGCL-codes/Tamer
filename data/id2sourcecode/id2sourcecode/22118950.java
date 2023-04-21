    int getFileChannels(FileToRead[] astrPaths) throws Exception {
        int iNumPaths = 0;
        if (astrPaths == null) {
            return 0;
        }
        if (this.mAllowDuplicates == false) {
            this.maFiles = NIOFileReader.dedupFileList(this.maFiles);
        }
        for (FileToRead element : astrPaths) {
            FileInputStream fi;
            try {
                File f = new File(element.filePath);
                this.bytesRead += f.length();
                fi = new FileInputStream(f);
                this.openChannels++;
                ManagedFastInputChannel rf = new ManagedFastInputChannel();
                rf.mfChannel = fi.getChannel();
                rf.mPath = element.filePath;
                rf.file = f;
                this.mvReadyFiles.add(rf);
                this.maFiles.add(element);
                iNumPaths++;
            } catch (Exception e) {
                while (this.mvReadyFiles.size() > 0) {
                    ManagedFastInputChannel fs = this.mvReadyFiles.remove(0);
                    this.close(fs, NIOFileReader.OK_RECORD);
                }
                throw new Exception("Failed to open file: " + e.toString());
            }
        }
        return iNumPaths;
    }
