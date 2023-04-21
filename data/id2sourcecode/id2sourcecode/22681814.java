    protected byte[] readFile(String name, URL pathUsed[]) {
        Enumeration classpath = _urlClassPath.elements();
        byte[] data = null;
        URL base_path;
        while ((data == null) && (classpath.hasMoreElements())) {
            base_path = (URL) classpath.nextElement();
            try {
                URL path = new URL(base_path, name);
                ByteArrayOutputStream out_buffer = new ByteArrayOutputStream();
                InputStream in = new BufferedInputStream(path.openStream());
                int octet;
                while ((octet = in.read()) != -1) out_buffer.write(octet);
                data = out_buffer.toByteArray();
                if (pathUsed != null) pathUsed[0] = base_path;
            } catch (IOException e) {
            }
        }
        return data;
    }
