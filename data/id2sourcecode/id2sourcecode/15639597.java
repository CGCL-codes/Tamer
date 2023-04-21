    public boolean checkForUpdate() {
        String installedVersion = MainFrame.s_version;
        String availableVersion = new String();
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://ggc.sourceforge.net/LATEST_VERSION.txt");
            conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                availableVersion = nextLine();
                if (availableVersion == null) conn.disconnect();
                String line = new String();
                StringBuffer sb = new StringBuffer();
                while ((line = nextLine()) != null) sb.append(line + "\n");
                String message = null;
                if (isNewVersion(installedVersion, availableVersion)) message = m_ic.getMessage("YOUR_VERSION") + ":\t\t" + installedVersion + "\n" + m_ic.getMessage("AVAILABLE_VERSION") + ":\t" + availableVersion + "\n\n" + sb.toString(); else message = m_ic.getMessage("NO_NEW_VERSION");
                JOptionPane.showMessageDialog(null, message, m_ic.getMessage("CHECK_FOR_NEW_VERSION"), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, m_ic.getMessage("HOST_UNAVAILABLE_MSG"), m_ic.getMessage("HOST_UNAVAILABLE_INFO"), JOptionPane.ERROR_MESSAGE);
        } catch (java.io.IOException e) {
            System.err.println(e);
        } finally {
            if (conn != null) conn.disconnect();
        }
        return true;
    }
