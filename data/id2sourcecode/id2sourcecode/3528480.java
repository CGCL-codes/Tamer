    private boolean checkReadData() {
        for (int i = 0; i < reader.length; i++) {
            if (writer.value == null) {
                if (reader[i].value != null) {
                    return false;
                }
            } else {
                if (!writer.value.equals(reader[i].value)) {
                    return false;
                }
            }
        }
        return true;
    }
