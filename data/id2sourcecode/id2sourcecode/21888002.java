    public boolean verifyPassword(final Password password) {
        boolean success = false;
        if (this.sources.size() == 0) {
            success = true;
        } else {
            for (Map.Entry<String, String> entry : this.sources.entrySet()) {
                final String p = entry.getValue();
                if (this.digest != null) {
                    final String hash = this.digest.digest(password.getText().getBytes(), this.converter);
                    if (p.equals(hash)) {
                        success = false;
                        this.setMessage(String.format("Password can not be the same as your %s password", entry.getKey()));
                        break;
                    } else {
                        success = true;
                    }
                } else {
                    if (p.equals(password.getText())) {
                        success = false;
                        this.setMessage(String.format("Password can not be the same as your %s password", entry.getKey()));
                        break;
                    } else {
                        success = true;
                    }
                }
            }
        }
        return success;
    }
