    private String getServletData(String full_url) {
        try {
            URL url = new URL(full_url);
            log.debug("Servlet::URL (" + full_url + ")");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String iLine, iLine2;
            iLine2 = "";
            while ((iLine = in.readLine()) != null) {
                iLine2 += iLine;
            }
            in.close();
            return iLine2;
        } catch (Exception ex) {
            log.error("Servlet::URL (" + full_url + ")");
            log.error("Error contacting servlet: " + ex, ex);
            this.status_label.setText(ic.getMessage("UPD_ERROR_CONTACTING_SERVER"));
            m_da.showDialog(this, ATDataAccessAbstract.DIALOG_ERROR, ic.getMessage("UPD_ERROR_CONTACTING_SERVER"));
            return null;
        }
    }
