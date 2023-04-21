    public boolean saveBegin(boolean useAmavisSMTPDirectory) {
        File smtpDirectory = new File(useAmavisSMTPDirectory ? configurationManager.getAmavisSMTPDirectory() : configurationManager.getSMTPDirectory());
        if (getMessageLocation() == null) {
            File messageFile;
            StringBuilder sb;
            int i;
            do {
                sb = new StringBuilder(8);
                for (i = 0; i < 8; i++) {
                    sb.append(characters.charAt(random.nextInt(16)));
                }
                message.setSMTPUID(sb.toString());
                messageFile = new File(smtpDirectory, "smtp" + message.getSMTPUID() + ".tmp");
                if (!messageFile.exists() && !new File(smtpDirectory, "smtp" + message.getSMTPUID() + ".ser").exists()) break;
            } while (true);
            setMessageLocation(messageFile);
            return true;
        } else {
            File toFilename = new File(getMessageLocation().getPath().substring(0, getMessageLocation().getPath().lastIndexOf(".") + 1) + "bak");
            try {
                FileUtils.copyFile(getMessageLocation(), toFilename);
            } catch (IOException ioe) {
                log.error("Error copying file " + getMessageLocation().getPath() + " to " + toFilename.getPath());
                return false;
            }
            toFilename = new File(toFilename.getPath().substring(0, toFilename.getPath().lastIndexOf(".") + 1) + "tmp");
            if (!toFilename.exists()) {
                deleteMessage();
                setMessageLocation(toFilename);
                return true;
            }
            return false;
        }
    }
