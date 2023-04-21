    public static void createSamples() {
        if (UIPlugin.getDefault().getBundle().getState() != Bundle.ACTIVE) {
            return;
        }
        String destination = UIPlugin.getDefault().getPluginPreferences().getString(UIConstants.xprocess_importdir);
        File destinationSampleDir = new File(destination);
        if (!destinationSampleDir.exists()) {
            destinationSampleDir.mkdirs();
        }
        String source = UIPlugin.getDefault().getPluginLocation() + "lib" + File.separator + "samples";
        File sourceSampleDir = new File(source);
        if (sourceSampleDir.exists()) {
            for (File file : sourceSampleDir.listFiles()) {
                if (file.getName().endsWith(".xpe")) {
                    File out = new File(destination + File.separator + file.getName());
                    if (!out.exists()) {
                        try {
                            FileUtils.copyFile(file, out);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
