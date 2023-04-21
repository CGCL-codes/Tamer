    @Override
    public void processFile(File inFile, String inEncoding, File outFile, String outEncoding) throws IOException, TranslationException {
        defineDOCUMENTSOptions(processOptions);
        ZipFile zipfile = new ZipFile(inFile);
        ZipOutputStream zipout = null;
        if (outFile != null) zipout = new ZipOutputStream(new FileOutputStream(outFile));
        Enumeration<? extends ZipEntry> unsortedZipcontents = zipfile.entries();
        List<? extends ZipEntry> filelist = Collections.list(unsortedZipcontents);
        Collections.sort(filelist, new Comparator<ZipEntry>() {

            public int compare(ZipEntry z1, ZipEntry z2) {
                String s1 = z1.getName();
                String s2 = z2.getName();
                String[] words1 = s1.split("\\d+\\.");
                String[] words2 = s2.split("\\d+\\.");
                if ((words1.length > 1 && words2.length > 1) && (words1[0].equals(words2[0]))) {
                    int number1 = 0;
                    int number2 = 0;
                    Matcher getDigits = DIGITS.matcher(s1);
                    if (getDigits.find()) number1 = Integer.parseInt(getDigits.group(1));
                    getDigits = DIGITS.matcher(s2);
                    if (getDigits.find()) number2 = Integer.parseInt(getDigits.group(1));
                    if (number1 > number2) return 1; else if (number1 < number2) return -1; else return 0;
                } else {
                    String shortname1 = removePath(words1[0]);
                    shortname1 = removeXML(shortname1);
                    String shortname2 = removePath(words2[0]);
                    shortname2 = removeXML(shortname2);
                    if (shortname1.indexOf("sharedStrings") >= 0 || shortname2.indexOf("sharedStrings") >= 0) {
                        if (shortname2.indexOf("sharedStrings") >= 0) return 1; else return -1;
                    }
                    int index1 = DOCUMENTS.indexOf(shortname1);
                    int index2 = DOCUMENTS.indexOf(shortname2);
                    if (index1 > index2) return 1; else if (index1 < index2) return -1; else return 0;
                }
            }
        });
        Enumeration<? extends ZipEntry> zipcontents = Collections.enumeration(filelist);
        while (zipcontents.hasMoreElements()) {
            ZipEntry zipentry = zipcontents.nextElement();
            String shortname = zipentry.getName();
            shortname = removePath(shortname);
            Matcher filematch = TRANSLATABLE.matcher(shortname);
            boolean first = true;
            if (filematch.matches()) {
                File tmpin = tmp();
                LFileCopy.copy(zipfile.getInputStream(zipentry), tmpin);
                File tmpout = null;
                if (zipout != null) tmpout = tmp();
                try {
                    createXMLFilter().processFile(tmpin, null, tmpout, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new TranslationException(e.getLocalizedMessage() + "\n" + OStrings.getString("OpenXML_ERROR_IN_FILE") + inFile, e);
                }
                if (zipout != null) {
                    ZipEntry outEntry = new ZipEntry(zipentry.getName());
                    zipout.putNextEntry(outEntry);
                    LFileCopy.copy(tmpout, zipout);
                    zipout.closeEntry();
                    first = false;
                }
                if (!tmpin.delete()) tmpin.deleteOnExit();
                if (tmpout != null) {
                    if (!tmpout.delete()) tmpout.deleteOnExit();
                }
            } else {
                if (zipout != null) {
                    ZipEntry outEntry = new ZipEntry(zipentry.getName());
                    zipout.putNextEntry(outEntry);
                    LFileCopy.copy(zipfile.getInputStream(zipentry), zipout);
                    zipout.closeEntry();
                }
            }
        }
        if (zipout != null) zipout.close();
    }
