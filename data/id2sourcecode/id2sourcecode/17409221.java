    private void saveFile(String urlID) {
        URL url = (URL) remoteUrlList.get(urlID);
        try {
            URLConnection urlConnection = url.openConnection();
            DataOutputStream out;
            BufferedReader in;
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            out = new DataOutputStream(urlConnection.getOutputStream());
            String content = (String) remoteUrlList.get(urlID + ".params");
            content = (content == null ? "" : content + "&") + "content=" + URLEncoder.encode(textArea.getText());
            if (debug > 0) System.err.println("Capture: " + content);
            out.writeBytes(content);
            out.flush();
            out.close();
            if (debug > 0) System.err.println("Capture: reading response");
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String str;
            while (null != ((str = in.readLine()))) {
                System.out.println("Capture: " + str);
            }
            in.close();
            doneDialog.pack();
            doneDialog.setVisible(true);
        } catch (IOException ioe) {
            System.err.println("Capture: cannot store text on remote server: " + url);
            ioe.printStackTrace();
            JTextArea errorMsg = new JTextArea(ioe.toString(), 5, 30);
            errorMsg.setEditable(false);
            errorDialog.add(errorMsg, BorderLayout.CENTER);
            errorDialog.pack();
            errorDialog.setVisible(true);
        }
        if (debug > 0) System.err.println("Capture: storage complete: " + url);
    }
