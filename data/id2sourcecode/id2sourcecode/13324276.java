    public boolean saveFile(String sfileName) {
        try {
            FileOutputStream output = new FileOutputStream(sfileName);
            ObjectOutputStream outStream = new ObjectOutputStream(output);
            outStream.writeObject("FILENAME: " + sfileName);
            outStream.writeObject(this.readInX10Devices());
            outStream.writeObject(this.readInX10Events());
            outStream.writeObject(this.loadMacroTriggers());
            outStream.writeObject(this.readInX10Macros());
            outStream.writeObject(this.readInAliceMonitors());
            outStream.flush();
            outStream.close();
        } catch (IOException err) {
            System.err.println("Unable to write file :" + err);
            return false;
        }
        return true;
    }
