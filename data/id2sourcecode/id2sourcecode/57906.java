    public void submitBug(String mail, String user, String inform, StringBuffer trace) {
        URLConnection urlConn = null;
        URL url;
        OutputStreamWriter printout;
        InputStreamReader input;
        BufferedReader reader;
        modal = false;
        try {
            url = new URL("http://interna.sourceforge.net/headers.php");
            urlConn = url.openConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        try {
            printout = new OutputStreamWriter(urlConn.getOutputStream());
            String content = "javaversion=" + URLEncoder.encode(System.getProperty("java.version"), "iso-8859-1") + "&country=" + URLEncoder.encode(System.getProperty("user.country"), "iso-8859-1") + "&os=" + URLEncoder.encode(System.getProperty("os.name") + " " + System.getProperty("os.arch") + " " + System.getProperty("os.version"), "iso-8859-1") + "&vendor=" + URLEncoder.encode(System.getProperty("java.vendor"), "iso-8859-1") + "&pversion=" + URLEncoder.encode(CustAppl.VERSION, "iso-8859-1") + "&report=" + URLEncoder.encode(inform, "iso-8859-1") + "&trace=" + URLEncoder.encode(" ( nicht angezeigt ) ", "iso-8859-1") + "&mail=" + URLEncoder.encode(mail, "iso-8859-1") + "&name=" + URLEncoder.encode(user, "iso-8859-1");
            if (JOptionPane.showConfirmDialog(null, "Diese Daten werden zusätzlich zur\ndetailierten Fehlerbeschreibung übermittelt:\n\n" + URLDecoder.decode(content, "iso-8859-1").replaceAll("&", "\n"), "Abschicken?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                content.replaceFirst("(nicht angezeigt)", URLEncoder.encode(trace.toString(), "iso-8859-1"));
                String base64content = "c=" + Base64.base64Encode(content);
                printout.write(base64content.toCharArray());
                printout.flush();
                printout.close();
                input = new InputStreamReader(urlConn.getInputStream());
                String string;
                reader = new BufferedReader(input);
                while (null != ((string = reader.readLine()))) {
                    System.err.println(string);
                    if (string.equals("OK")) JOptionPane.showMessageDialog(null, "Die Übertragung war erfolgreich!\nVielen Dank für ihre Hilfe.", "Information", JOptionPane.INFORMATION_MESSAGE); else {
                        JOptionPane.showMessageDialog(null, "Übertragungsfehler!", "Fehler", JOptionPane.ERROR_MESSAGE);
                    }
                }
                resetAll();
                input.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
