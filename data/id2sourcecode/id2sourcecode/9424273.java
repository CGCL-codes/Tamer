    public ByteArrayOutputStream readResource(String name) {
        InputStream in = getClass().getResourceAsStream(name);
        ByteArrayOutputStream baos = null;
        if (in != null) {
            baos = new ByteArrayOutputStream();
            try {
                int c;
                while ((c = in.read()) >= 0) baos.write(c);
            } catch (IOException e) {
                System.err.println("ERROR reading resource " + name + " : " + e);
            }
        }
        return baos;
    }
