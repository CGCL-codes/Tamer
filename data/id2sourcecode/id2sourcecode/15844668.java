    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (this.txt.getText().length() == 0) return;
        String data;
        try {
            data = URLEncoder.encode("classe", "UTF-8") + "=" + URLEncoder.encode(this.txtClasse.getText(), "UTF-8");
            data += "&" + URLEncoder.encode("testo", "UTF-8") + "=" + URLEncoder.encode(this.txt.getText(), "UTF-8");
            URL url;
            String u = Main.config.getRemoteRoot() + "/bug.asp?" + data;
            if (!u.startsWith("http://")) u = "http://" + u;
            url = new URL(u);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String str = "";
            while ((line = rd.readLine()) != null) {
                str += line;
            }
            rd.close();
            new Info("Invio eseguito con successo!").startModal(this);
            doDefaultCloseAction();
        } catch (Exception ex) {
            Utils.showException(ex, "Errore in upload", this);
        }
    }
