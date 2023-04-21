    public Assignment getAssignment() {
        try {
            ByteArrayOutputStream Buffer = new ByteArrayOutputStream();
            ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(file));
            ZipEntry data = data = in.getNextEntry();
            if (data == null) return (null);
            int i = 0;
            byte buffer[] = new byte[512];
            while ((i = in.read(buffer)) > 0) Buffer.write(buffer, 0, i);
            ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(Buffer.toByteArray()));
            return ((Assignment) oin.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (null);
    }
