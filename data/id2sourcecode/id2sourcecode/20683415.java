    private void loadRes() {
        jpng = new Jpng();
        try {
            java.io.InputStream stream = Jpng.class.getResourceAsStream(ConsEnv.RES_ICON + "wait.png");
            jpng.readIcons(stream, 16, 16);
            stream.close();
            jpng.setIt(0);
            jpng.setButton(btApply);
        } catch (Exception exp) {
            Logs.exception(exp);
        }
        if (MpwdMdl.getRunMode() == RunMode.dev) {
            return;
        }
        javax.swing.Icon icon = null;
        try {
            StringBuilder buf = new StringBuilder();
            buf.append("http://mpwd.sinaapp.com/bar.php?v=").append(ConsEnv.VERSIONS);
            buf.append("&k=").append(MpwdMdl.getAppGuid());
            buf.append("&m=").append(MpwdMdl.getRunMode());
            buf.append("&p=").append(Char.escape(System.getProperty("os.name")));
            buf.append("_").append(Char.escape(System.getProperty("os.arch")));
            buf.append("_").append(Char.escape(System.getProperty("os.version")));
            java.net.URL url = new java.net.URL(buf.toString());
            java.io.InputStream stream = url.openStream();
            icon = new javax.swing.ImageIcon(javax.imageio.ImageIO.read(stream));
            stream.close();
        } catch (Exception ex) {
            Logs.exception(ex);
            icon = null;
        }
        if (icon != null) {
            if (lbLogo != null) {
                final String tgi = "";
                synchronized (tgi) {
                    lbLogo.setIcon(icon);
                }
            }
        }
    }
