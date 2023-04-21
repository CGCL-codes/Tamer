    public static void extract(String archivePath, String to) throws IOException {
        logger.info("extract() - start");
        ZipFile archive = new ZipFile(archivePath);
        logger.info("extract() - archivePath archivePath=" + archivePath);
        List entries = findRelevant(archive);
        InputStream in = null;
        OutputStream out = null;
        byte[] buffer = new byte[2048];
        ZipEntry entry;
        String outFile;
        int lastSep, amountRead;
        for (int i = 0; i < entries.size(); i++) {
            entry = (ZipEntry) entries.get(i);
            outFile = entry.getName();
            logger.info("extract() - ZipEntry outFile=" + outFile);
            if ((lastSep = outFile.lastIndexOf('/')) != -1) outFile = outFile.substring(lastSep);
            try {
                in = archive.getInputStream(entry);
                if (in == null) throw new IOException("Zip file entry <" + entry.getName() + "> not found");
                logger.info("extract() - ZipEntry entry=" + entry);
                logger.info("extract() - ZipEntry outFile=" + outFile);
                String outputfile = to + File.separator + outFile;
                logger.info("extract() - String outputfile=" + outputfile);
                out = new FileOutputStream(outputfile);
                while ((amountRead = in.read(buffer)) != -1) out.write(buffer, 0, amountRead);
            } finally {
                if (in != null) in.close();
                if (out != null) out.close();
            }
        }
        logger.info("extract() - end");
    }
