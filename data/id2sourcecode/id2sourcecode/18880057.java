    public static File extract(ClassLoader classLoader, String filename, File target) {
        if (filename == null || filename.trim().length() <= 0 || target == null) return null;
        try {
            URL url = classLoader.getResource(filename);
            if (url == null) return null;
            target.getParentFile().mkdirs();
            int bytesRead;
            byte[] buffer = new byte[1024];
            FileOutputStream output = new FileOutputStream(target);
            BufferedInputStream input = new BufferedInputStream(url.openStream());
            while ((bytesRead = input.read(buffer)) != -1) output.write(buffer, 0, bytesRead);
            output.close();
            input.close();
            return target;
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }
