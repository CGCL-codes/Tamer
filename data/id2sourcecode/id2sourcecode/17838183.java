    private void createPoseidonFile(String zipFilename) throws MiddlegenException {
        try {
            File dir = getDestinationDir();
            String xmiFileName = getXmiFilename();
            String projFileName = getProjFilename();
            File zipFile = new File(dir, zipFilename);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
            ZipEntry zipEntry = new ZipEntry(projFileName);
            out.putNextEntry(zipEntry);
            File projFile = new File(dir, projFileName);
            FileInputStream in = new FileInputStream(projFile);
            writeInToOut(in, out);
            in.close();
            zipEntry = new ZipEntry(xmiFileName);
            out.putNextEntry(zipEntry);
            File xmiFile = new File(dir, xmiFileName);
            in = new FileInputStream(xmiFile);
            writeInToOut(in, out);
            in.close();
            out.close();
            projFile.delete();
        } catch (Exception ex) {
            throw new MiddlegenException(ex.getMessage());
        }
    }
