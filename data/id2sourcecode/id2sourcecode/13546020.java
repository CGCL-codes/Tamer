    private void restoreDatabase(String iDbDirectory, File iArchFile) throws DbRestoreException {
        try {
            ZipFile archive = new ZipFile(iArchFile);
            File outFile;
            FileOutputStream outStream;
            byte[] buffer;
            ZipEntry entry;
            InputStream inStream;
            outFile = new File(iDbDirectory);
            outFile.mkdir();
            String force = (String) parameters.get("force");
            Enumeration entries = archive.entries();
            for (int fileIndex = 0; entries.hasMoreElements(); ++fileIndex) {
                entry = (ZipEntry) entries.nextElement();
                inStream = archive.getInputStream(entry);
                outFile = new File(iDbDirectory + "/" + entry.getName());
                if (monitor != null) monitor.notifyStatus("Restoring segment: " + entry.getName(), fileIndex * 100 / archive.size());
                if (force == null && outFile.exists()) {
                    System.out.println();
                    char response = getUserAdvisor().askToUser(System.out, "    Database already exist, overwrite ?", "#No,#Yes");
                    if (response != 'y' && response != 'Y') {
                        System.out.println();
                        throw new DbRestoreException("Restore aborted by user.");
                    }
                    System.out.print("    Overwriting...");
                    force = "true";
                }
                outStream = new FileOutputStream(outFile);
                int n;
                int nread = 0;
                int len = (int) entry.getSize();
                buffer = new byte[(int) len];
                while (nread < len) {
                    if ((n = inStream.read(buffer, nread, len - nread)) == -1) {
                        throw new DbRestoreException("ERROR! Cannot restore the database: error on reading archive file");
                    }
                    nread += n;
                }
                inStream.close();
                outStream.write(buffer);
                outStream.flush();
                outStream.close();
            }
        } catch (Exception e) {
            throw new DbRestoreException("ERROR! Cannot restore the database (" + e + ")");
        }
    }
