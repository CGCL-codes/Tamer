    public Image createImage(String imageName) throws IOException {
        InputStream inputStream = getResourceAsStream(imageName);
        BufferedInputStream in = new BufferedInputStream(inputStream);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int i;
        while ((i = in.read()) > -1) out.write(i);
        byte[] imageData = out.toByteArray();
        out.close();
        in.close();
        return Toolkit.getDefaultToolkit().createImage(imageData);
    }
