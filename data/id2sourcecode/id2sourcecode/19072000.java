    public ImportFromInternet() {
        File f = new File(KTH.data.getHomeDir() + File.separator + KTH.data.katalogName);
        System.out.println(f + (f.exists() ? " is found " : " is missing "));
        try {
            URL url = new URL(KTH.data.updateURL);
            KTH.out.pl("Opening connection to " + KTH.data.updateURL + "...", 0);
            URLConnection urlC = url.openConnection();
            InputStream is = url.openStream();
            KTH.out.p("Quelleninfos: (Typ: " + urlC.getContentType(), 0);
            Date date = new Date(urlC.getLastModified());
            KTH.out.pl(", Letzte �nderung: " + date.toLocaleString() + ")", 0);
            System.out.flush();
            Date fDate = null;
            long longdate = urlC.getLastModified();
            long filedate = f.lastModified();
            if (f.exists()) {
                KTH.out.pl(f.lastModified(), 0);
                KTH.out.pl(urlC.getLastModified(), 0);
            }
            if (f.exists() && longdate < filedate) {
            } else {
                FileOutputStream fos = null;
                String fileName = null;
                StringTokenizer st = new StringTokenizer(url.getFile(), "/");
                while (st.hasMoreTokens()) fileName = st.nextToken();
                if (fileName.equals("katalog.xml")) {
                    fos = new FileOutputStream(KTH.data.getHomeDir() + File.separator + KTH.data.katalogName);
                    int oneChar, count = 0;
                    while ((oneChar = is.read()) != -1) {
                        fos.write(oneChar);
                        count++;
                    }
                    is.close();
                    fos.close();
                    KTH.out.pl(count + " Byte kopiert", 0);
                    new JDialog();
                } else {
                    System.out.println("Quellenfehler");
                }
            }
        } catch (MalformedURLException e) {
            KTH.out.err(e.toString());
        } catch (IOException e) {
            KTH.out.err(e.toString());
        }
        KTH.dc2.removeDc("Import");
    }
