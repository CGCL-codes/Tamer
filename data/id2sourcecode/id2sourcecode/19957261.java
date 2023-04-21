    protected void sendReport(boolean error) {
        String report;
        if (error) {
            report = "ERROR REPORT\n" + message + "\n\n";
            if (askName) {
                report += "USER: " + nameTextField.getText() + "\n";
                report += "EMAIL: " + emailTextField.getText() + "\n\n";
            }
            report += os + "\n";
            report += java + "\n";
            if (release != null) report += release + "\n";
            report += "\n";
            report += "DESCRIPTION:\n" + descriptionTextArea.getText() + "\n\n\n";
            report += "STACK TRACE:\n" + exception + "\n";
        } else {
            report = "USER COMMENTS\n\n";
            report += "USER: " + nameTextField.getText() + "\n";
            report += "EMAIL: " + emailTextField.getText() + "\n\n";
            report += os + "\n";
            report += java + "\n";
            if (release != null) report += release + "\n";
            report += "\n";
            report += "COMMENTS AND SUGGESTIONS:\n" + descriptionTextArea.getText() + "\n";
        }
        try {
            URL url = new URL("http://backend-ea.e-ucm.es/reports.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setAllowUserInteraction(true);
            HttpURLConnection.setFollowRedirects(true);
            con.setInstanceFollowRedirects(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            String content = "type=" + (error ? "bug" : "comment") + "&version=" + release + "&file=" + report;
            out.writeBytes(content);
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            in.readLine();
        } catch (Exception e) {
        }
    }
