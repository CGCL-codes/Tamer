    public void wonderize(JWondrousMachine jwm, boolean play, boolean draw) {
        if (play) {
            int l = jwm.getLimit();
            if (l != curr_lim) {
                resetSpectrum(l);
            }
            int i = jwm.getInstrument();
            if (i != curr_inst) {
                JMIDI.setChannel(jwm.getChannel(), i);
                curr_inst = i;
            }
            int p = jwm.getCurrentPitch();
            if (p != last_pitch) {
                JMIDI.getChannel(jwm.getChannel()).noteOff(last_pitch);
                JMIDI.getChannel(jwm.getChannel()).noteOn(p, jwm.getVolume());
                last_pitch = p;
            }
        }
        if (draw) {
            Graphics g = buff_img.getGraphics();
            g.setColor(new Color(spec.getColor(jwm.getCurrentWondrousness())));
            g.drawOval(MID_X - curr_rad, MID_Y - curr_rad, curr_rad * 2, curr_rad * 2);
            repaint();
            if (++curr_rad > MAX_RAD) {
                curr_rad = 0;
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
