    private boolean postFile(File f) {
        try {
            if ((!EveTrader.isRemoteActive()) || sent.contains(f.getName())) {
                return false;
            }
            String urlString = EveTrader.getRemoteURL();
            data = null;
            URL url = new URL(urlString);
            URLConnection conn;
            addParam("filename", f.getName());
            addParam("modified", "" + f.lastModified());
            addParam("version", TradeFinder.version);
            addParam("time", "" + System.currentTimeMillis());
            if (TradeFinder.playerName != null) {
                addParam("player", TradeFinder.playerName);
            } else {
                addParam("player", "Unknown");
            }
            addParam("postfile", "");
            conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data.toString());
            FileReader fr = new FileReader(f);
            BufferedReader in = new BufferedReader(fr);
            String str;
            int idx = 0;
            while ((str = in.readLine()) != null) {
                if (idx != 0) {
                    wr.write(str + '\n');
                }
                idx++;
            }
            wr.flush();
            sent.add(f.getName());
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                }
                rd.close();
            } catch (Exception ex) {
            }
            wr.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
