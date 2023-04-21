    private void downloadTaxonomy(File inputDir) {
        File file = new File(inputDir.getAbsolutePath() + File.separatorChar + "names.dmp");
        if (!file.exists()) {
            GeneralOutputEvent goe = new GeneralOutputEvent("Trying to download NCBI Taxonomy from FTP to " + inputDir.getAbsolutePath(), "[Validator - downloadTaxonomy]");
            goe.setLog4jLevel(Level.INFO);
            fireEventOccurred(goe);
            try {
                URL url = new URL("ftp://ftp.ncbi.nih.gov/pub/taxonomy/taxdmp.zip");
                ZipInputStream zis = new ZipInputStream(url.openStream());
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    System.out.println("Unzipping: " + entry.getName());
                    int size;
                    byte[] buffer = new byte[2048];
                    FileOutputStream fos = new FileOutputStream(inputDir.getAbsolutePath() + File.separatorChar + entry.getName());
                    BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
                    while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, size);
                    }
                    bos.flush();
                    bos.close();
                }
                zis.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
