    protected void read() {
        FileOutputStream out = null;
        InputStream in = null;
        try {
            String name = ZipPlugin.getRootFolder() + File.separator + getPath().lastSegment();
            out = new FileOutputStream(name);
            ArchiveFile f = ((Archive) getParent()).getArchiveFile();
            Object archiveFile = f.file;
            if (archiveFile instanceof ZipFile) {
                ZipFile zipFile = (ZipFile) archiveFile;
                in = zipFile.getInputStream((ZipEntry) entry);
            } else if (archiveFile instanceof TarFile) {
                TarFile tarFile = (TarFile) archiveFile;
                in = tarFile.getInputStream((TarEntry) entry);
            }
            byte[] buffer = new byte[4096];
            int read = 0;
            while ((read = in.read(buffer)) != -1) out.write(buffer, 0, read);
            file = ZipPlugin.createArchive(name);
            super.read();
        } catch (Exception e) {
            ZipPlugin.logError(e);
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
            }
        }
    }
