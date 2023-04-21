        FileFetcher(File dir, Map<String, Object> fileDetails, String saveAs, boolean isConf, long latestVersion) throws IOException {
            this.copy2Dir = dir;
            this.fileName = (String) fileDetails.get(NAME);
            this.size = (Long) fileDetails.get(SIZE);
            this.isConf = isConf;
            this.saveAs = saveAs;
            if (fileDetails.get(LAST_MODIFIED) != null) {
                lastmodified = (Long) fileDetails.get(LAST_MODIFIED);
            }
            indexVersion = latestVersion;
            this.file = new File(copy2Dir, saveAs);
            this.fileOutputStream = new FileOutputStream(file);
            this.fileChannel = this.fileOutputStream.getChannel();
            if (includeChecksum) checksum = new Adler32();
        }
