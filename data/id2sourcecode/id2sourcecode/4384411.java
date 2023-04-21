    public static ArrayList<String> zipExtractedList(File fileName) {
        ArrayList<String> fileNames = new ArrayList<String>();
        try {
            ZipFile zf = new ZipFile(fileName);
            for (Enumeration entries = zf.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                {
                    if (!entry.isDirectory() && entry.getName().contains(".srt")) {
                        fileNames.add(entry.getName());
                    }
                    if (!entry.isDirectory() && entry.getName().contains(".zip")) {
                        copyInputStream(zf.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(entry.getName())));
                        fileNames.addAll(zipExtractedList(new File(entry.getName())));
                    }
                }
            }
            zf.close();
            fileName.delete();
        } catch (IOException ex) {
            Logger.getLogger(FindSub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileNames;
    }
