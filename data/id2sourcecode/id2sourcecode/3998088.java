    private static File copyFromJarToTempFile(ClassLoader cl, File home_dir, String resource) throws IOException {
        InputStream input = cl.getResource(resource).openStream();
        String[] resource_parts = resource.split("\\.");
        File temp = File.createTempFile(resource_parts[0], "." + resource_parts[1], home_dir);
        new JxLogEvent().send("Uncompressing " + temp);
        temp.deleteOnExit();
        FileOutputStream output = new FileOutputStream(temp);
        byte[] scratch = new byte[10 * 1024 * 1024];
        int size;
        while ((size = input.read(scratch)) > 0) output.write(scratch, 0, size);
        input.close();
        output.close();
        new JxLogEvent().send("Uncompressing " + temp + "  complete.");
        return temp;
    }
