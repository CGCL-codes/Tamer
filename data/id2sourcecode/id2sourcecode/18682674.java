    public boolean setDir() {
        javago.appendTextln("List directory : " + url_dir);
        String s1 = "";
        boolean flag = true;
        String s3;
        if (url_dir != null) s3 = url_dir.toString(); else s3 = "";
        url_list = new Choice();
        try {
            InputStream inputstream = url_dir.openStream();
            DataInputStream datainputstream = new DataInputStream(inputstream);
            String s;
            while ((s = datainputstream.readLine()) != null) {
                int i = 0;
                String s4 = s;
                while (i != -1) {
                    i = s4.indexOf("href");
                    if (i == -1) {
                        i = s4.indexOf("HREF");
                        if (i == -1) continue;
                    }
                    s4 = s4.substring(i + 5);
                    int j = s4.indexOf('"');
                    s4 = s4.substring(j + 1);
                    int k = s4.indexOf('"');
                    String s5 = s4.substring(0, k);
                    s4 = s4.substring(k + 1);
                    j = s5.indexOf("%7C");
                    char c = s3.charAt(7);
                    if (c != '|' && c != ':') c = '|';
                    if (j != -1) s5 = s5.substring(0, j) + c + s5.substring(j + 3);
                    j = s5.indexOf("%20");
                    if (j != -1) s5 = s5.substring(0, j) + " " + s5.substring(j + 3);
                    String s2 = s5;
                    if (s2.startsWith("/")) url_list.addItem(s3.substring(0, 5) + s2); else if (s2.startsWith("http") || s2.startsWith("file")) url_list.addItem(s2); else url_list.addItem(s3 + s2);
                    javago.appendText(".");
                }
            }
            javago.appendText(" ok");
            datainputstream.close();
            inputstream.close();
            url_list.select(0);
        } catch (Exception exception) {
            log.error(exception);
            flag = false;
        }
        url_list.addItem(name_of_dir_games);
        return flag;
    }
