    @Override
    public void run() {
        boolean bExit = false;
        while (!bExit) {
            if (_bUpdateIdList) {
                _bUpdateIdList = false;
                URL url;
                try {
                    url = new URL(_serverURL + "get.php?cmd=list");
                    URLConnection con = url.openConnection();
                    con.connect();
                    Vector<Integer> ids = new Vector<Integer>();
                    Scanner s = new Scanner(con.getInputStream());
                    while (s.hasNextLine()) {
                        StringTokenizer st = new StringTokenizer(s.nextLine(), ",");
                        while (st.hasMoreTokens()) {
                            ids.add(Integer.parseInt(st.nextToken()));
                        }
                    }
                    for (ObjectLoaderListener ol : _oli) ol.recvObjectIdList(ids);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
            while (!_cmd.isEmpty()) {
                URL url = null;
                try {
                    url = new URL(_serverURL + "get.php?obj=" + _cmd.pop());
                    URLConnection con = url.openConnection();
                    con.connect();
                    InputStream is = con.getInputStream();
                    GeoObject gobj = parseObject(is);
                    for (ObjectLoaderListener ol : _oli) ol.recvObject(gobj);
                } catch (Exception e) {
                    Logger.getRootLogger().error(e.getMessage() + "(" + url.toString() + ")");
                    System.err.println(e.getMessage());
                }
            }
            waitForTask();
        }
    }
