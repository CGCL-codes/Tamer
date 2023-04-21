    public static String encodeGearsRequest(LocationRequest lrq) {
        String rqs = "{ \"version\" : \"1.1.0\", \"host\" : \"_null_.localdomain\", \"request_address\" : true ";
        if (lrq.getMeasurements() != null) {
            Measurement[] meas = lrq.getMeasurements();
            boolean gotWifi = false;
            for (int i = 0; i < meas.length && !gotWifi; ++i) gotWifi = (meas[i] instanceof WifiMeasurement);
            if (gotWifi) {
                rqs += ", \"wifi_towers\" : [ ";
                int wifiCount = 0;
                for (int i = 0; i < meas.length; ++i) {
                    if (!(meas[i] instanceof WifiMeasurement)) continue;
                    WifiMeasurement wfm = (WifiMeasurement) meas[i];
                    if (wifiCount > 0) rqs += ", ";
                    wifiCount++;
                    rqs += "{";
                    if (wfm.getBssid() != null) {
                        rqs += "\"mac_address\" : \"" + wfm.getBssid() + "\", ";
                    }
                    if (wfm.getChannel() != -1) {
                        rqs += "\"channel\" : \"" + wfm.getChannel() + "\", ";
                    }
                    if (wfm.getSsid() != null) {
                        rqs += "\"ssid\" : \"" + wfm.getSsid() + "\", ";
                    }
                    if (wfm.getRssi() != -1) {
                        rqs += "\"signal_strength\" : \"" + wfm.getRssi() + "\", ";
                    }
                    if (wfm.getSnr() != -1) {
                        rqs += "\"signal_to_noise\" : \"" + wfm.getSnr() + "\", ";
                    }
                    rqs += "\"foo\": \"bar\"";
                    rqs += "}";
                }
                rqs += "] }";
            } else {
                rqs += "}";
            }
        } else {
            rqs += "}";
        }
        System.err.println(rqs);
        return rqs;
    }
