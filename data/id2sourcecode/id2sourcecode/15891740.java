    public void setAwtRasterPalette(WritableRaster raster, IPalette palette) {
        m_src_index_color_raster = raster;
        if (palette != null) {
            try {
                byte[] colors = palette.getIndexColors();
                int color_count = palette.getIndexColorCount();
                int transparent_color_index = palette.getTransparentColorIndex();
                byte[] ra = new byte[color_count];
                byte[] ga = new byte[color_count];
                byte[] ba = new byte[color_count];
                byte[] ralpha = new byte[color_count];
                for (int i = 0, j = 0; (i < colors.length) && (j < color_count); i += 3, ++j) {
                    ra[j] = colors[i];
                    ga[j] = colors[i + 1];
                    ba[j] = colors[i + 2];
                    ralpha[j] = (byte) ((j == transparent_color_index) ? 0 : 255);
                }
                IndexColorModel icm = new IndexColorModel(8, color_count, ra, ga, ba, ralpha);
                BufferedImage new_image = new BufferedImage(icm, m_src_index_color_raster, icm.isAlphaPremultiplied(), null);
                m_image = createBuffer(new_image);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
    }
