    static void processException(Exception e, String message, Component component) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        Log.log(Log.DEBUG, Thread.currentThread(), writer.toString());
        String msg = MessageFormat.format(jEdit.getProperty("xmlindenter.message.error"), new Object[] { message, e.getMessage() });
        JOptionPane.showMessageDialog(component, msg.toString());
    }
