    private boolean extractFileEntry(Zip64File zf, FileEntry fe) throws IOException, FileNotFoundException {
        boolean bExtracted = false;
        String sFileName = m_fileZipRoot.getAbsolutePath();
        if (sFileName.endsWith(".")) sFileName = sFileName.substring(0, sFileName.length() - 1);
        if (sFileName.endsWith(File.separator)) sFileName = sFileName.substring(0, sFileName.length() - 1);
        sFileName = sFileName + File.separator + fe.getName().replace('/', File.separatorChar);
        File fileOutput = new File(sFileName);
        fileOutput.getParentFile().mkdirs();
        if (fe.isDirectory()) bExtracted = fileOutput.mkdir(); else {
            if (!fileOutput.exists() || m_bReplace) {
                if (fileOutput.exists()) displayMessage("extracting " + fileOutput.getAbsolutePath() + " from " + fe.getName()); else displayMessage("replacing " + fileOutput.getAbsolutePath() + " by " + fe.getName());
                FileOutputStream fos = new FileOutputStream(fileOutput);
                EntryInputStream eis = zf.openEntryInputStream(fe.getName());
                byte[] buffer = new byte[BUFFER_SIZE];
                for (int iRead = eis.read(buffer); iRead >= 0; iRead = eis.read(buffer)) fos.write(buffer, 0, iRead);
                eis.close();
                fos.close();
                bExtracted = true;
            } else System.err.println("File " + fileOutput.getAbsolutePath() + " already exists and is left unreplaced!");
        }
        return bExtracted;
    }
