    SgfFile(Game game1, URL url, JavaGO javago1) {
        javago = javago1;
        game = game1;
        int i = 0;
        if (i > 0) log.debug("SgfFile.SgfFile()");
        try {
            InputStream inputstream = url.openStream();
            DataInputStream datainputstream = new DataInputStream(inputstream);
            String s;
            while ((s = datainputstream.readLine()) != null) {
                if (i > 1) log.debug("SgfFile.SgfFile : " + s);
                i_start = 0;
                for (int j = 0; j < s.length(); j++) {
                    char c = s.charAt(j);
                    if (i > 2) log.debug("SgfFile.SgfFile : " + c);
                    if (Character.isUpperCase(c) && !inside) {
                        n++;
                        if (n == 1) c1 = c; else if (n == 2) c2 = c;
                    } else if (c == '[') {
                        inside = true;
                        i_start = j + 1;
                    } else if (c == ']') {
                        String s1;
                        if (c1 == 0) s1 = ""; else if (c2 == 0) s1 = "" + c1; else s1 = "" + c1 + c2;
                        param = param + s.substring(i_start, j);
                        sgfCommand(s1, param);
                        init();
                    } else if (c == ';') init();
                }
                if (i_start != -1) param = param + s.substring(i_start, s.length());
            }
            game.setNode(-1);
            datainputstream.close();
            inputstream.close();
            return;
        } catch (MalformedURLException malformedurlexception) {
            javago.appendTextln("Bad URL file : " + url);
            log.error("MalformedURLException: " + malformedurlexception);
            return;
        } catch (IOException ioexception) {
            javago.appendTextln("Problem reading URL file : " + url);
            log.error("IOException: " + ioexception);
            return;
        }
    }
