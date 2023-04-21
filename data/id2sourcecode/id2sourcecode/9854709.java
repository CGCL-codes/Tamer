    private void populateDemoList() {
        List demoList = new ArrayList();
        URL url = BrowserMenuBar.class.getResource("/demos/file-list.txt");
        if (url != null) {
            try {
                InputStream is = url.openStream();
                InputStreamReader reader = new InputStreamReader(is);
                LineNumberReader lnr = new LineNumberReader(reader);
                String line = null;
                while ((line = lnr.readLine()) != null) {
                    demoList.add(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Iterator itr = demoList.iterator(); itr.hasNext(); ) {
                String s = (String) itr.next();
                String s1[] = s.split(",");
                allDemos.put(s1[0], s1[1]);
            }
        }
    }
