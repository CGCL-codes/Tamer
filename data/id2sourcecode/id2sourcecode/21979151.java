    private void createPNGGraph(HttpServletRequest request, OutputStream out) throws IOException {
        int width;
        int height;
        try {
            int w = Integer.parseInt(request.getParameter("width"));
            width = w <= 800 ? w : 800;
        } catch (NumberFormatException e) {
            width = 400;
        }
        try {
            int h = Integer.parseInt(request.getParameter("height"));
            height = h <= 600 ? h : 600;
        } catch (NumberFormatException e) {
            height = 300;
        }
        String error = "An error has occured.";
        try {
            ConvertUtils.register(converter, Color.class);
            byte[] output;
            String psid = request.getParameter("psid");
            if (psid != null) {
                MeterReaderBean bean = (MeterReaderBean) request.getSession().getAttribute(psid);
                output = preExisting(bean, request);
            } else {
                output = adhoc(request, width, height);
            }
            ByteArrayInputStream bis = new ByteArrayInputStream(output);
            while (bis.available() > 0) {
                out.write(bis.read());
            }
        } catch (Exception e) {
            error += " " + e.getLocalizedMessage();
        } finally {
            ConvertUtils.deregister(Color.class);
        }
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
        g2d.setBackground(Color.white);
        g2d.fillRect(0, 0, width, height);
        int errorWidth = g2d.getFontMetrics().stringWidth(error);
        g2d.setColor(Color.black);
        g2d.drawString(error, width / 2 - errorWidth / 2, height / 2);
        g2d.dispose();
        ImageIO.write(bufferedImage, "png", out);
    }
