    public static List<v3LinkReachableMacsExtension> getRotameterInformation(int numberOfRotameterObservations) {
        List<v3LinkReachableMacsExtension> resultList = new ArrayList<v3LinkReachableMacsExtension>();
        synchronized (RotameterObservationTable) {
            if (RotameterObservationTable.isEmpty()) {
                System.out.println("NetworkMonitor:getRotameterInformation -> There are not available RotameterObservation(s) for any segment.");
                return null;
            }
            Enumeration<String> keys = RotameterObservationTable.keys();
            while (keys.hasMoreElements()) {
                String qs = keys.nextElement();
                List<v3RotameterObservation> elem = RotameterObservationTable.get(qs);
                if (elem != null) {
                    if (elem.size() < numberOfRotameterObservations) {
                        System.out.println("NetworkMonitor:getRotameterInformation -> " + numberOfRotameterObservations + " are not available for Segment: " + qs);
                        return null;
                    }
                    v3LinkReachableMacsExtension lrm = new v3LinkReachableMacsExtension();
                    lrm = new v3LinkReachableMacsExtension();
                    lrm.BridgeId = "?";
                    lrm.LinkId = "?";
                    PcapIf pcapif = GetInterfaceFromSegmentId(qs);
                    try {
                        lrm.MacAddress = NetworkUtil.getMacAddressNotFormatted(pcapif.getHardwareAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lrm.QosSegmentId = qs;
                    resultList.add(lrm);
                    for (int i = 0; i < numberOfRotameterObservations; i++) {
                        lrm.RotameterObservation.add(elem.get(i));
                    }
                }
            }
        }
        return resultList;
    }
