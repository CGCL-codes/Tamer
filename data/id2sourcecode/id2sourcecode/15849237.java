    public void testOpenEntryInputStream() {
        File fileExtract = new File(sEXTRACT_DIRECTORY);
        if (!fileExtract.exists()) fileExtract.mkdirs();
        File fileLarge = new File(fileExtract.getAbsolutePath() + "\\large.txt");
        if (fileLarge.exists()) fileLarge.delete();
        File fileMedium = new File(fileExtract.getAbsolutePath() + "\\medium.txt");
        if (fileMedium.exists()) fileMedium.delete();
        File fileSmall = new File(fileExtract.getAbsolutePath() + "\\small.txt");
        if (fileSmall.exists()) fileSmall.delete();
        File fileTemp = new File(sTEMP_DIRECTORY);
        if (!fileTemp.exists()) fileTemp.mkdirs();
        File fileLargeOriginal = new File(fileTemp.getAbsolutePath() + "\\large.txt");
        File fileMediumOriginal = new File(fileTemp.getAbsolutePath() + "\\medium.txt");
        File fileSmallOriginal = new File(fileTemp.getAbsolutePath() + "\\many\\small13854.txt");
        byte[] buffer = new byte[iBUFFER_SIZE];
        try {
            Zip64File zf = new Zip64File(m_sPkZipFile, true);
            EntryInputStream eis = zf.openEntryInputStream("many/small13854.txt");
            if (eis == null) fail("Input stream for small file could not be opened!"); else {
                FileOutputStream fos = new FileOutputStream(fileSmall);
                for (int iRead = eis.read(buffer); iRead >= 0; iRead = eis.read(buffer)) fos.write(buffer, 0, iRead);
                fos.close();
                eis.close();
                if (!equalTest(fileSmall, fileSmallOriginal)) fail("extracted small file is not equal to its original!");
                fileSmall.delete();
                eis = zf.openEntryInputStream("medium.txt");
                if (eis == null) fail("Input stream for medium file could not be opened!"); else {
                    fos = new FileOutputStream(fileMedium);
                    for (int iRead = eis.read(buffer); iRead >= 0; iRead = eis.read(buffer)) fos.write(buffer, 0, iRead);
                    fos.close();
                    eis.close();
                    if (!equalTest(fileMedium, fileMediumOriginal)) fail("extracted medium file is not equal to its original!"); else {
                        fileMedium.delete();
                        eis = zf.openEntryInputStream("large.txt");
                        if (eis == null) fail("Input stream for large file could not be opened!"); else {
                            fos = new FileOutputStream(fileLarge);
                            for (int iRead = eis.read(buffer); iRead >= 0; iRead = eis.read(buffer)) fos.write(buffer, 0, iRead);
                            fos.close();
                            eis.close();
                            if (!equalTest(fileLarge, fileLargeOriginal)) fail("extracted large file is not equal to its original!"); else {
                                fileLarge.delete();
                                zf.close();
                                fileExtract.delete();
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException fnfe) {
            fail(fnfe.getClass().getName() + ": " + fnfe.getMessage());
        } catch (IOException ie) {
            fail(ie.getClass().getName() + ": " + ie.getMessage());
        }
    }
