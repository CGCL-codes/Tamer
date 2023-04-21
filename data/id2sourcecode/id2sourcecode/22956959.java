    public Image getImage(String filename, Component cmp) {
        Image img;
        InputStream is = this.getClass().getResourceAsStream(filename);
        if (is == null) System.out.println("Error reading image: " + filename);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            int c;
            while ((c = is.read()) >= 0) baos.write(c);
            img = cmp.getToolkit().createImage(baos.toByteArray());
        } catch (Exception ex) {
            System.out.println("Image " + filename + " could not be created.");
            return null;
        }
        return img;
    }
