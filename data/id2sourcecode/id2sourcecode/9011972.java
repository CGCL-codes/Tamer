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
