    void setSyncReceipt(String receiptStr) throws PrismsRecordException {
        byte[] receiptBytes = new byte[receiptStr.length() / 2];
        for (int i = 0; i < receiptBytes.length; i++) {
            int _byte = fromHex(receiptStr.charAt(i * 2));
            _byte = _byte << 4 | fromHex(receiptStr.charAt(i * 2 + 1));
            receiptBytes[i] = (byte) _byte;
        }
        final String[] message = new String[] { null };
        final boolean[] hasReceipt = new boolean[] { false };
        final java.io.Reader reader;
        try {
            java.io.Reader tempReader = new java.io.InputStreamReader(new prisms.util.ImportStream(new java.io.ByteArrayInputStream(receiptBytes)));
            java.io.StringWriter writer = new java.io.StringWriter();
            int read = tempReader.read();
            while (read >= 0) {
                writer.write(read);
                read = tempReader.read();
            }
            String json = writer.toString();
            reader = new java.io.StringReader(json);
            new prisms.util.json.SAJParser().parse(reader, new prisms.util.json.SAJParser.DefaultHandler() {

                @Override
                public void separator(ParseState state) {
                    super.separator(state);
                    if ("receipt".equals(state.top().getPropertyName())) {
                        try {
                            state.spoofValue();
                            valueNull(state);
                            theSynchronizer.readSyncReceipt(reader);
                        } catch (IOException e) {
                            throw new IllegalStateException("Could not parse synchronization receipt", e);
                        } catch (ParseException e) {
                            throw new IllegalStateException("Could not parse synchronization receipt", e);
                        } catch (PrismsRecordException e) {
                            throw new IllegalStateException("Could not read synchronization receipt: " + e.getMessage(), e);
                        }
                        hasReceipt[0] = true;
                    }
                }

                @Override
                public void valueString(ParseState state, String value) {
                    super.valueString(state, value);
                    if ("message".equals(state.top().getPropertyName())) message[0] = value;
                }
            });
        } catch (IOException e) {
            throw new PrismsRecordException("Not a synchronization receipt", e);
        } catch (ParseException e) {
            throw new PrismsRecordException("Could not parse synchronization receipt", e);
        } catch (IllegalStateException e) {
            if (e.getCause() != null) throw new PrismsRecordException(e.getMessage(), e.getCause()); else throw e;
        }
        UI ui = getSession().getUI();
        if (!hasReceipt[0]) ui.error("Not a synchronization receipt"); else if (message[0] == null) ui.warn("No message included--receipt input successful"); else ui.info(message[0]);
    }
