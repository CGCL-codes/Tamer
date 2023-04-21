    public void run() {
        if (theConnection == null) {
            return;
        }
        String method;
        String ct;
        String version = "";
        String referer = null;
        String accept = "text/vnd.wap.wml, image/vnd.wap.wbmp, */*";
        String user_agent = "Nokia7110/1.0 (04.76) aplpi.com v0.5j";
        boolean urlIsFile = false;
        boolean convertWBMP = false;
        File theFile;
        PrintStream os = null;
        try {
            os = new PrintStream(theConnection.getOutputStream());
            InputStream inputstream = theConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputstream);
            BufferedReader is = new BufferedReader(isr);
            String get = null;
            for (int i = 1; i < 1000 && (get = is.readLine()) == null; i++) ;
            if (get == null) {
                System.err.println("no input...");
                os.close();
                theConnection.close();
                return;
            }
            System.err.println("get:" + get);
            StringTokenizer st = new StringTokenizer(get);
            method = st.nextToken();
            if (!(method.equals("GET") || method.equals("POST"))) {
                return;
            }
            String file = st.nextToken();
            if (file.equals("/")) {
                file = "/index.html";
            }
            if (file.toLowerCase().endsWith(".wbmp&.gif")) {
                file = file.substring(0, file.length() - 5);
                convertWBMP = true;
            }
            if (st.hasMoreTokens()) {
                version = st.nextToken();
            }
            while ((get = is.readLine()) != null) {
                if (get.toLowerCase().startsWith("referer: ")) {
                    referer = get.substring(9, get.length());
                }
                if (get.trim().equals("")) break;
            }
            URL url = null;
            if (file.toLowerCase().startsWith("/http:")) {
                url = new URL(file.substring(1, file.length()));
            } else {
                File f;
                if (file.toLowerCase().startsWith("/file://")) {
                    url = new URL(file.substring(1, file.length()));
                    f = new File(url.getFile());
                    url = new URL("file", "", f.toString());
                } else {
                    f = new File(URLDecode(file.substring(1, file.length())));
                    String p = f.getAbsolutePath();
                    boolean java2 = false;
                    if (java2) {
                        url = f.toURL();
                    } else {
                        p = p.replace(File.separatorChar, '/');
                        while (!p.startsWith("//")) {
                            p = "/" + p;
                        }
                        if (!p.endsWith("/") && f.isDirectory()) {
                            p = p + "/";
                        }
                        url = new URL("file", "", p);
                        f = new File(url.getFile());
                    }
                }
                if (f.canRead()) {
                    urlIsFile = true;
                    os.print("HTTP/1.0 200 OK\n");
                    Date now = new Date();
                    os.print("Date: " + now + "\n");
                    os.print("Server: " + server + "\n");
                    os.print("Content-length: " + f.length() + "\n");
                    os.print("Connection: close\n");
                    os.print("Content-type: " + guessContentType(f.toString()) + "\n\n");
                } else if (f.exists()) {
                    os.print("HTTP/1.0 200 OK\n");
                    Date now = new Date();
                    os.print("Date: " + now + "\n");
                    os.print("Server: " + server + "\n");
                    os.print("Connection: close\n");
                    os.print("Content-type: " + guessContentType(".wml") + "\n\n");
                    os.println("<wml><card>><title>Error!</title><p>Forbidden!</p></card></wml>");
                    os.close();
                    theConnection.close();
                    return;
                } else {
                    os.print("HTTP/1.0 200 OK\n");
                    Date now = new Date();
                    os.print("Date: " + now + "\n");
                    os.print("Connection: close\n");
                    os.print("Server: " + server + "\n");
                    os.print("Content-type: " + guessContentType(".wml") + "\n\n");
                    os.println("<wml><card><title>Error!</title><p>File not found!</p></card></wml>");
                    os.close();
                    theConnection.close();
                    return;
                }
            }
            URLConnection connection = url.openConnection();
            if (connection != null) {
                connection.setRequestProperty("Accept", accept);
                connection.setRequestProperty("User-Agent", user_agent);
                if (referer != null) {
                    connection.setRequestProperty("Referer", referer);
                }
                if (method.equals("POST")) {
                    connection.setDoOutput(true);
                    OutputStream cs = connection.getOutputStream();
                    while (is.ready()) {
                        cs.write(is.read());
                    }
                    cs.close();
                }
                connection.connect();
                if (!urlIsFile) {
                    os.print("HTTP/1.0 200 OK\n");
                    os.print("Connection: close\n");
                    boolean headers = true;
                    for (int i = 1; true; i++) {
                        String key = connection.getHeaderFieldKey(i);
                        if (key == null) break;
                        headers = true;
                        String value = connection.getHeaderField(i);
                        os.println(key + ": " + value);
                    }
                    if (headers) os.print("\n");
                }
                String txtLine;
                InputStream in = connection.getInputStream();
                if (convertWBMP) {
                    WBMPimg wbmp = new WBMPimg();
                    wbmp.wbmp2gif(wricproxy.graphicsContext, in, os);
                    os.close();
                    theConnection.close();
                    return;
                } else {
                    int r;
                    while ((r = in.read()) >= 0) {
                        os.write(r);
                    }
                    os.close();
                }
            } else {
            }
            os.close();
            return;
        } catch (IOException e) {
            System.err.println(e);
            try {
                os.print("HTTP/1.0 200 OK\n");
                Date now = new Date();
                os.print("Date: " + now + "\n");
                os.print("Connection: close\n");
                os.print("Server: " + server + "\n");
                os.print("Content-type: " + guessContentType(".wml") + "\n\n");
                os.println("<wml><card><title>Error!</title><p>" + e.toString() + "</p></card></wml>");
                os.close();
                theConnection.close();
            } catch (Exception xe) {
            }
        }
    }
