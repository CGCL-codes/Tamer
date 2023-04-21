    public static void writeGeometryToFile(String filename, Geometry g) throws IOException {
        ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
        zip.putNextEntry(new ZipEntry("object"));
        ScribeOutputStream out = new ScribeOutputStream(zip);
        ScribeGeometryArray.writeGeometryArray(out, g);
        out.flush();
        out.close();
    }
