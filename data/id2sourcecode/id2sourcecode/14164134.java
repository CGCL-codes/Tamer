    public byte[] getData(String resourceName) {
        ResourcePlace rp = get(resourceName, ResourceType.DATA);
        if (rp == null) {
            rp = get(resourceName, ResourceType.OTHER);
        }
        if (rp == null) {
            throw new AssertionError("Missing resource: " + language + " " + resourceName);
        }
        InputStream in = rp.open();
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buffer = new byte[Math.max(8192, in.available())];
            int read = 0;
            do {
                read = in.read(buffer);
                if (read > 0) {
                    bout.write(buffer, 0, read);
                }
            } while (read >= 0);
            return bout.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new AssertionError("Resource error" + language + " " + resourceName);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
