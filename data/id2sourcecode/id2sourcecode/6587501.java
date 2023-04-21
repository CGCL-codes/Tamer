    public File findPlatformFile(File seriesMatrixFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(seriesMatrixFile));
            String currentLine = "";
            String platformName = "";
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.contains("!Series_platform_id")) {
                    platformName = currentLine.substring(currentLine.indexOf("G"), currentLine.lastIndexOf("\""));
                    break;
                }
            }
            platformFile = new File("experiments/" + platformName);
            if (platformFile.exists() == false) {
                String URLname = "http://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?targ=self&form=text&view=data&acc=" + platformName;
                URL url = new URL(URLname);
                URLConnection con = url.openConnection();
                BufferedReader reader;
                BufferedWriter writer = new BufferedWriter(new FileWriter(platformFile));
                do {
                    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } while (reader == null);
                String currentRead = "";
                while ((currentRead = reader.readLine()) != null) {
                    writer.write(currentRead + "\n");
                }
                reader.close();
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialog.openWarning(main.getShell(), "Download failed!", "ChiBE could not download the file. Check your parameters.");
        }
        return platformFile;
    }
