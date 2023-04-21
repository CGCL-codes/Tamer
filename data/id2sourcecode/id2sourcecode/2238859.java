            public ReadableByteChannel findStream(final Class<?> type, final String name) throws IOException {
                assert type != null : "Type cannot be null.";
                assert name != null : "Name cannot be null.";
                return new RandomAccessFile(new File(nameStrategy.getResourceName(type, name)), "r").getChannel();
            }
