    public final void testCreateScreenCapture() {
        Runnable capture = new Runnable() {

            public void run() {
                robot.createScreenCapture(new Rectangle(10, 10, 100, 200));
            }
        };
        assertTrue(isDenied(new MySecurityManager(), capture));
        assertFalse(isDenied(null, capture));
        BufferedImage img = null;
        Rectangle rect = new Rectangle();
        try {
            img = robot.createScreenCapture(rect);
        } catch (IllegalArgumentException iae) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
        assertNull(img);
        exceptionCaught = false;
        img = null;
        rect.width = 100;
        try {
            img = robot.createScreenCapture(rect);
        } catch (IllegalArgumentException iae) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
        assertNull(img);
        exceptionCaught = false;
        img = null;
        waitForButton();
        rect.setBounds(f.getBounds());
        try {
            img = robot.createScreenCapture(rect);
        } catch (IllegalArgumentException iae) {
            exceptionCaught = true;
        }
        assertFalse(exceptionCaught);
        assertNotNull(img);
        int rgb = img.getRGB(30, 50);
        assertEquals(rect.width, img.getWidth(null));
        assertEquals(rect.height, img.getHeight(null));
        assertEquals("RGB", b.getBackground().getRGB(), rgb);
    }
