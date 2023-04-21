    public static void saveLocal(String uri, String localfile, BufferedWriter logWriter, int counter) {
        try {
            URL url = new URL(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
            BufferedWriter writer = new BufferedWriter(new FileWriter("onts/" + localfile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            logWriter.write("[" + counter + "]" + uri + " saved. ");
            logWriter.newLine();
            logWriter.flush();
            writer.flush();
            writer.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                logWriter.write("[" + counter + "]" + uri + " failed at saving: " + Utils.getExceptionTrace(e));
                logWriter.newLine();
                logWriter.flush();
            } catch (IOException ex) {
                System.err.println("Writing log failed. ");
                ex.printStackTrace();
            }
        }
    }
