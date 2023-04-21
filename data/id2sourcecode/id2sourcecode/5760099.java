    private static void aggFileInString(ZipOutputStream out, String strContenutoFile, String nomefile) throws Exception {
        String separatore = System.getProperty("file.separator");
        try {
            ZipEntry entry = new ZipEntry(nomefile);
            out.putNextEntry(entry);
            int count;
        } catch (Exception e) {
            new Exception("\n aggFileInString: " + e.getMessage() + "\n");
        }
    }
