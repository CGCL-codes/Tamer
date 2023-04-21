    private static String backupFile(BufferedReader reader, BufferedWriter writer, String filename) {
        try {
            String line = null;
            StringBuffer ret = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                writer.append(line + "\n");
            }
            return ret.toString();
        } catch (NullPointerException npe) {
            String msg = "Failed to copy file for backup [" + filename + "].";
            _log.error(msg, npe);
            throw new InvalidImplementationException(msg, npe);
        } catch (IOException ioe) {
            String msg = "Cannot access file for backup [" + filename + "].";
            _log.error(ioe, ioe);
            throw new InvalidImplementationException(msg, ioe);
        } finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
            }
        }
    }
