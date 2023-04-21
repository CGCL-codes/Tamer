        private void saveZipArchive(Object session, ZipInputStream archiveIn, ArchivePath archive, String savePath, String outputFile, Component comp) throws IOException {
            OutputStream out = null;
            VFS vfs = VFSManager.getVFSForPath(savePath);
            long length = new File(outputFile).length();
            try {
                out = vfs._createOutputStream(session, savePath, comp);
                out = new ZipOutputStream(out);
                ZipOutputStream archiveOut = (ZipOutputStream) out;
                boolean saved = false;
                for (; ; ) {
                    ZipEntry next = archiveIn.getNextEntry();
                    if (next == null) break;
                    Log.log(Log.DEBUG, this, "Copy entry " + next);
                    if (next.getName().equals(archive.entryName)) {
                        next = new ZipEntry(archive.entryName);
                        archiveOut.putNextEntry(next);
                        copy(outputFile, archiveOut);
                        saved = true;
                    } else {
                        archiveOut.putNextEntry(next);
                        copy(archiveIn, archiveOut);
                    }
                    archiveOut.closeEntry();
                }
                if (!saved) {
                    ZipEntry newEntry = new ZipEntry(archive.entryName);
                    newEntry.setSize(length);
                    archiveOut.putNextEntry(newEntry);
                    copy(outputFile, archiveOut);
                    saved = true;
                    archiveOut.closeEntry();
                }
            } finally {
                IOUtilities.closeQuietly(out);
            }
        }
