    public static File extract(String filename, File target) {
        if (filename == null || filename.trim().length() <= 0 || target == null) return null;
        try {
            InputStream inputStream = ResourceLoader.getResource(filename, false).openInputStream();
            if (inputStream == null) return null;
            BufferedInputStream input = new BufferedInputStream(inputStream);
            target.getParentFile().mkdirs();
            int bytesRead;
            byte[] buffer = new byte[1024];
            FileOutputStream output = new FileOutputStream(target);
            while ((bytesRead = input.read(buffer)) != -1) output.write(buffer, 0, bytesRead);
            output.close();
            input.close();
            return target;
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }
