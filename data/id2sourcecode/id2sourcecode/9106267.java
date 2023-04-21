    void draw(boolean erase) {
        if (gc == null) gc = canvas.getGraphics();
        int ax = hostA.getRenderedX();
        int ay = hostA.getRenderedY();
        int bx = hostB.getRenderedX();
        int by = hostB.getRenderedY();
        drawCommLink(erase, ax, ay, bx, by);
        int midX = (ax + bx) / 2;
        int midY = (ay + by) / 2;
        midX += OFFSET * (index - 1);
        midY += OFFSET * (index - 1);
        if (erase) gc.setColor(Settings.COLOR_BG); else if (packet instanceof UDPPacket) gc.setColor(Settings.COLOR_P_UDP_H); else if (packet instanceof TCPPacket) gc.setColor(Settings.COLOR_P_TCP_H); else if (packet instanceof ICMPPacket) gc.setColor(Settings.COLOR_P_ICMP_H); else if (packet instanceof IGMPPacket) gc.setColor(Settings.COLOR_P_IGMP_H); else gc.setColor(Settings.COLOR_P_UNKNOWN_H);
        gc.drawString(description + (Settings.SHOW_COUNTS ? " (" + packetCount + ":" + byteCount + ")" : ""), midX, midY);
    }
