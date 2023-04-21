    @Override()
    public ResultCode doToolProcessing() {
        final Schema schema;
        if (schemaDirectory.isPresent()) {
            final File schemaDir = schemaDirectory.getValue();
            try {
                final TreeMap<String, File> fileMap = new TreeMap<String, File>();
                for (final File f : schemaDir.listFiles()) {
                    final String name = f.getName();
                    if (f.isFile() && name.endsWith(".ldif")) {
                        fileMap.put(name, f);
                    }
                }
                if (fileMap.isEmpty()) {
                    err("No LDIF files found in directory " + schemaDir.getAbsolutePath());
                    return ResultCode.PARAM_ERROR;
                }
                final ArrayList<File> fileList = new ArrayList<File>(fileMap.values());
                schema = Schema.getSchema(fileList);
            } catch (Exception e) {
                err("Unable to read schema from files in directory " + schemaDir.getAbsolutePath() + ":  " + getExceptionMessage(e));
                return ResultCode.LOCAL_ERROR;
            }
        } else {
            try {
                final LDAPConnection connection = getConnection();
                schema = connection.getSchema();
                connection.close();
            } catch (LDAPException le) {
                err("Unable to connect to the directory server and read the schema:  ", le.getMessage());
                return le.getResultCode();
            }
        }
        entryValidator = new EntryValidator(schema);
        entryValidator.setCheckAttributeSyntax(!ignoreAttributeSyntax.isPresent());
        entryValidator.setCheckMalformedDNs(!ignoreMalformedDNs.isPresent());
        entryValidator.setCheckMissingAttributes(!ignoreMissingAttributes.isPresent());
        entryValidator.setCheckNameForms(!ignoreNameForms.isPresent());
        entryValidator.setCheckProhibitedAttributes(!ignoreProhibitedAttributes.isPresent());
        entryValidator.setCheckProhibitedObjectClasses(!ignoreProhibitedObjectClasses.isPresent());
        entryValidator.setCheckMissingSuperiorObjectClasses(!ignoreMissingSuperiorObjectClasses.isPresent());
        entryValidator.setCheckSingleValuedAttributes(!ignoreSingleValuedAttributes.isPresent());
        entryValidator.setCheckStructuralObjectClasses(!ignoreStructuralObjectClasses.isPresent());
        entryValidator.setCheckUndefinedAttributes(!ignoreUndefinedAttributes.isPresent());
        entryValidator.setCheckUndefinedObjectClasses(!ignoreUndefinedObjectClasses.isPresent());
        final LDIFReader ldifReader;
        rejectWriter = null;
        try {
            InputStream inputStream = new FileInputStream(ldifFile.getValue());
            if (isCompressed.isPresent()) {
                inputStream = new GZIPInputStream(inputStream);
            }
            ldifReader = new LDIFReader(inputStream, numThreads.getValue(), this);
        } catch (Exception e) {
            err("Unable to open the LDIF reader:  ", getExceptionMessage(e));
            return ResultCode.LOCAL_ERROR;
        }
        ldifReader.setSchema(schema);
        if (ignoreDuplicateValues.isPresent()) {
            ldifReader.setDuplicateValueBehavior(DuplicateValueBehavior.STRIP);
        } else {
            ldifReader.setDuplicateValueBehavior(DuplicateValueBehavior.REJECT);
        }
        try {
            try {
                if (rejectFile.isPresent()) {
                    rejectWriter = new LDIFWriter(rejectFile.getValue());
                }
            } catch (Exception e) {
                err("Unable to create the reject writer:  ", getExceptionMessage(e));
                return ResultCode.LOCAL_ERROR;
            }
            ResultCode resultCode = ResultCode.SUCCESS;
            while (true) {
                try {
                    final Entry e = ldifReader.readEntry();
                    if (e == null) {
                        break;
                    }
                } catch (LDIFException le) {
                    malformedEntries.incrementAndGet();
                    if (resultCode == ResultCode.SUCCESS) {
                        resultCode = ResultCode.DECODING_ERROR;
                    }
                    if (rejectWriter != null) {
                        try {
                            rejectWriter.writeComment("Unable to parse an entry read from LDIF:", false, false);
                            if (le.mayContinueReading()) {
                                rejectWriter.writeComment(getExceptionMessage(le), false, true);
                            } else {
                                rejectWriter.writeComment(getExceptionMessage(le), false, false);
                                rejectWriter.writeComment("Unable to continue LDIF processing.", false, true);
                                err("Aborting LDIF processing:  ", getExceptionMessage(le));
                                return ResultCode.LOCAL_ERROR;
                            }
                        } catch (IOException ioe) {
                            err("Unable to write to the reject file:", getExceptionMessage(ioe));
                            err("LDIF parse failure that triggered the rejection:  ", getExceptionMessage(le));
                            return ResultCode.LOCAL_ERROR;
                        }
                    }
                } catch (IOException ioe) {
                    if (rejectWriter != null) {
                        try {
                            rejectWriter.writeComment("I/O error reading from LDIF:", false, false);
                            rejectWriter.writeComment(getExceptionMessage(ioe), false, true);
                            return ResultCode.LOCAL_ERROR;
                        } catch (Exception ex) {
                            err("I/O error reading from LDIF:", getExceptionMessage(ioe));
                            return ResultCode.LOCAL_ERROR;
                        }
                    }
                }
            }
            if (malformedEntries.get() > 0) {
                out(malformedEntries.get() + " entries were malformed and could not " + "be read from the LDIF file.");
            }
            if (entryValidator.getInvalidEntries() > 0) {
                if (resultCode == ResultCode.SUCCESS) {
                    resultCode = ResultCode.OBJECT_CLASS_VIOLATION;
                }
                for (final String s : entryValidator.getInvalidEntrySummary(true)) {
                    out(s);
                }
            } else {
                if (malformedEntries.get() == 0) {
                    out("No errors were encountered.");
                }
            }
            return resultCode;
        } finally {
            try {
                ldifReader.close();
            } catch (Exception e) {
            }
            try {
                if (rejectWriter != null) {
                    rejectWriter.close();
                }
            } catch (Exception e) {
            }
        }
    }
