    public static void main(String args[]) {
        String s;
        WeirdX weirdx = new WeirdX();
        Properties props = new Properties();
        try {
            InputStream rs = null;
            if (args != null && args.length >= 2 && args[1] != null) {
                URL propsurl = new URL(args[1]);
                rs = propsurl.openStream();
            } else {
                rs = weirdx.getClass().getResourceAsStream("/props");
            }
            if (rs != null) props.load(rs);
        } catch (Exception e) {
        }
        try {
            String root = props.getProperty("user.dir", null);
            File guess = new File(new File(root, "config"), "props");
            props.load(new FileInputStream(guess));
        } catch (Exception e) {
        }
        Properties sprops = null;
        try {
            sprops = System.getProperties();
        } catch (Exception e) {
            System.err.println("Unable to read system properties: " + e);
            sprops = new Properties();
        }
        for (Enumeration e = props.keys(); e.hasMoreElements(); ) {
            String key = (String) (e.nextElement());
            if (key.startsWith("weirdx.") && sprops.get(key) == null) {
                sprops.put(key, (String) (props.get(key)));
            }
        }
        try {
            System.setProperties(sprops);
            props = System.getProperties();
        } catch (Exception e) {
            System.err.println("Error updating system properties: " + e);
        }
        try {
            try {
                s = (String) props.get("weirdx.ddxwindow");
                if (s != null) {
                    Window.installDDXWindow(s);
                }
            } catch (Exception ee) {
                System.err.println(ee);
            }
            try {
                s = (String) props.get("weirdx.display.width");
                if (s != null) {
                    width = Short.parseShort(s);
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.display.height");
                if (s != null) {
                    height = Short.parseShort(s);
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.display.autosize");
                if (s != null) {
                    if (Boolean.valueOf(s).booleanValue()) {
                        Toolkit toolkit = Toolkit.getDefaultToolkit();
                        Dimension ScrSize = toolkit.getScreenSize();
                        int widthreduce = 20;
                        int heightreduce = 60;
                        try {
                            s = (String) props.get("weirdx.display.autosize.widthreduce");
                            if (s != null) {
                                widthreduce = Short.parseShort(s);
                            }
                        } catch (Exception ee) {
                        }
                        try {
                            s = (String) props.get("weirdx.display.autosize.heightreduce");
                            if (s != null) {
                                heightreduce = Short.parseShort(s);
                            }
                        } catch (Exception ee) {
                        }
                        width = (short) (ScrSize.width - widthreduce);
                        height = (short) (ScrSize.height - heightreduce);
                    }
                }
            } catch (Exception ee) {
                System.err.println(ee);
            }
            try {
                s = (String) props.get("weirdx.display.visual");
                if (s != null) {
                    visuals = s;
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.windowmode");
                if (s != null) {
                    weirdx.mode = s;
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.myaddress");
                if (s != null) {
                    myAddress = s;
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.displaynum");
                if (s != null) {
                    weirdx.displaynum = Integer.parseInt(s);
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.display.acl");
                if (s != null) {
                    Acl.parse(s);
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.display.threebutton");
                if (s.equals("yes")) {
                    threeButton = true;
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.display.copypaste");
                if (s.equals("yes")) {
                    copypaste = true;
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.display.keymap");
                if (s != null) {
                    keymap = s;
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.display.charset");
                if (s != null && s.length() > 0) {
                    charset = s;
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.xdmcp.mode");
                if (s != null) {
                    String ss = (String) props.get("weirdx.xdmcp.address");
                    if (s.equals("query")) {
                        xdmcp = new XDMCP(ss, myAddress, weirdx.displaynum);
                    } else if (s.equals("broadcast")) {
                        xdmcp = new XDMCP(XDMCP.BroadcastQuery, ss, myAddress, weirdx.displaynum);
                    } else if (s.equals("indirect")) {
                        xdmcp = new XDMCP(XDMCP.IndirectQuery, ss, myAddress, weirdx.displaynum);
                    }
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.xrexec");
                if (s.equals("yes")) {
                    xrexec = new XRexec(myAddress, weirdx.displaynum);
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.sshrexec");
                ssshrexec = s;
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.jesd");
                if (s != null && s.equals("yes")) {
                    jesd = s;
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.jdxpc");
                if (s != null && s.length() != 0 && !s.equals("no")) {
                    jdxpc = s;
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.jdxpcport");
                if (s != null) {
                    jdxpcport = Integer.parseInt(s);
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.jdxpc.socket");
                if (s != null) {
                    jdxpcsocket = s;
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.jdxpc.serverproxy");
                if (s != null) {
                    jdxpcserverproxy = s;
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.extension");
                if (s != null) {
                    weirdx.extension = s;
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.display.background.alpha");
                if (s != null) {
                    alphaBackground = s;
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.logo");
                if (s != null) {
                    weirdx.logo = s;
                }
            } catch (Exception ee) {
            }
            try {
                s = (String) props.get("weirdx.displaysocket");
                if (s != null) {
                    try {
                        displaySocketClass = Class.forName(s);
                        if (!s.equals("com.jcraft.weirdx.DisplaySocket6k")) System.out.println(s + " is used for DisplaySocket");
                    } catch (Exception e) {
                        System.err.println(e);
                        displaySocketClass = com.jcraft.weirdx.DisplaySocket6k.class;
                    }
                }
            } catch (Exception ee) {
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        weirdx.weirdx = weirdx;
        Container container = new Frame("WeirdX");
        ((Frame) container).addWindowListener(new WindowAdapter() {

            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }

            public void windowClosing(WindowEvent e) {
                ((Frame) e.getWindow()).dispose();
                System.exit(0);
            }
        });
        try {
            weirdx.weirdx_start(container);
        } catch (Exception e) {
            System.out.println("main: " + e);
        }
    }
