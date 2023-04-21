    private static OutcomeTh processROR(RetrieveOutcomeReply ror, Connection c) throws JobManager.Exception {
        byte[] outcome_bytes = ror.getOutcome();
        Outcome njs_reply;
        try {
            njs_reply = (Outcome) (new ObjectInputStream(new ByteArrayInputStream(outcome_bytes))).readObject();
        } catch (java.lang.Exception ex) {
            throw new JobManager.Exception("Problems deserialising Outcome (classes compatible?)", ex);
        }
        OutcomeTh reply = new OutcomeTh();
        reply.setOutcome((AbstractJob_Outcome) njs_reply);
        if (ror.hasStreamed()) {
            CLogger.status("Reading streamed data from a RetrieveOutcomeReply");
            ZipInputStream streamed = null;
            try {
                streamed = c.getStreamedInputStream();
            } catch (IOException ioex) {
                throw new JobManager.Exception("Problems getting streamed data: " + ioex.getMessage(), ioex);
            }
            try {
                String message = null;
                ZipEntry entry = streamed.getNextEntry();
                while (entry != null) {
                    String mode = "no mode";
                    if (entry.getExtra() != null) {
                        mode = "mode " + Byte.toString(entry.getExtra()[0]);
                    }
                    if (entry.isDirectory()) {
                        CLogger.status("Directory <" + entry.getName() + "> skipped.");
                    } else {
                        File entry_file = new File(outcome_dir, entry.getName().replace('/', '_'));
                        if (deleteOnExit) entry_file.deleteOnExit();
                        CLogger.status("Extracting file <" + entry.getName() + "> to <" + entry_file + ">, size <" + entry.getSize() + ">, mode <" + mode + ">");
                        byte[] buffer = new byte[65536];
                        FileOutputStream fos = new FileOutputStream(entry_file);
                        int read;
                        do {
                            read = streamed.read(buffer, 0, buffer.length);
                            CLogger.status("Read <" + read + "> from streamed data");
                            if (read > 0) {
                                fos.write(buffer, 0, read);
                            }
                        } while (read >= 0);
                        fos.close();
                        String path = entry.getName();
                        String the_action_id = null;
                        StringTokenizer st = new StringTokenizer(path, "/");
                        while (st.hasMoreTokens()) {
                            String this_token = st.nextToken();
                            if (!this_token.startsWith("AA")) break;
                            the_action_id = this_token;
                        }
                        if (the_action_id == null) {
                            message = "Streamed file names badly structured (expect AA): " + path;
                            break;
                        }
                        Integer id_value;
                        try {
                            long temp = Long.parseLong(the_action_id.substring(2), 16);
                            if (temp > Integer.MAX_VALUE) temp = temp + 2 * Integer.MIN_VALUE;
                            id_value = new Integer((int) temp);
                        } catch (java.lang.Exception ex) {
                            message = "Problems parsing directory as AA identifier: " + the_action_id;
                            break;
                        }
                        Collection already_there = (Collection) reply.getFilesMapping().get(id_value);
                        if (already_there == null) {
                            already_there = new HashSet();
                            reply.getFilesMapping().put(id_value, already_there);
                        }
                        already_there.add(entry_file);
                    }
                    entry = streamed.getNextEntry();
                }
                if (message != null) {
                    throw new JobManager.Exception(message);
                }
            } catch (ZipException zex) {
                throw new JobManager.Exception("Errors (zip) reading data streamed from NJS.", zex);
            } catch (IOException ioex) {
                throw new JobManager.Exception("Errors reading data streamed from NJS.", ioex);
            }
            try {
                streamed.close();
            } catch (java.lang.Exception ex) {
            }
            c.doneWithStreamedInputStream();
        }
        return reply;
    }
