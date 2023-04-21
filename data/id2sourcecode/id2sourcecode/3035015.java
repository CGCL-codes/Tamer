    public void doSubmit() {
        updateProgress("Encoding...", 10);
        StringBuffer buffer = new StringBuffer();
        buffer.append(_BUGS_URL);
        buffer.append("&func=postadd");
        buffer.append("&category_id=" + URLEncoder.encode(((StringCodeListItem) _CATEGORY.getSelectedItem()).CODE));
        buffer.append("&artifact_group_id=" + URLEncoder.encode(((StringCodeListItem) _GROUP.getSelectedItem()).CODE));
        buffer.append("&summary=" + URLEncoder.encode(_SUMMARY.getText().trim()));
        buffer.append("&details=" + URLEncoder.encode(_DETAILS.getText().trim()));
        buffer.append("&user_email=" + URLEncoder.encode(_EMAIL.getText().trim()));
        URLConnection connection = null;
        try {
            updateProgress("Connecting...", 30);
            URL submiturl = new URL(buffer.toString());
            connection = submiturl.openConnection();
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection http = (HttpURLConnection) connection;
                http.setRequestMethod("POST");
                http.setFollowRedirects(true);
                updateProgress("Sending...", 50);
                http.connect();
                int rcode = http.getResponseCode();
                String rmsg = http.getResponseMessage();
                updateProgress("Done...", 100);
                switch(rcode) {
                    case HttpURLConnection.HTTP_OK:
                        JOptionPane.showMessageDialog(this, "Your bug report was submit with no errors.\n" + rcode + ": " + rmsg, "Submit Successful", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case HttpURLConnection.HTTP_INTERNAL_ERROR:
                        JOptionPane.showMessageDialog(this, "The server is haing a problem at this time\nTry you submission later.\n" + rcode + ": " + rmsg, "Server Problem", JOptionPane.ERROR_MESSAGE);
                        break;
                    case HttpURLConnection.HTTP_FORBIDDEN:
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        JOptionPane.showMessageDialog(this, "The bug submission URL is our of date.\nYou may need to update your version of this software\n" + rcode + ": " + rmsg, "Bad URL", JOptionPane.ERROR_MESSAGE);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, rcode + ": " + rmsg, "Unknown Response", JOptionPane.WARNING_MESSAGE);
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Wrong Connection Type=" + connection.getClass().getName() + "\nPlease update the software...", "Out Of Date", JOptionPane.ERROR_MESSAGE);
            }
        } catch (MalformedURLException mfurle0) {
            JOptionPane.showMessageDialog(this, "The URL used to submit the report in invalid.\n" + mfurle0.getMessage(), "URL Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe0) {
            JOptionPane.showMessageDialog(this, "I/O Error.\n" + ioe0.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).disconnect();
            }
        }
        doExit();
    }
