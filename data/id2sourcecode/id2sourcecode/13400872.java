        @Override
        public synchronized void iHave() {
            String message = "";
            int deviceCount = 0;
            for (Iterator<DeviceContainer> i = switchSet.iterator(); i.hasNext(); ) {
                DeviceContainer dc = i.next();
                message += getDeviceSignature(dc);
                deviceCount += getChannelCount(dc.getAddress());
                if (i.hasNext()) {
                    message += " ";
                }
            }
            send("IHAVE " + deviceCount + ": " + message);
        }
