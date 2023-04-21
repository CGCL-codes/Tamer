    private void sendReport() {
        try {
            String data = URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(report.getText(), "UTF-8");
            URL url = new URL("http://jimcat.org/reportbug.php");
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            wr.close();
            SwingClient.getInstance().showMessage(sb.toString(), "Error report", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            SwingClient.getInstance().showMessage("There was a problem sending your report. Do you have internet connectivity?", "Error report", JOptionPane.ERROR_MESSAGE);
        }
        setVisible(false);
    }
