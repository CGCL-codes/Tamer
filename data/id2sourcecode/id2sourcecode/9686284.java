    private void copyWorkspaceFile(String file) {
        File f = new File(Platform.getLocation().toFile(), file);
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            try {
                byte[] buffer = new byte[10240];
                OutputStream os = new FileOutputStream(f);
                InputStream is = FileLocator.openStream(getBundle(), new Path("data/" + file), false);
                int readed = 0;
                do {
                    readed = is.read(buffer);
                    os.write(buffer, 0, readed);
                } while (readed == buffer.length);
                os.close();
                is.close();
            } catch (Exception e) {
                CorePlugin.logException(e);
            }
        }
    }
