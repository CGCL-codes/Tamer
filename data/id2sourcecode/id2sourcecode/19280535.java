        @Override
        public void run() {
            if (Helper.getXmlHandler().getCheckUpdates()) {
                BufferedReader br;
                InputStreamReader isr;
                try {
                    isr = new InputStreamReader(CheckForUpdates.class.getClassLoader().getResource("Resources/update.upd").openStream());
                    br = new BufferedReader(isr);
                    String currentVersion = br.readLine();
                    URL url = new URL("http://radis.sf.net/update.upd");
                    isr = new InputStreamReader(url.openStream());
                    br = new BufferedReader(isr);
                    String serverVersion = br.readLine();
                    if (Integer.parseInt(currentVersion) < Integer.parseInt(serverVersion)) showDialog();
                } catch (IOException e) {
                    Helper.log().warn("Update check failed. Exception: ", e);
                } catch (Exception e) {
                    Helper.log().error("Update check failed in an unexpected way. Exception: ", e);
                }
            }
        }
