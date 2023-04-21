        @SuppressWarnings("unchecked")
        public void execute(double x, double y, OTFDataWriter writer) {
            Collection<Class<?>> readerClasses = this.connect.getToEntries(writer.getClass());
            for (Class readerClass : readerClasses) {
                try {
                    OTFDataReader reader = (OTFDataReader) readerClass.newInstance();
                    reader.setSrc(writer);
                    this.client.put(x, y, reader);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }
        }
