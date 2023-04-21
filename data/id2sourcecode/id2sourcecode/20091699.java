    protected void launchInEditMode() {
        HttpURL url = new HttpURL(mCurrentNodeUrl);
        try {
            java.net.URL urlServlet = new java.net.URL(mServerUrl + "/echo");
            java.net.URLConnection con = urlServlet.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-java-serialized-object");
            if (browserCookie != null) con.setRequestProperty("Cookie", getParameter("browserCookie"));
            java.io.OutputStream outstream = con.getOutputStream();
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(outstream);
            oos.writeObject(additionToEdit);
            oos.flush();
            oos.close();
            java.io.InputStream in = con.getInputStream();
            java.io.ObjectInputStream inputFromServlet = new java.io.ObjectInputStream(in);
            try {
                currentAddition = (NoteAddition) inputFromServlet.readObject();
                System.out.println("Response:" + currentAddition);
            } catch (Exception e) {
                System.out.println(e);
            }
            inputFromServlet.close();
            in.close();
        } catch (java.net.MalformedURLException e) {
            System.out.println("ex" + e);
        } catch (java.io.IOException e2) {
            System.out.println("ex2" + e2);
        }
        try {
            String datatype = (String) currentAddition.getProp("datatype");
            NObNode contentNode = extractContent(currentAddition);
            boolean readonly = false;
            mEditorPanel.launchWithNOb(contentNode, readonly);
            mEditorPanel.setEnabled(false);
        } catch (Exception e) {
            System.err.println("Failed to launch editor on node: " + mCurrentNodeUrl);
            e.printStackTrace();
        }
    }
