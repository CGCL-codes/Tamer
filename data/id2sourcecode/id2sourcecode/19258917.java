    private Image getImage(String filename) {
        InputStream is = getClass().getResourceAsStream(filename);
        if (is == null) System.out.println((new StringBuilder()).append("Error reading image: ").append(filename).toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Image img;
        try {
            int c;
            while ((c = is.read()) >= 0) baos.write(c);
            img = getToolkit().createImage(baos.toByteArray());
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return img;
    }
