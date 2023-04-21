    public void processFile(File inFile, String inEncoding, File outFile, String outEncoding) throws IOException, TranslationException {
        ZipFile zipfile = new ZipFile(inFile);
        ZipOutputStream zipout = null;
        if (outFile != null) zipout = new ZipOutputStream(new FileOutputStream(outFile));
        Enumeration<? extends ZipEntry> zipcontents = zipfile.entries();
        while (zipcontents.hasMoreElements()) {
            ZipEntry zipentry = zipcontents.nextElement();
            String shortname = zipentry.getName();
            if (shortname.lastIndexOf('/') >= 0) shortname = shortname.substring(shortname.lastIndexOf('/') + 1);
            if (TRANSLATABLE.contains(shortname)) {
                File tmpin = tmp();
                LFileCopy.copy(zipfile.getInputStream(zipentry), tmpin);
                File tmpout = null;
                if (zipout != null) tmpout = tmp();
                try {
                    createXMLFilter(processOptions).processFile(tmpin, null, tmpout, null);
                } catch (Exception e) {
                    throw new TranslationException(e.getLocalizedMessage() + "\n" + OStrings.getString("OpenDoc_ERROR_IN_FILE") + inFile);
                }
                if (zipout != null) {
                    ZipEntry outentry = new ZipEntry(zipentry.getName());
                    outentry.setMethod(ZipEntry.DEFLATED);
                    zipout.putNextEntry(outentry);
                    LFileCopy.copy(tmpout, zipout);
                    zipout.closeEntry();
                }
                if (!tmpin.delete()) tmpin.deleteOnExit();
                if (tmpout != null) {
                    if (!tmpout.delete()) tmpout.deleteOnExit();
                }
            } else {
                if (zipout != null) {
                    ZipEntry outentry = new ZipEntry(zipentry.getName());
                    zipout.putNextEntry(outentry);
                    LFileCopy.copy(zipfile.getInputStream(zipentry), zipout);
                    zipout.closeEntry();
                }
            }
        }
        if (zipout != null) zipout.close();
    }
