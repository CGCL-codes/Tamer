    public static void extract(String archivePath, String packege, String className, boolean inner, String to) throws IOException {
        ZipFile archive = new ZipFile(archivePath);
        List entries = findRelevant(archive, packege, className, inner);
        InputStream in = null;
        OutputStream out = null;
        byte[] buffer = new byte[2048];
        ZipEntry entry;
        String outFile;
        int lastSep, amountRead;
        for (int i = 0; i < entries.size(); i++) {
            entry = (ZipEntry) entries.get(i);
            outFile = entry.getName();
            if ((lastSep = outFile.lastIndexOf('/')) != -1) outFile = outFile.substring(lastSep);
            try {
                in = archive.getInputStream(entry);
                if (in == null) throw new IOException("Zip file entry <" + entry.getName() + "> not found");
                out = new FileOutputStream(to + File.separator + outFile);
                while ((amountRead = in.read(buffer)) != -1) out.write(buffer, 0, amountRead);
            } finally {
                if (in != null) in.close();
                if (out != null) out.close();
            }
        }
    }
