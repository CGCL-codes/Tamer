    private void list(File base, String suffix, ArrayList<String> list) throws FileNotFoundException, IOException {
        File[] listing = base.listFiles();
        for (int i = 0; i < listing.length; i++) {
            if ((listing[i].getName().equals(".") == true) || (listing[i].getName().equals("..") == true)) {
                continue;
            }
            if (listing[i].isDirectory() == true) {
                list(listing[i], suffix, list);
                continue;
            }
            if (listing[i].getName().endsWith(suffix) == true) {
                String orig_abs = listing[i].getAbsolutePath();
                String config_abs = orig_abs.substring(0, (orig_abs.length() - suffix.length()));
                list.add(config_abs);
                if ((new File(config_abs)).exists() == true) {
                    FileUtils.copy(config_abs, config_abs + ".old");
                }
                FileUtils.copy(orig_abs, config_abs);
            }
        }
    }
