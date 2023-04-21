    private void addXsdEntry(byte[] buf, ZipOutputStream out, FileInputStream in, String xsdFile) throws IOException {
        ZipEntry zipEntry;
        InputStream xsd = getClass().getResourceAsStream("/ims/" + xsdFile);
        zipEntry = new ZipEntry(xsdFile);
        out.putNextEntry(zipEntry);
        transferBytes(buf, out, xsd);
        out.closeEntry();
        in.close();
    }
