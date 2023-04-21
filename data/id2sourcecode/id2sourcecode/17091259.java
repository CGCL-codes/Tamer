    public static java.awt.Image createImageFromPDF(PDFDocument docu, Component anyComponent, FloatPoint cropBoxSize, int page, double escal, int pos) {
        try {
            RandomAccessFile raf = new RandomAccessFile(docFile, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = ByteBuffer.allocate((int) channel.size());
            channel.read(buf);
            PDFFile pdffile = new PDFFile(buf);
            PDFPage pageSheet = pdffile.getPage(page + 1);
            Rectangle rect = new Rectangle(0, 0, (int) pageSheet.getBBox().getWidth(), (int) pageSheet.getBBox().getHeight());
            Image img = pageSheet.getImage(rect.width, rect.height, rect, null, true, true);
            int ancho = (int) (cropBoxSize.x * escal);
            int alto = (int) (cropBoxSize.y * escal);
            double arg1 = 0, arg2 = 0, arg3 = 0, arg4 = 0, arg5 = 0, arg6 = 0;
            switch(pos) {
                case ARRIBA:
                    arg1 = escal;
                    arg4 = escal;
                    break;
                case DERECHA:
                    arg2 = escal * cropBoxSize.y / cropBoxSize.x;
                    arg3 = -escal * cropBoxSize.x / cropBoxSize.y;
                    arg5 = ancho;
                    break;
                case ABAJO:
                    arg1 = -escal;
                    arg4 = -escal;
                    arg5 = ancho;
                    arg6 = alto;
                    break;
                case IZQUIERDA:
                    arg2 = -escal * cropBoxSize.y / cropBoxSize.x;
                    arg3 = escal * cropBoxSize.x / cropBoxSize.y;
                    arg6 = alto;
                    break;
            }
            java.awt.geom.AffineTransform transform = new java.awt.geom.AffineTransform(arg1, arg2, arg3, arg4, arg5, arg6);
            AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
            img = op.filter(createBufferedImage(img), null);
            buf.clear();
            channel.close();
            raf.close();
            return img;
        } catch (Exception e) {
            log.log(Level.SEVERE, "(Create Image from pdf):" + e.getMessage(), e);
            return null;
        }
    }
