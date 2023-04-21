    @Override
    public void paint(Graphics g) {
        if (cspace != null) try {
            Name.gene = rw;
            g.clearRect(0, 0, getWidth(), getHeight());
            float width = getWidth() - 4;
            float height = getHeight() - 8;
            float[][] bounds = cspace.getBounds();
            float xmul = width / (bounds[1][0] - bounds[0][0]);
            float ymul = height / (bounds[1][1] - bounds[0][1]);
            if (cspace.getDimension() == 1) {
                for (int i = 0; i < width; i++) {
                    float[] col = cspace.getValue(new float[] { i / xmul, 0 });
                    g.setColor(new Color(col[0], col[1], col[2], col[3]));
                    g.fillRect(i, 0, 1, (int) height);
                }
            } else {
                long startTime = System.currentTimeMillis();
                int it0 = 0, it = 0;
                int step = curStep;
                if (avgDrawTime != null) {
                    if (avgDrawTime > 100) {
                        step = ++curStep;
                    }
                }
                BufferedImage bim = new BufferedImage((int) width, (int) height + step - 1, BufferedImage.TYPE_INT_ARGB);
                WritableRaster r = bim.getWritableTile(0, 0);
                DataBuffer d = r.getDataBuffer();
                for (int j = 0; j < height - step + 1; j += step) {
                    for (int i = 0; i < width - step + 1; i += step) {
                        float[] col = cspace.getValue(new float[] { i / xmul, j / ymul });
                        int intval = (((int) (Math.min(1f, (col[3])) * 255f)) << 24) + (((int) (Math.min(1f, col[0]) * 255f)) << 16) + (((int) (Math.min(1f, col[1]) * 255f)) << 8) + (((int) (Math.min(1f, col[2]) * 255f)));
                        int it1 = it;
                        for (int ys = 0; ys < step; ys++) {
                            for (int xs = 0; xs < step; xs++) {
                                d.setElem(it, intval);
                                it++;
                            }
                            it += width - step;
                        }
                        it = it1 + step;
                    }
                    it = it0 = it0 + step * (int) width;
                }
                while (!g.drawImage(bim, 2, 4, null)) ;
                bim.releaseWritableTile(0, 0);
                long span = System.currentTimeMillis() - startTime;
                if (avgDrawTime == null) avgDrawTime = span; else avgDrawTime = (avgDrawTime + span) / 2;
            }
            int it = 0;
            for (RGBA rgba : cspace.getPivots()) {
                int bwidth = 4;
                float x = 2f + rgba.point.get(0).evaluate() * width / (bounds[1][0] - bounds[0][0]);
                float y1 = rgba.point.size() > 1 ? 4f + rgba.point.get(1).evaluate() * height / (bounds[1][1] - bounds[0][1]) : bwidth / 2;
                float y2 = y1;
                if (cspace.getDimension() == 1) {
                    y2 = getHeight() - 1 - bwidth / 2;
                }
                g.setColor(it == lastActive ? Color.GRAY : Color.WHITE);
                g.fillRect((int) x - bwidth / 2 + 1, (int) y1 - bwidth / 2 + 1, bwidth - 2, (int) (y2 - y1 + bwidth - 2));
                g.drawLine((int) (x - bwidth), (int) (y1 - bwidth), (int) (x + bwidth), (int) (y1 + bwidth));
                g.drawLine((int) (x - bwidth), (int) (y1 + bwidth), (int) (x + bwidth), (int) (y1 - bwidth));
                g.setColor(Color.BLACK);
                g.drawRect((int) x - bwidth / 2, (int) y1 - bwidth / 2, bwidth, (int) (y2 - y1 + bwidth));
                it++;
            }
        } catch (ParseException ex) {
        }
    }
