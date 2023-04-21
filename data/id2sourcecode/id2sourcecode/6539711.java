    @Override
    protected void saveRecord(PwsFile file) throws IOException {
        LOG.debug2("----- START OF RECORD -----");
        for (Iterator<Integer> iter = getFields(); iter.hasNext(); ) {
            int type;
            PwsField value;
            type = iter.next().intValue();
            value = getField(type);
            if (LOG.isDebug2Enabled()) LOG.debug2("Writing field " + type + " (" + ((Object[]) VALID_TYPES[type])[1] + ") : \"" + value.toString() + "\"");
            writeField(file, value);
            PwsFileV3 fileV3 = (PwsFileV3) file;
            fileV3.hasher.digest(value.getBytes());
        }
        writeField(file, new PwsStringField(END_OF_RECORD, ""));
        LOG.debug2("----- END OF RECORD -----");
    }
