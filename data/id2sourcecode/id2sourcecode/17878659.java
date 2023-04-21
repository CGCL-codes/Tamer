    public void run() {
        int qpos = -1;
        try {
            if (gtype == '7' && (qpos = gkey.indexOf('?')) < 0) {
                PrintStream ps = new PrintStream(os, false, encoding);
                ps.print("<html><head><title>Searchable Gopher Index</title></head>\n<body><h1>Searchable Gopher Index</h1><isindex>\n</body></html>\n");
            } else if (gtype != '1' && gtype != '7') {
                byte buf[] = new byte[2048];
                try {
                    int n;
                    while ((n = serverInput.read(buf)) >= 0) os.write(buf, 0, n);
                } catch (Exception e) {
                }
            } else {
                PrintStream ps = new PrintStream(os, false, encoding);
                String title = null;
                if (gtype == '7') title = "Results of searching for \"" + gkey.substring(qpos + 1) + "\" on " + u.getHost(); else title = "Gopher directory " + gkey + " from " + u.getHost();
                ps.print("<html><head><title>");
                ps.print(title);
                ps.print("</title></head>\n<body>\n<H1>");
                ps.print(title);
                ps.print("</h1><dl compact>\n");
                DataInputStream ds = new DataInputStream(serverInput);
                String s;
                while ((s = ds.readLine()) != null) {
                    int len = s.length();
                    while (len > 0 && s.charAt(len - 1) <= ' ') len--;
                    if (len <= 0) continue;
                    int key = s.charAt(0);
                    int t1 = s.indexOf('\t');
                    int t2 = t1 > 0 ? s.indexOf('\t', t1 + 1) : -1;
                    int t3 = t2 > 0 ? s.indexOf('\t', t2 + 1) : -1;
                    if (t3 < 0) {
                        continue;
                    }
                    String port = t3 + 1 < len ? ":" + s.substring(t3 + 1, len) : "";
                    String host = t2 + 1 < t3 ? s.substring(t2 + 1, t3) : u.getHost();
                    ps.print("<dt><a href=\"gopher://" + host + port + "/" + s.substring(0, 1) + encodePercent(s.substring(t1 + 1, t2)) + "\">\n");
                    ps.print("<img align=middle border=0 width=25 height=32 src=");
                    switch(key) {
                        default:
                            ps.print(System.getProperty("java.net.ftp.imagepath.file"));
                            break;
                        case '0':
                            ps.print(System.getProperty("java.net.ftp.imagepath.text"));
                            break;
                        case '1':
                            ps.print(System.getProperty("java.net.ftp.imagepath.directory"));
                            break;
                        case 'g':
                            ps.print(System.getProperty("java.net.ftp.imagepath.gif"));
                            break;
                    }
                    ps.print(".gif align=middle><dd>\n");
                    ps.print(s.substring(1, t1) + "</a>\n");
                }
                ps.print("</dl></body>\n");
                ps.close();
            }
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(encoding + " encoding not found");
        } catch (IOException e) {
        } finally {
            try {
                closeServer();
                os.close();
            } catch (IOException e2) {
            }
        }
    }
