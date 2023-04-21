    public boolean verifyPassword(final Password password) {
        boolean success = false;
        synchronized (this.history) {
            if (this.history.size() == 0) {
                success = true;
            } else {
                for (String p : this.history) {
                    if (this.digest != null) {
                        final String hash = this.digest.digest(password.getText().getBytes(), this.converter);
                        if (p.equals(hash)) {
                            success = false;
                            this.setMessage(String.format("Password matches one of %s previous passwords", this.history.size()));
                            break;
                        } else {
                            success = true;
                        }
                    } else {
                        if (p.equals(password.getText())) {
                            success = false;
                            this.setMessage(String.format("Password matches one of %s previous passwords", this.history.size()));
                            break;
                        } else {
                            success = true;
                        }
                    }
                }
            }
        }
        return success;
    }
