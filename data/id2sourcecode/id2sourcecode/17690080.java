        void drawLUT() {
            Graphics g = lutImage.getGraphics();
            int[] rgb = new int[256];
            ColorModel currentLUT = i5d.getChannelDisplayProperties(cColorChooser.cControl.currentChannel).getColorModel();
            if (!(currentLUT instanceof IndexColorModel)) throw new IllegalArgumentException("Color Model has to be IndexColorModel.");
            for (int i = 0; i < 256; i++) {
                rgb[i] = ((IndexColorModel) currentLUT).getRGB(i);
            }
            g.setColor(Color.black);
            g.drawRect(0, 0, lutWidth + 2, lutHeight + 2);
            g.setColor(new Color(rgb[255]));
            g.drawLine(1, 1, lutWidth, 1);
            for (int i = 0; i < 128; i++) {
                g.setColor(new Color(rgb[255 - i * 2]));
                g.drawLine(1, i + 2, lutWidth, i + 2);
            }
        }
