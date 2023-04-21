    void openZip(String path) {
        ZipInputStream in = null;
        ByteArrayOutputStream out;
        int nRois = 0;
        try {
            in = new ZipInputStream(new FileInputStream(path));
            byte[] buf = new byte[1024];
            int len;
            ZipEntry entry = in.getNextEntry();
            while (entry != null) {
                String name = entry.getName();
                if (name.endsWith(".roi")) {
                    out = new ByteArrayOutputStream();
                    while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
                    out.close();
                    byte[] bytes = out.toByteArray();
                    RoiDecoder rd = new RoiDecoder(bytes, name);
                    Roi roi = rd.getRoi();
                    if (roi != null) {
                        name = name.substring(0, name.length() - 4);
                        name = getUniqueName(name);
                        list.add(name);
                        rois.put(name, roi);
                        nRois++;
                    }
                }
                entry = in.getNextEntry();
            }
            in.close();
        } catch (IOException e) {
            error(e.toString());
        }
        if (nRois == 0) error("This ZIP archive does not appear to contain \".roi\" files");
        updateShowAll();
    }
