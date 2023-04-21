    protected byte[] loadClassData(String name) {
        try {
            String pathName = name.replace('.', '/') + ".class";
            BufferedInputStream in = new BufferedInputStream(getResourceAsStream(pathName));
            if (in == null) return null;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int i;
            while ((i = in.read()) > -1) out.write(i);
            in.close();
            return out.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
