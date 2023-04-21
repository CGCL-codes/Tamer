    private MockSMACollector newMockCollectorInstance() throws IOException, DecoderException {
        final List<byte[]> mockResponses = new ArrayList<byte[]>();
        ClassPathResource getChannelResponse = new ClassPathResource("sma-getchannellist-response.txt", SMASerialGenerationDataSourceTest.class);
        BufferedReader r = new BufferedReader(new InputStreamReader(getChannelResponse.getInputStream()));
        String line;
        while ((line = r.readLine()) != null) {
            if (line.length() < 1 || line.charAt(0) == '#') {
                continue;
            }
            byte[] data = Hex.decodeHex(line.toCharArray());
            mockResponses.add(data);
        }
        MockSMACollector collector = new MockSMACollector(mockResponses);
        return collector;
    }
