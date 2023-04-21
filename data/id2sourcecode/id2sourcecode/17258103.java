    public int doTask() {
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());
            BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream("new.zip"));
            int i;
            details.append("Downloading file from: " + urlString + " ");
            while ((i = bin.read()) != -1) {
                details.append(".");
                bout.write(i);
            }
            bin.close();
            bout.close();
            details.append("DONE\n");
            ZipFile zipFile = new ZipFile("new.zip");
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    details.append("Extracting directory: " + entry.getName() + "\n");
                    (new File(entry.getName())).mkdir();
                    continue;
                }
                details.append("Extracting file: " + entry.getName() + "\n");
                copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(entry.getName())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        details.append("FINISHED!!");
        return 0;
    }
