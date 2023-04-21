    private void sendGetRequest() {
        try {
            final URI url = GoogleMapsUtils.buildGoogleMapsStaticUrl(BigDecimal.TEN, BigDecimal.TEN, 10);
            final URLConnection conn = url.toURL().openConnection();
            final BufferedImage img = ImageIO.read(conn.getInputStream());
            Assert.assertNotNull(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
