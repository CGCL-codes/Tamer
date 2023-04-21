    public static void openURL(String url) {
        String osName = SysProperties.getStringSetting("os.name", "linux").toLowerCase();
        Runtime rt = Runtime.getRuntime();
        try {
            String browser = SysProperties.BROWSER;
            if (browser != null) {
                if (browser.indexOf("%url") >= 0) {
                    String[] args = StringUtils.arraySplit(browser, ',', false);
                    for (int i = 0; i < args.length; i++) {
                        args[i] = StringUtils.replaceAll(args[i], "%url", url);
                    }
                    rt.exec(args);
                } else if (osName.indexOf("windows") >= 0) {
                    rt.exec(new String[] { "cmd.exe", "/C", browser, url });
                } else {
                    rt.exec(new String[] { browser, url });
                }
                return;
            }
            try {
                Class<?> desktopClass = Class.forName("java.awt.Desktop");
                Boolean supported = (Boolean) desktopClass.getMethod("isDesktopSupported").invoke(null, new Object[0]);
                URI uri = new URI(url);
                if (supported.booleanValue()) {
                    Object desktop = desktopClass.getMethod("getDesktop").invoke(null, new Object[0]);
                    desktopClass.getMethod("browse", URI.class).invoke(desktop, uri);
                    return;
                }
            } catch (Exception e) {
            }
            if (osName.indexOf("windows") >= 0) {
                rt.exec(new String[] { "rundll32", "url.dll,FileProtocolHandler", url });
            } else if (osName.indexOf("mac") >= 0) {
                Runtime.getRuntime().exec(new String[] { "open", url });
            } else {
                String[] browsers = { "firefox", "mozilla-firefox", "mozilla", "konqueror", "netscape", "opera" };
                boolean ok = false;
                for (String b : browsers) {
                    try {
                        rt.exec(new String[] { b, url });
                        ok = true;
                        break;
                    } catch (Exception e) {
                    }
                }
                if (!ok) {
                    System.out.println("Please open a browser and go to " + url);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to start a browser to open the URL " + url);
            e.printStackTrace();
        }
    }
