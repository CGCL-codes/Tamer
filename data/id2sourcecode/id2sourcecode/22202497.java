    private boolean ZipExist(ZipOutputStream out) {
        String filename_info = GetClassName() + ".template";
        try {
            out.putNextEntry(new ZipEntry(filename_info));
            return false;
        } catch (Exception e) {
            return true;
        }
    }
