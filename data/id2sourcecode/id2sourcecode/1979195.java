    public static void unzip(String zipName, String extractionPath) throws IOException {
        Enumeration zipEntries;
        getLogger().debug("Opening: " + zipName);
        ZipFile zipFile = new ZipFile(zipName);
        zipEntries = zipFile.entries();
        while (zipEntries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) zipEntries.nextElement();
            if (entry.isDirectory()) {
                getLogger().debug("Extracting directory: " + extractionPath + NIX_FILEPATH_SEP + entry.getName());
                File dir = new File(extractionPath + NIX_FILEPATH_SEP + entry.getName());
                dir.mkdir();
                continue;
            }
            getLogger().debug("Extracting file: " + extractionPath + NIX_FILEPATH_SEP + entry.getName());
            byte[] buffer = new byte[1024];
            int len;
            InputStream in = zipFile.getInputStream(entry);
            File x = new File(extractionPath + NIX_FILEPATH_SEP + entry.getName());
            getLogger().debug("Output location: " + extractionPath + NIX_FILEPATH_SEP + entry.getName());
            x.createNewFile();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(x));
            while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
            in.close();
            out.close();
        }
        zipFile.close();
    }
