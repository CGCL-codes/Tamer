    private void copyImage(String resource, ReportIOProvider provider, String name, String extension) throws IOException {
        URL url = Tools.getResource(resource);
        if (url == null) {
            throw new IOException("could not find resource");
        }
        RenderedImage image = ImageIO.read(url.openStream());
        final OutputStream out = provider.createOutputStream(name + "." + extension, "image/" + extension);
        ImageIO.write(image, extension, out);
        out.close();
    }
