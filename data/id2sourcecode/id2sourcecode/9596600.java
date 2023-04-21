    public static ErrorReturn parseError(JDOConnector connector, JDOException e) {
        ErrorReturn r = new ErrorReturn();
        r._error = Error.JDO_Error;
        Throwable[] nested = e.getNestedExceptions();
        if (nested != null) {
            r = connector.getDatabaseImpl().parseErrorCommand(nested);
        } else {
            String msg = e.getMessage();
            if (msg.startsWith("Unable to load resource: ")) {
                r._error = Error.JDO_ResourceNotFound;
                r._args = new String[] { msg.substring(msg.indexOf(":") + 2) };
            } else if (msg.equals("Unexpected exception caught.")) {
            } else if (msg.equals("Active transaction required for write operation")) {
                r._error = Error.JDO_TransactionRequired;
            } else if (msg.equals("The transaction is already active")) {
                r._error = Error.JDO_TransactionAlreadyActive;
            } else if (msg.equals("No active transaction.") || msg.equals("Transaction is not active")) {
                r._error = Error.JDO_NoActiveTransaction;
            } else if (msg.startsWith("The supplied instance is not of type javax.jdo.spi.PersistenceCapable")) {
                r._error = Error.JDO_NoPersistenceCapable;
                r._args = new String[] { msg.substring(71, msg.length() - 1) };
            } else if (msg.startsWith("Meta data referenced a field '")) {
                r._error = Error.JDO_BadEnhanced;
            } else if (msg.equals("Not allowed to read/write to a instance marked for deletion")) {
                r._error = Error.JDO_CantReadWriteDeleted;
            } else if (msg.startsWith("One or more classes in the JDO meta data have not been enhanced:")) {
                r._error = Error.JDO_NotEnhanced;
            } else if (msg.equals("May not close 'PersistenceManager' with active transaction")) {
                r._error = Error.JDO_CloseErrorBecauseActiveTransaction;
            } else if (msg.startsWith("There is no metadata registered for class")) {
                r._error = Error.JDO_NoMetadata;
                r._args = new String[] { msg.substring(msg.indexOf("class ")) };
            } else if (msg.startsWith("Invalid method name: '")) {
                r._error = Error.JDO_InvalidMethod;
                int tmpPos = msg.indexOf("'") + 1;
                r._args = new String[] { msg.substring(tmpPos, msg.lastIndexOf("'")) };
            } else if (msg.startsWith("Field '") && msg.substring(msg.lastIndexOf("'")).startsWith("' not found on ")) {
                r._error = Error.JDO_FieldNotExisting;
                int tmpPos = msg.lastIndexOf("'");
                r._args = new String[] { msg.substring(msg.indexOf("'") + 1, tmpPos), msg.substring(tmpPos + 15) };
            } else if (e instanceof JDOObjectNotFoundException && msg.startsWith("Object not found:")) {
                r._error = Error.JDO_ObjectNotFound;
                r._args = new String[] { msg.substring(18) };
            } else {
                r._args = new String[] { msg };
            }
        }
        return r;
    }
