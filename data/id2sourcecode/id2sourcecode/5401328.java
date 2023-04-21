        public void write(OutputStream out) throws IOException {
            String dir = info.getDirectory();
            if (dir == null && info.getFiles().length == 1) {
                String path = info.getBaseDirectory() + "/" + info.getFiles()[0].getPath();
                writeFile(path, out);
            } else {
                ZipOutputStream zipOut = new ZipOutputStream(out);
                zipOut.setLevel(0);
                zipOut.setComment("name: " + info.getName() + info.getComment() != null ? "\n comment:" + info.getComment() : "" + info.getTag() != null ? "\n tag:" + info.getTag() : "");
                FileInfo[] files = info.getFiles();
                char separator = rtorrent.getFileSeparatoChar();
                String remoteBaseDir = info.getBaseDirectory();
                String remoteBasePath = remoteBaseDir + separator + dir;
                for (FileInfo fileInfo : files) {
                    String remotePath = '\'' + remoteBasePath + separator + fileInfo.getPath() + '\'';
                    ZipEntry entry = new ZipEntry(dir + "\\" + fileInfo.getPath().replace(separator, '\\'));
                    zipOut.putNextEntry(entry);
                    writeFile(remotePath, zipOut);
                }
                zipOut.finish();
            }
        }
