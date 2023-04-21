    public void submit(String uri, String user) throws java.io.IOException, Exception {
        String n3 = myModel.getRuleExpr().toN3(myRule);
        String label = myModel.getRuleExpr().getLabel(myRule);
        String predicate = myModel.getRuleExpr().getPredicate(myRule);
        SubmissionProfile profile = (SubmissionProfile) destinationCombo.getSelectedItem();
        for (Iterator iter = checkBoxes.iterator(); iter.hasNext(); ) {
            JCheckBox box = (JCheckBox) iter.next();
            if (box.getModel().isSelected()) {
                String predObjAssertion = (String) profile.optionToAsserts.get(box.getText());
                n3 = n3 + "\n<" + predicate + "> " + predObjAssertion + " .";
            }
        }
        String encodedrdf = URLEncoder.encode(n3, "UTF-8");
        try {
            URL url = new URL(uri);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.println("n3blob=" + encodedrdf);
            out.println("&inputtype=1");
            out.println("&op=Submit");
            out.println("&label=" + label);
            out.println("&creator=" + user);
            out.flush();
            out.close();
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String output = "";
            String line;
            while ((line = in.readLine()) != null) {
                output += line;
                if (DEBUG) System.err.println(line);
            }
        } catch (Exception e) {
            System.out.println("ERROR!");
            System.out.println(e.getMessage());
        }
    }
