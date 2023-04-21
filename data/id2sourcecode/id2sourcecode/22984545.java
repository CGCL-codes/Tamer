    public void run() {
        try {
            byte msg[] = new byte[0x1000];
            while (!isInterrupted()) {
                Socket s = ss.accept();
                try {
                    InputStream is = s.getInputStream();
                    OutputStream os = new BufferedOutputStream(s.getOutputStream(), 0x8000);
                    int ix = is.read(msg);
                    String itemName = null;
                    scan: for (int i = 0; i < ix; i++) {
                        if (msg[i] == '/') {
                            for (int j = i + 1; j < msg.length; j++) {
                                if (msg[j] == ' ') {
                                    itemName = new String(msg, i, j - i);
                                    break scan;
                                }
                            }
                        }
                    }
                    String clientHost = s.getInetAddress().getHostAddress();
                    if (log != null) log.println("Client " + clientHost + " request: " + itemName);
                    if (itemName == null) os.write(bye); else if (itemName.indexOf('.') == -1 && itemName.indexOf('/', 1) == -1) {
                        try {
                            int ia = itemName.indexOf(':') != -1 ? itemName.indexOf(':') : itemName.indexOf('-') != -1 ? itemName.indexOf('-') : itemName.indexOf('!') != -1 ? itemName.indexOf('!') : itemName.length();
                            int ib = itemName.indexOf('-') != -1 ? itemName.indexOf('-') : itemName.indexOf('!') != -1 ? itemName.indexOf('!') : itemName.length();
                            int ic = itemName.indexOf('!') != -1 ? itemName.indexOf('!') : itemName.length();
                            String clientPort = ia > 1 ? itemName.substring(1, ia) : "0";
                            String localPort = ib > ia ? itemName.substring(ia + 1, ib) : "0";
                            String proxyName = ic > ib ? itemName.substring(ib + 1, ic) : "main";
                            Integer.parseInt(clientPort);
                            Integer.parseInt(localPort);
                            int proxyPort = Remote.getDefaultClientPort();
                            if (itemName.indexOf('!') == -1) {
                                byte iex[] = ("<PARAM NAME=\"clientHost\" VALUE=\"" + clientHost + "\">\r\n" + "<PARAM NAME=\"clientPort\" VALUE=\"" + clientPort + "\">\r\n" + "<PARAM NAME=\"localPort\"  VALUE=\"" + localPort + "\">\r\n" + "<PARAM NAME=\"proxyPort\"  VALUE=\"" + proxyPort + "\">\r\n" + "<PARAM NAME=\"proxyName\"  VALUE=\"" + proxyName + "\">\r\n").getBytes();
                                byte nav[] = ("clientHost=\"" + clientHost + "\"\r\n" + "clientPort=\"" + clientPort + "\"\r\n" + "localPort=\"" + localPort + "\"\r\n" + "proxyPort=\"" + proxyPort + "\"\r\n" + "proxyName=\"" + proxyName + "\"\r\n").getBytes();
                                os.write(apl);
                                os.write(top);
                                os.write(iex);
                                os.write(mid);
                                os.write(nav);
                                os.write(end);
                            } else {
                                byte obj[] = ("  href=\"" + clientPort + ':' + localPort + '-' + proxyName + "!\">\r\n").getBytes();
                                byte arg[] = ("    <argument>//" + Remote.getDefaultClientHost() + ':' + proxyPort + '/' + proxyName + "</argument>\r\n" + "    <argument>" + clientPort + "</argument>\r\n" + "    <argument>" + clientHost + "</argument>\r\n" + "    <argument>" + localPort + "</argument>\r\n").getBytes();
                                os.write(jws);
                                os.write(tip);
                                os.write(obj);
                                os.write(xml);
                                os.write(arg);
                                os.write(out);
                            }
                        } catch (Exception x) {
                            os.write(bye);
                        }
                    } else if (!itemName.endsWith("service.jar")) {
                        if (itemName.equals("/favicon.ico") || itemName.endsWith(".jar") || itemName.endsWith(".class") || itemName.endsWith(".gif") || itemName.endsWith(".jpg") || itemName.endsWith(".jpeg")) try {
                            InputStream ris = getClass().getResourceAsStream(itemName);
                            if (ris == null) ris = new FileInputStream('.' + itemName);
                            os.write(itemName.endsWith(".jar") ? jarHdr : itemName.endsWith(".class") ? classHdr : imgHdr);
                            os.write(formatter.format(new Date(new File('.' + itemName).lastModified())).getBytes());
                            os.write("\r\nConnection: close\r\n\r\n".getBytes());
                            for (int i = ris.read(msg); i != -1; i = ris.read(msg)) os.write(msg, 0, i);
                            ris.close();
                        } catch (Exception x) {
                            os.write(bye);
                        } else os.write(bye);
                    } else os.write(bye);
                    os.flush();
                    os.close();
                    is.close();
                } catch (Exception x) {
                    x.printStackTrace();
                }
                try {
                    s.close();
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        try {
            ss.close();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
