    public void saveAttributes(Attributes a) throws IOException {
        String extension = fileresource.getUpperCaseExtension();
        if ("XML".equals(extension)) {
        } else {
            File file = new File(getBinaryFilename());
            if (!file.exists()) {
                File parent = new File(file.getParent());
                if (!parent.exists()) {
                    parent.mkdir();
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            ZipOutputStream zout = new ZipOutputStream(fos);
            zout.putNextEntry(new ZipEntry("Attributes"));
            DataOutputStream out = new DataOutputStream(zout);
            saveAttributesBinary(out, a);
            out.close();
            fos.close();
        }
    }
