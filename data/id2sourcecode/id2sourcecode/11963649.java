    public static byte[] packPdf() throws IOException {
        byte[] data = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zout = new ZipOutputStream(out);
        zout.setLevel(0);
        try {
            String filename = "my_pdf.pdf";
            ZipEntry ze = new ZipEntry(filename);
            zout.putNextEntry(ze);
            copy(MY_PDF, zout);
            zout.closeEntry();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("FileNotProcessed Problems processing ");
        }
        zout.close();
        data = out.toByteArray();
        out.close();
        return data;
    }
