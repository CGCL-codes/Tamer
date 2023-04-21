    public boolean crawl(String urlstring, String folder, String appendname) {
        s = null;
        URL url = null;
        HttpURLConnection uconn = null;
        BufferedReader bin = null;
        InputStreamReader isr = null;
        try {
            int k = 1;
            while (true) {
                if (k > 3) return false;
                System.out.println("connecting to " + urlstring + "     try  " + k);
                edu.fudan.cse.medlab.event.extraction.DB.addLog("connecting to " + urlstring + "     try  " + k);
                url = new URL(urlstring);
                uconn = (HttpURLConnection) url.openConnection();
                uconn.setConnectTimeout(30 * 1000);
                uconn.setReadTimeout(10 * 60 * 1000);
                if (uconn.getHeaderField(0) == null || !uconn.getHeaderField(0).startsWith("HTTP/1.1 200 OK")) {
                    k++;
                    Thread.sleep(1000);
                    continue;
                }
                isr = new InputStreamReader(uconn.getInputStream());
                if (isr == null) {
                    k++;
                    Thread.sleep(1000);
                    continue;
                } else break;
            }
            bin = new BufferedReader(isr);
            String localurl = urlstring.substring(urlstring.indexOf("//") + 2);
            localurl = localurl.replace("/", "\\");
            localurl = localurl.replace("?", "@");
            if (appendname == null) {
                while (localurl.endsWith("\\")) localurl = localurl.substring(0, localurl.length() - 1);
            }
            String filepath = folder + localurl;
            if (appendname != null) filepath += "\\" + appendname;
            if (filepath.endsWith("\\index.php")) filepath = filepath.replace("\\index.php", "\\indexpage");
            File f = new File(filepath);
            File folderfile = new File(f.getParent());
            if (folderfile.exists() && folderfile.isFile()) {
                folderfile.renameTo(new File(f.getParent() + "(index)"));
            }
            if (f.exists() && f.isDirectory()) {
                filepath += "(index)";
            }
            if (!f.exists()) {
                f.getParentFile().mkdirs();
            }
            BufferedInputStream in = new BufferedInputStream(uconn.getInputStream());
            int ks = 1;
            FileOutputStream out = new FileOutputStream(filepath);
            byte[] buf = new byte[1024];
            int c;
            s = "";
            while ((c = in.read(buf)) != -1) {
                out.write(buf, 0, c);
                s += new String(buf, 0, c);
            }
            System.out.println("downloading  " + urlstring + "     as  " + filepath);
            edu.fudan.cse.medlab.event.extraction.DB.addLog("downloading  " + urlstring + "     as  " + filepath);
            bin.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
