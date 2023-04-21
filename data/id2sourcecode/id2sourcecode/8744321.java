    protected void run() {
        String data = null;
        for (int i = 0; i < regEntry.getNumRegKeys(); i++) {
            String key = regEntry.getRegKey(i);
            String val = regEntry.getRegValue(i);
            try {
                if (data == null) {
                    data = "";
                } else {
                    data += "&";
                }
                data += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(val, "UTF-8");
            } catch (Exception e) {
                loadProgress.println("Error formatting data, could not complete registration");
                loadProgress.println("key = " + key + ", value = " + val);
                loadProgress.println("exception " + e);
                loadProgress.println("if problems persist please alert java treeview maintainers (jtreeview-users@lists.sourceforge.net");
                setException(e);
                setFinished(true);
                return;
            }
        }
        URL url;
        try {
            url = new URL(regUrl);
        } catch (Exception e) {
            loadProgress.println("Error constructing URL, could not complete registration");
            loadProgress.println("url = " + regUrl);
            loadProgress.println("exception " + e);
            loadProgress.println("if problems persist please alert java treeview maintainers (jtreeview-users@lists.sourceforge.net");
            setException(e);
            setFinished(true);
            return;
        }
        URLConnection conn;
        try {
            conn = url.openConnection();
        } catch (Exception e) {
            loadProgress.println("Error opening connection, could not complete registration");
            loadProgress.println("exception " + e);
            loadProgress.println("please check network connection");
            loadProgress.println("if problems persist please alert java treeview maintainers (jtreeview-users@lists.sourceforge.net");
            setException(e);
            setFinished(true);
            return;
        }
        OutputStreamWriter wr;
        try {
            conn.setDoOutput(true);
            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
        } catch (Exception e) {
            loadProgress.println("Error sending data, could not complete registration");
            loadProgress.println("exception " + e);
            loadProgress.println("please check network connection");
            loadProgress.println("if problems persist please alert java treeview maintainers (jtreeview-users@lists.sourceforge.net");
            setException(e);
            setFinished(true);
            return;
        }
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                if (line.indexOf("FAILED") >= 0) {
                    loadProgress.println(line);
                    setComplete(false);
                } else if (line.indexOf("SUCCEEDED") >= 0) {
                    setComplete(true);
                } else {
                    loadProgress.println(line);
                }
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            loadProgress.println("Error reading response, could not complete registration");
            loadProgress.println("exception " + e);
            loadProgress.println("if problems persist please alert java treeview maintainers (jtreeview-users@lists.sourceforge.net");
            setException(e);
            setFinished(true);
            return;
        }
        if (isComplete()) {
            loadProgress.setButtonText("Close");
            loadProgress.clear();
            loadProgress.setValue(100);
            loadProgress.println("Registration Succeeded!!!\n\n");
            loadProgress.println("Thank you for your support.");
            regEntry.setStatus("complete");
        } else {
            loadProgress.setButtonText("Close");
            loadProgress.println("Registration failed. Must retry later");
        }
    }
