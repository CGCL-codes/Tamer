    private void copyTemplateSource(File destinationFile) {
        TransactionalFileSaver saver = new TransactionalFileSaver(timeSlotTracker, destinationFile.getAbsolutePath());
        InputStream source = null;
        FileOutputStream destination = null;
        try {
            String filename = "/" + destinationFile.getName();
            logger.info("Coping from [" + filename + "] to [" + destinationFile + "]");
            source = XmlDataSource.class.getResourceAsStream(filename);
            destination = new FileOutputStream(saver.begin());
            int readByte = 0;
            while ((readByte = source.read()) > 0) {
                destination.write(readByte);
            }
            destination.close();
            source.close();
            saver.commit();
            Object[] arg = { destinationFile.getName() };
            String msg = timeSlotTracker.getString("datasource.xml.copyFile.copied", arg);
            logger.fine(msg);
        } catch (Exception e) {
            Object[] expArgs = { e.getMessage() };
            String expMsg = timeSlotTracker.getString("datasource.xml.copyFile.exception", expArgs);
            timeSlotTracker.errorLog(expMsg);
            timeSlotTracker.errorLog(e);
        } finally {
            try {
                if (destination != null) {
                    destination.close();
                }
                if (source != null) {
                    source.close();
                }
            } catch (Exception e) {
                Object[] expArgs = { e.getMessage() };
                String expMsg = timeSlotTracker.getString("datasource.xml.copyFile.exception", expArgs);
                timeSlotTracker.errorLog(expMsg);
                timeSlotTracker.errorLog(e);
            }
        }
    }
