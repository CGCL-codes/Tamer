    public void iButtonInserted(JibMultiEvent event) {
        String password = "";
        factory.setSlot(event.getSlotID());
        try {
            Selector appletSelector = new Selector(false);
            if (appletSelector.select(appletPath, appletName + ".jib", appletName, event.getChannel(), password)) {
            } else {
                int sw = appletSelector.getLastStatusWord();
                System.out.println("Applet load failed: " + Integer.toHexString(sw));
                System.out.println("Reason : " + decodeSW(sw));
            }
        } catch (CardTerminalException cte) {
            System.err.println("ERROR:  CardTerminalException occurred while communicating with iButton.");
        } catch (FileNotFoundException fnfe) {
            System.err.println("Unable to find file: " + appletName + ".jib");
        } catch (IOException ioe) {
            System.err.println("Unable to read file: " + appletName + ".jib");
        } catch (Exception e) {
            System.err.println("Exception in iButtonInserted:");
            e.printStackTrace();
        }
    }
