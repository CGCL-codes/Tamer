    public void setField(PdfStamper stamper, String name, String data, URL templateURL) throws DocumentException {
        if (data == null || data == "") return;
        AcroFields form = stamper.getAcroFields();
        float[] positions = form.getFieldPositions(name);
        OutputStream out = null;
        for (int k = 0; k < positions.length; k += 5) {
            Rectangle rectangle = new Rectangle(positions[k + 1], positions[k + 2], positions[k + 3], positions[k + 4]);
            Image img = null;
            DataMatrixBean bean = new DataMatrixBean();
            final int dpi = 150;
            bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
            bean.doQuietZone(false);
            try {
                URL imgURL = URLHelper.createByteCacheURL(name);
                URLConnection imConnection = imgURL.openConnection();
                imConnection.setDoOutput(true);
                out = imConnection.getOutputStream();
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/jpeg", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
                bean.generateBarcode(canvas, "123456");
                canvas.finish();
                out.close();
                img = Image.getInstance(imgURL);
                if (img != null) {
                    img.scaleToFit(rectangle.getWidth(), rectangle.getHeight());
                    img.setAbsolutePosition(positions[k + 1] + (rectangle.getWidth() - img.getScaledWidth()), positions[k + 2] + (rectangle.getHeight() - img.getScaledHeight()));
                    PdfContentByte cb = stamper.getOverContent((int) positions[k]);
                    cb.addImage(img);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtil.close(out);
            }
        }
    }
