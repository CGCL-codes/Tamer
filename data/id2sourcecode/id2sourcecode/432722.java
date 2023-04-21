    public void drawCube(Graphics g) {
        long now;
        Color c;
        int i;
        double theCos, theSin, ouverture;
        for (i = 0; i < 3; i++) {
            anglesxyz[i].compute();
        }
        theCos = 2 * d * (Math.cos(angle));
        theSin = 2 * d * (Math.sin(angle));
        sommets[0] = new Edge(-d, d + theCos, theSin);
        sommets[1] = new Edge(d, d + theCos, theSin);
        sommets[2] = new Edge(-d - theCos, d, theSin);
        sommets[3] = new Edge(-d, d, 0);
        sommets[4] = new Edge(d, d, 0);
        sommets[5] = new Edge(d + theCos, d, +theSin);
        sommets[6] = new Edge(d + theCos - 2 * d * Math.cos(Math.PI - 2 * angle), d, theSin + 2 * d * Math.sin(Math.PI - 2 * angle));
        sommets[7] = new Edge(-d - theCos, -d, theSin);
        sommets[8] = new Edge(-d, -d, 0);
        sommets[9] = new Edge(d, -d, 0);
        sommets[10] = new Edge(d + theCos, -d, theSin);
        sommets[11] = new Edge(d + theCos - 2 * d * Math.cos(Math.PI - 2 * angle), -d, theSin + 2 * d * Math.sin(Math.PI - 2 * angle));
        sommets[12] = new Edge(-d, -d - theCos, theSin);
        sommets[13] = new Edge(d, -d - theCos, theSin);
        for (i = 0; i < 14; i++) {
            sommets[i].rotate(lesCos, lesSin, anglesxyz[0].getValue(), anglesxyz[1].getValue(), anglesxyz[2].getValue());
            sommets[i].decal(size.width / 2, size.height / 2, 10 * d);
        }
        for (i = 0; i < 6; i++) {
            orderOfApparition[i] = i;
        }
        boolean done = false;
        int k;
        while (done == false) {
            done = true;
            for (i = 0; i < 5; i++) {
                if (FaceDuCube[orderOfApparition[i + 1]].isFarest(FaceDuCube[orderOfApparition[i]]) == true) {
                    k = orderOfApparition[i];
                    orderOfApparition[i] = orderOfApparition[i + 1];
                    orderOfApparition[i + 1] = k;
                    done = false;
                }
            }
        }
        for (i = 0; i < 50; i++) {
            float f = (float) ((49.0 - (float) i) / 49.0);
            Color bckgcolor = new Color(0, 0, f);
            bufferGraphics.setColor(bckgcolor);
            bufferGraphics.fillRect(0, 0, size.width, (int) ((double) size.height * ((49.0 - (double) i) / 49.0)));
        }
        bufferGraphics.setColor(Color.yellow);
        bufferGraphics.drawString("Pierre Lindenbaum PhD.2000 http://plindenbaum.blogspot.com", 24, 24);
        for (i = 0; i < 6; i++) {
            float f = (float) (0.5 - 0.3 * (float) ((5.0 - (float) i) / 5.0));
            Color bckgcolor = new Color(0, f, 0);
            bufferGraphics.setColor(bckgcolor);
            FaceDuCube[orderOfApparition[i]].draw(bufferGraphics);
        }
        now = System.currentTimeMillis();
        if (now - when > 0) {
            when = now;
            angle += dangle;
            if (angle < -Math.PI / 2) {
                angle = -Math.PI / 2;
                dangle *= -1;
            }
            if (angle > Math.PI / 2) {
                angle = Math.PI / 2;
                dangle *= -1;
            }
        }
        g.drawImage(buffer, 0, 0, this);
    }
