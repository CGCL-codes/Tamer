    public void UpLoadImage() throws Exception {
        if (formFile == null) {
        } else if (imagemaxsize != 0 && imagemaxsize * 1024 < formFile.getFileSize()) {
        } else if (imageminsize != 0 && imageminsize * 1024 > formFile.getFileSize()) {
        } else if (filePath == null) {
        } else if (iswatermark != 0) {
            if (watermarkString != null && !watermarkString.equals("")) {
                BufferedImage bi = ImageIO.read(formFile.getInputStream());
                Graphics2D g = bi.createGraphics();
                g.setColor(Color.BLACK);
                g.setFont(new Font("宋体", Font.PLAIN, watermarkStringSize));
                int Xcoord = 0;
                int Ycoord = 0;
                if ("1".equalsIgnoreCase(position)) {
                    Xcoord = 0;
                    Ycoord = 0;
                }
                if ("2".equalsIgnoreCase(position)) {
                    Xcoord = (bi.getWidth() - watermarkString.length()) / 2;
                    Ycoord = 0;
                }
                if ("3".equalsIgnoreCase(position)) {
                    Xcoord = bi.getWidth() - watermarkString.length();
                    Ycoord = 0;
                }
                if ("4".equalsIgnoreCase(position)) {
                    Xcoord = 0;
                    Ycoord = (bi.getHeight() - watermarkStringSize) / 2;
                }
                if ("5".equalsIgnoreCase(position)) {
                    Xcoord = (bi.getWidth() - watermarkString.length()) / 2;
                    Ycoord = (bi.getHeight() - watermarkStringSize) / 2;
                }
                if ("6".equalsIgnoreCase(position)) {
                    Xcoord = bi.getWidth() - watermarkString.length();
                    Ycoord = (bi.getHeight() - watermarkStringSize) / 2;
                }
                if ("7".equalsIgnoreCase(position)) {
                    Xcoord = 0;
                    Ycoord = bi.getHeight() - watermarkStringSize;
                }
                if ("8".equalsIgnoreCase(position)) {
                    Xcoord = (bi.getWidth() - watermarkString.length()) / 2;
                    Ycoord = bi.getHeight() - watermarkStringSize;
                }
                if ("9".equalsIgnoreCase(position)) {
                    Xcoord = bi.getWidth() - watermarkString.length();
                    Ycoord = bi.getHeight() - watermarkStringSize;
                }
                g.drawString(watermarkString, Xcoord, Ycoord);
                g.dispose();
                BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(new File(filePath)), diskBufferSize);
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
                JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
                param.setQuality(0.9f, false);
                encoder.encode(bi);
                bi = null;
                fos.close();
                fos = null;
            } else if (watermarkImagePath != null) {
                BufferedImage bi = ImageIO.read(formFile.getInputStream());
                int width = bi.getWidth();
                int height = bi.getHeight();
                Image Itemp = compress(watermarkImagePath, width, height);
                Graphics2D g = bi.createGraphics();
                g.setBackground(Color.black);
                int pits[] = new int[2];
                int Xcoord = 0;
                int Ycoord = 0;
                if ("1".equalsIgnoreCase(position)) {
                    Xcoord = 0;
                    Ycoord = 0;
                }
                if ("2".equalsIgnoreCase(position)) {
                    Xcoord = width / 2 - Itemp.getWidth(null) / 2;
                    Ycoord = 0;
                }
                if ("3".equalsIgnoreCase(position)) {
                    Xcoord = width - Itemp.getWidth(null);
                    Ycoord = 0;
                }
                if ("4".equalsIgnoreCase(position)) {
                    Xcoord = 0;
                    Ycoord = height / 2 - Itemp.getHeight(null) / 2;
                }
                if ("5".equalsIgnoreCase(position)) {
                    pits = getMidPels(width, height, Itemp);
                    Xcoord = pits[0];
                    Ycoord = pits[1];
                }
                if ("6".equalsIgnoreCase(position)) {
                    Xcoord = width - Itemp.getWidth(null);
                    Ycoord = height / 2 - Itemp.getHeight(null) / 2;
                }
                if ("7".equalsIgnoreCase(position)) {
                    pits = getLeftDownPels(width, height, Itemp);
                    Xcoord = pits[0];
                    Ycoord = pits[1];
                }
                if ("9".equalsIgnoreCase(position)) {
                    pits = getRightDownPels(width, height, Itemp);
                    Xcoord = pits[0];
                    Ycoord = pits[1];
                }
                if ("8".equalsIgnoreCase(position)) {
                    Xcoord = (width - Itemp.getWidth(null)) / 2;
                    Ycoord = height - Itemp.getHeight(null);
                }
                g.drawImage(Itemp, Xcoord, Ycoord, null);
                g.dispose();
                FileOutputStream out = new FileOutputStream(filePath);
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
                param.setQuality(0.95f, true);
                encoder.encode(bi, param);
                out.close();
                Itemp.flush();
                Itemp = null;
                bi.flush();
                bi = null;
            } else {
                BufferedInputStream in = null;
                BufferedOutputStream fos = null;
                try {
                    in = new BufferedInputStream(formFile.getInputStream());
                    fos = new BufferedOutputStream(new FileOutputStream(new File(filePath)), diskBufferSize);
                    int read = 0;
                    byte buffer[] = new byte[diskBufferSize];
                    while ((read = in.read(buffer, 0, diskBufferSize)) > 0) {
                        fos.write(buffer, 0, read);
                    }
                    buffer = null;
                    fos.flush();
                    in.close();
                    fos.close();
                } finally {
                    in = null;
                    fos = null;
                }
            }
        } else {
            BufferedInputStream in = null;
            BufferedOutputStream fos = null;
            try {
                in = new BufferedInputStream(formFile.getInputStream());
                fos = new BufferedOutputStream(new FileOutputStream(new File(filePath)), diskBufferSize);
                int read = 0;
                byte buffer[] = new byte[diskBufferSize];
                while ((read = in.read(buffer, 0, diskBufferSize)) > 0) {
                    fos.write(buffer, 0, read);
                }
                buffer = null;
                fos.flush();
                in.close();
                fos.close();
            } finally {
                in = null;
                fos = null;
            }
        }
    }
