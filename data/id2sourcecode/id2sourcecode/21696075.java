    public void exportToText(String filename) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(filename);
            Iterator<? extends PwsRecord> iter = getPwsFile().getRecords();
            CSVWriter csvWriter = new CSVWriter(fw, '\t');
            while (iter.hasNext()) {
                PwsRecord nextRecord = iter.next();
                List<String> nextEntry = new ArrayList<String>();
                if (nextRecord instanceof PwsRecordV1) {
                    nextEntry.add(V1_GROUP_PLACEHOLDER);
                    nextEntry.add(PwsEntryBean.getSafeValue(nextRecord, PwsFieldTypeV1.TITLE));
                    nextEntry.add(PwsEntryBean.getSafeValue(nextRecord, PwsFieldTypeV1.USERNAME));
                    nextEntry.add(PwsEntryBean.getSafeValue(nextRecord, PwsFieldTypeV1.PASSWORD));
                } else if (nextRecord instanceof PwsRecordV2) {
                    nextEntry.add(PwsEntryBean.getSafeValue(nextRecord, PwsFieldTypeV2.GROUP));
                    nextEntry.add(PwsEntryBean.getSafeValue(nextRecord, PwsFieldTypeV2.TITLE));
                    nextEntry.add(PwsEntryBean.getSafeValue(nextRecord, PwsFieldTypeV2.USERNAME));
                    nextEntry.add(PwsEntryBean.getSafeValue(nextRecord, PwsFieldTypeV2.PASSWORD));
                } else {
                    nextEntry.add(PwsEntryBean.getSafeValue(nextRecord, PwsFieldTypeV3.GROUP));
                    nextEntry.add(PwsEntryBean.getSafeValue(nextRecord, PwsFieldTypeV3.TITLE));
                    nextEntry.add(PwsEntryBean.getSafeValue(nextRecord, PwsFieldTypeV3.USERNAME));
                    nextEntry.add(PwsEntryBean.getSafeValue(nextRecord, PwsFieldTypeV3.PASSWORD));
                }
                String[] nextLine = nextEntry.toArray(new String[0]);
                csvWriter.writeNext(nextLine);
            }
        } catch (IOException ioe) {
            displayErrorDialog(Messages.getString("PasswordSafeJFace.ExportError.Title"), Messages.getString("PasswordSafeJFace.ExportError.Message"), ioe);
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    log.warn("Could not close output text file", e);
                }
            }
        }
    }
