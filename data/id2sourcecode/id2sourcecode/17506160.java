    public PrintChannel getChannel(int channelNumber) throws MaverickException {
        if (channelNumber < 0 || (channelNumber == 0 && !printerEnabled)) {
            if (screen == null) {
                screen = new PrintChannel(this, getPseudoTerminal().getWriter(), true);
            }
            return screen;
        }
        if (outputs[channelNumber] == null) {
            String spooldir = "c:\\tmp";
            String filesep = System.getProperty("file.separator");
            String filename = spooldir + filesep + channelNumber;
            try {
                PrintWriter ps = new PrintWriter(new FileOutputStream(filename));
                outputs[channelNumber] = new PrintChannel(this, ps, false);
            } catch (FileNotFoundException fnfe) {
            }
        }
        return outputs[channelNumber];
    }
