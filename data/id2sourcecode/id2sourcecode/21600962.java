    public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
        String name = fileName;
        if (!pkg.isUnnamed()) name = toDirName(pkg) + name;
        zip.putNextEntry(new ZipEntry(name));
        return filter;
    }
