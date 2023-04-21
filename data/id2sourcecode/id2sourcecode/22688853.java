    public void saveAttributes(Attributes a) throws IOException {
        String extension = fileresource.getUpperCaseExtension();
        saveExtentsXML(a);
        saveAttributeXML(a);
        if ("XML".equals(extension)) {
        } else {
            File file = new File(getBinaryFilename());
            FileOutputStream fos = new FileOutputStream(file);
            ZipOutputStream zout = new ZipOutputStream(fos);
            zout.putNextEntry(new ZipEntry("Attributes"));
            DataOutputStream out = new DataOutputStream(zout);
            saveAttributesBinary(out, a);
            out.close();
            fos.close();
        }
        String extent_tree_filename_stem = "extent_tree_";
        for (int i = 0; i < a.getExtents().size(); i++) {
            try {
                saveXMLDocument(extent_tree_filename_stem + i + ".xml", a.getExtents().at(i).getExtentXML());
            } catch (Exception e) {
            }
        }
        if (a.getIndicators() != null) {
            saveIndicatorsXML(a.getIndicators());
        }
        if (a.getNavigatorXML() != null) {
            saveXMLDocument("navigator.xml", a.getNavigatorXML());
        }
    }
