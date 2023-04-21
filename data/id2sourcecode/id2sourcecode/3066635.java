        @SuppressWarnings("unchecked")
        @Override
        public void execute(double x, double y, OTFDataWriter writer) {
            Collection<Class> readerClasses = connect.getEntries(writer.getClass());
            for (Class readerClass : readerClasses) {
                try {
                    Object reader = readerClass.newInstance();
                    client.put(x, y, (OTFDataReader) reader);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
