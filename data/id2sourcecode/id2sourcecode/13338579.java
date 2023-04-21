    private boolean setAttribute(String name, String value) {
        if (name.equals("session") == true) {
            try {
                sessionId = Long.parseLong(value);
            } catch (NumberFormatException ex) {
                errorMessage = "The session id is not an integer";
                return false;
            }
        } else if (name.equals("transfer") == true) {
            transferId = value;
        } else if (name.equals("transfer-from") == true) {
            transferFrom = value;
        } else if (name.equals("transcript") == true) {
            if (value.equals("yes") == true) {
                transcript = true;
            } else if (value.equals("no") == true) {
                transcript = false;
            } else {
                errorMessage = "The transcript parameter has invalid value " + value;
                return false;
            }
        }
        return true;
    }
