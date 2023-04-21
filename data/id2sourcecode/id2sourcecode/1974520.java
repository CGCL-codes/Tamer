    public void actionPerformed(ActionEvent e) {
        if ("submit".equals(e.getActionCommand())) {
            try {
                String bugdescStr = bugdesc.getText();
                if (bugdescStr == null || bugdescStr.equals("")) {
                    JOptionPane.showMessageDialog(this, "Problem description not defined.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String data = URLEncoder.encode("ver", "UTF-8") + "=" + URLEncoder.encode(Double.toString(bugVer), "UTF-8");
                data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name.getText(), "UTF-8");
                data += "&" + URLEncoder.encode("os", "UTF-8") + "=" + URLEncoder.encode(os.getText(), "UTF-8");
                data += "&" + URLEncoder.encode("jre", "UTF-8") + "=" + URLEncoder.encode(jre.getText(), "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email.getText(), "UTF-8");
                data += "&" + URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode("X7pa4yL", "UTF-8");
                data += "&" + URLEncoder.encode("bugdesc", "UTF-8") + "=" + URLEncoder.encode(bugdescStr, "UTF-8");
                URL url = new URL("http://ubcdcreator.sourceforge.net/reportbug.php");
                URLConnection conn = url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                }
                rd.close();
                wr.close();
            } catch (Exception ex) {
            }
            setVisible(false);
        } else if ("cancel".equals(e.getActionCommand())) {
            setVisible(false);
        }
    }
