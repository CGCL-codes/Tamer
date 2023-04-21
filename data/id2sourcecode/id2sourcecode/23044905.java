    public void saveZippedXML(File file) {
        try {
            ZipOutputStream zipo = new ZipOutputStream(new FileOutputStream(file));
            zipo.putNextEntry(new ZipEntry("phymote.xml"));
            new XMLOutputter().output(xmlDoc, zipo);
            zipo.close();
        } catch (IOException e) {
            System.err.println("Error writing file.");
        } catch (OutOfMemoryError e) {
            System.err.println("Error writing file : Out of Memory.");
            file.delete();
            xmlDoc.removeContent();
        }
    }
