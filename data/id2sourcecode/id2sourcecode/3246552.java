    public boolean writeIt() {
        try {
            path += filename;
            StringReader SR = new StringReader(data);
            File outputFile = new File(path);
            FileWriter out = new FileWriter(outputFile);
            int c;
            if (outputFile.length() > 0) {
                return false;
            }
            while ((c = SR.read()) != -1) out.write(c);
            out.flush();
            out.close();
        } catch (IOException e) {
            Logger.logInfo("JFileWriter IO Error : " + e.toString());
        }
        return true;
    }
