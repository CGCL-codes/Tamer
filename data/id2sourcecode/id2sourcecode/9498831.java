    private void doUpdate() {
        if (pdffile == null) return;
        try {
            String gs_out = Exec.runToString("gs -sDEVICE=bbox -dNOPAUSE -dBATCH " + pdffile.getName(), pdffile.getParent());
            int bbox_pos = gs_out.indexOf("%%HiResBoundingBox:");
            String bbox = gs_out.substring(bbox_pos + 20);
            String[] bbox_split = bbox.trim().split("\\s+");
            double x = Double.parseDouble(bbox_split[0]);
            double y = Double.parseDouble(bbox_split[1]);
            double w = Double.parseDouble(bbox_split[2]) - x;
            double h = Double.parseDouble(bbox_split[3]) - y;
            setBBoxFromGS(new Rectangle2D.Double(x, y, w, h));
            RandomAccessFile raf = new RandomAccessFile(pdffile, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            PDFFile pdffile_ = new PDFFile(buf);
            page = pdffile_.getPage(0);
            resize();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
