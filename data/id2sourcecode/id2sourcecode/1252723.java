    protected void getConfiguration() throws Exception {
        URL url = this.getClass().getResource("/physical_readers.properties");
        URLConnection site = url.openConnection();
        InputStream is = site.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bufferReader;
        bufferReader = new BufferedReader(isr);
        HashMap<String, String[]> physicalReaderHash = new HashMap<String, String[]>();
        String line = null;
        try {
            while ((line = bufferReader.readLine()) != null) {
                if (line.startsWith("#")) continue;
                log.debug("ALE physicalReader attributes = " + line);
                StringTokenizer sTokenizer = new StringTokenizer(line);
                int i = 0;
                String readerComponentId = "";
                String[] pReaderAttributes = new String[rcAttributes.values().length];
                while (sTokenizer.hasMoreTokens()) {
                    String token = sTokenizer.nextToken();
                    if (i == 0) readerComponentId = token; else pReaderAttributes[i - 1] = token;
                    i++;
                }
                physicalReaderHash.put(readerComponentId, pReaderAttributes);
                ReaderComponent readerComponent = new ReaderComponent(readerComponentId);
                readerComponent.setName(pReaderAttributes[rcAttributes.readerComponentName.ordinal()]);
                readerComponent.setIpAddress(pReaderAttributes[rcAttributes.interrogatorIPAddress.ordinal()]);
                readerComponent.setPort(pReaderAttributes[rcAttributes.interrogatorPort.ordinal()]);
                readerComponent.setInterrogatorClass(Class.forName(pReaderAttributes[rcAttributes.interrogatorDriver.ordinal()]));
                ArrayList<Sensor> sensors = new ArrayList<Sensor>();
                sensors.add(new Sensor(pReaderAttributes[rcAttributes.Sensor.ordinal()]));
                readerComponent.setSensors(sensors);
                IInterrogatorIO interrogator = InterrogatorFactory.create(readerComponent);
                Reader reader = new Reader(pReaderAttributes[rcAttributes.Reader.ordinal()]);
                reader.setSensors(sensors);
                readers.add(reader);
                interrogators.add(interrogator);
                interrogator.setDeviceManager(this);
            }
        } catch (IOException e) {
            log.error("unable to read properties stream", e);
            throw new Exception("unable to read properties stream");
        }
    }
