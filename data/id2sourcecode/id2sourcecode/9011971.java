    public void save(GPSMessage msg) {
        if (debug && current != null) {
            System.out.println("> *" + current.toString());
            System.out.println("  ID=" + getVehicle());
            System.out.println(" ORG=" + getOrganisation());
            if (msg.hasGPS()) System.out.println("   Has GPS: " + msg.getGPSData());
        }
        if (msg.hasGPS()) {
            new Thread(new Runnable() {

                public void run() {
                    try {
                        saveIntoDB("" + getOrganisation(), current.getGPSData(), getVehicle());
                        if (!inCanada) {
                            URL url = new URL(bundle.getString("httpSaver") + "?id=" + getVehicle() + "&gps=" + current.getGPSData() + "&org=" + getOrganisation());
                            if (debug) System.out.println(" Data saved: " + StreamPreparator.stream2String(url.openStream()));
                        }
                    } catch (Exception ex) {
                        System.out.println("Exception on URL saving: " + ex);
                    }
                }
            }).start();
        }
    }
