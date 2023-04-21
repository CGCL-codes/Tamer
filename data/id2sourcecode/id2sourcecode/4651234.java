    private void loadConfig(URL urlConfigFile) {
        try {
            InputStream is = urlConfigFile.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String sLine = br.readLine();
            while (sLine != null) {
                int iPos = sLine.indexOf('#');
                if (iPos != -1) {
                    sLine = sLine.substring(0, iPos);
                }
                sLine.trim();
                if (sLine.length() == 0) {
                    sLine = br.readLine();
                    continue;
                }
                String sProp;
                iPos = sLine.indexOf('=');
                if (iPos > 0 && (sProp = recognizeProperty(sLine.substring(0, iPos))) != null) {
                    addProperty(sProp, sLine.substring(iPos + 1, sLine.length()).trim());
                } else {
                    iPos = sLine.indexOf(';');
                    if (iPos == -1) {
                        addImage(sLine, null);
                    } else {
                        addImage(sLine.substring(0, iPos).trim(), sLine.substring(iPos + 1, sLine.length()).trim());
                    }
                }
                sLine = br.readLine();
            }
            br.close();
            isr.close();
            is.close();
        } catch (IOException e) {
            System.out.println("cannot read configuration from: " + mUrlConfigFile);
            System.out.println(e.getMessage());
        }
    }
