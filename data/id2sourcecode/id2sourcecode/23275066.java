    private static void putSegments() throws Exception {
        ContentName segment;
        ContentObject object;
        long lastMarkerTest = 1;
        bytesWritten = 0;
        byte[] toWrite;
        for (int i = 0; i < segments; i++) {
            if (i > 0) lastMarkerTest = segments - 1;
            segment = SegmentationProfile.segmentName(testName, i);
            toWrite = ("this is segment " + i + " of " + segments).getBytes();
            bytesWritten = bytesWritten + toWrite.length;
            object = ContentObject.buildContentObject(segment, toWrite, null, null, SegmentationProfile.getSegmentNumberNameComponent(lastMarkerTest));
            if (i == 0) {
                firstDigest = object.digest();
            }
            writeHandle.put(object);
        }
        System.out.println("wrote " + bytesWritten + " bytes");
    }
