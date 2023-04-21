    protected void onRadioStatusChanged() {
        signal.setText("Signal: " + RadioInfo.getSignalLevel());
        if (doGPS.latitude < -90 || doGPS.latitude > 90 || doGPS.longitude < -180 || doGPS.longitude > 180 || doGPS.lastupdate < ((System.currentTimeMillis() / 1000) - 120) || !ServerCommunication.isConnectionAvailable()) {
            lf2.setText("Still waiting for lock...");
            return;
        }
        String string = "http://www.opencellid.org/measure/add";
        string += "?key=9c46be0fe0d6b163f75b22a4acbc6a50";
        string += "&userid=" + ociApp.getkey();
        string += "&lat=" + doGPS.latitude;
        string += "&lon=" + doGPS.longitude;
        string += "&signal=" + RadioInfo.getSignalLevel();
        long timenow = System.currentTimeMillis();
        string += "&measured_at=" + dateToString(timenow) + "%20" + timeToString(timenow);
        try {
            if (isNetworkType(RadioInfo.NETWORK_CDMA)) {
                string += "&mcc=" + Integer.toHexString(CDMAInfo.getCellInfo().getSID());
                string += "&mnc=" + Integer.toHexString(CDMAInfo.getCellInfo().getNID());
                string += "&lac=";
                string += "&cellid=" + CDMAInfo.getCellInfo().getBID();
                string += "&extraInfo=" + networktypes() + "-" + CDMAInfo.getChannelNumber();
                lf.setText("C=" + Integer.toHexString(CDMAInfo.getCellInfo().getSID()) + "," + Integer.toHexString(CDMAInfo.getCellInfo().getNID()) + "," + Integer.toHexString(CDMAInfo.getCellInfo().getBID()) + "," + "N/A" + ",S=" + RadioInfo.getSignalLevel() + ",T=" + networktypes() + ",C=" + CDMAInfo.getChannelNumber());
            } else if (isNetworkType(RadioInfo.NETWORK_GPRS)) {
                string += "&mcc=" + Integer.toHexString(GPRSInfo.getCellInfo().getMCC());
                string += "&mnc=" + RadioInfo.getMNC(RadioInfo.getCurrentNetworkIndex());
                string += "&lac=" + GPRSInfo.getCellInfo().getLAC();
                string += "&cellid=" + GPRSInfo.getCellInfo().getCellId();
                string += "&extraInfo=" + networktypes() + "-" + GPRSInfo.getCellInfo().getARFCN();
                lf.setText("C=" + Integer.toHexString(GPRSInfo.getCellInfo().getMCC()) + "," + Integer.toHexString(GPRSInfo.getCellInfo().getMCC()) + "," + GPRSInfo.getCellInfo().getCellId() + "," + GPRSInfo.getCellInfo().getLAC() + ",S=" + RadioInfo.getSignalLevel() + ",T=" + networktypes() + ",C=" + GPRSInfo.getCellInfo().getARFCN());
            }
        } catch (Exception e) {
        }
        update.setText(dateToString(timenow) + " " + timeToString(timenow));
        System.out.println("url: " + string);
        if (!DeviceInfo.isSimulator()) {
            String reply = ServerCommunication.http_get(string);
            lf2.setText(reply);
        } else {
            lf2.setText("Simulator Session");
        }
    }
