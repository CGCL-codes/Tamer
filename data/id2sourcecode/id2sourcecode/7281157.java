    public void save() {
        try {
            RecordStore recordStore = RecordStore.openRecordStore("MapMidlet Options", true);
            deleteRecords(recordStore);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            DataOutputStream outputStream = new DataOutputStream(os);
            outputStream.writeBoolean(useFileApi);
            outputStream.writeUTF(rootName);
            outputStream.writeUTF(tileFactoryName);
            outputStream.writeBoolean(debugMode);
            outputStream.writeUTF(gpsUrl);
            outputStream.writeBoolean(gpsEnabled);
            outputStream.writeBoolean(fadeEffect);
            outputStream.writeBoolean(fileReadInSeparateThread);
            outputStream.writeInt(maxRetries);
            outputStream.writeBoolean(onlineMode);
            center = CloudGps.getTileCanvas().getCurrentCenter();
            outputStream.writeDouble(center.latitude);
            outputStream.writeDouble(center.longitude);
            outputStream.writeInt(zoom);
            outputStream.writeUTF(skin.path);
            outputStream.writeInt(routeEndIndex);
            for (int i = 0; i < 2; i++) {
                ScreenMarker routeEnd = (ScreenMarker) routeEnds.elementAt(i);
                outputStream.writeDouble(routeEnd.worldCoordinate.latitude);
                outputStream.writeDouble(routeEnd.worldCoordinate.longitude);
                outputStream.writeBoolean(routeEnd.visible);
            }
            outputStream.writeUTF(routeType);
            byte[] byteArray = os.toByteArray();
            recordStore.addRecord(byteArray, 0, byteArray.length);
            recordStore.closeRecordStore();
        } catch (Exception e) {
            CloudGps.setError(e);
        }
    }
