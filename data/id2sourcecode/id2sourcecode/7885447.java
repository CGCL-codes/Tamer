    public static String ExtractZip(String zipName, String outFolder) {
        try {
            File sourceZipFile = new File(zipName);
            File outDirectory = new File(outFolder);
            ZipFile zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);
            Enumeration zipFileEntries = zipFile.entries();
            while (zipFileEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                String currentEntry = entry.getName();
                for (int X = currentEntry.length() - 1; X >= 0; X--) {
                    if (currentEntry.charAt(X) == '\\' || currentEntry.charAt(X) == '/') {
                        currentEntry = currentEntry.substring(X + 1);
                        break;
                    }
                }
                File destFile = new File(outDirectory, currentEntry);
                if (destFile.getParentFile() != null) destFile.getParentFile().mkdirs();
                if (!entry.isDirectory()) {
                    BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
                    int currentByte;
                    byte data[] = new byte[BUFFER];
                    FileOutputStream fos = new FileOutputStream(destFile);
                    BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                    while ((currentByte = is.read(data, 0, BUFFER)) != -1) dest.write(data, 0, currentByte);
                    dest.flush();
                    dest.close();
                    is.close();
                }
            }
            zipFile.close();
            return "success";
        } catch (IOException e) {
            return "Failed to extract zip: " + e.toString();
        }
    }
