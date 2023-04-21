    @Override
    protected void importRecords(InputStream stream) throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        final byte[] bytes = new byte[1024];
        zip = new ZipInputStream(stream);
        while (zip.getNextEntry() != null) {
            int count;
            buffer.reset();
            while ((count = zip.read(bytes, 0, 1024)) != -1) buffer.write(bytes, 0, count);
            final DataInput in = new DataInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            final Record[] records = RecordSelection.read(in, null);
            for (int i = 0, imax = records.length; i < imax; ++i) SwingUtilities.invokeLater(new RvConnection.AddRecordTask(records[i]));
        }
    }
