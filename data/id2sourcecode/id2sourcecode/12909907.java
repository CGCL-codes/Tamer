    public static void replaceFile(File target, File replacement) throws IOException {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(replacement);
            outputStream = new FileOutputStream(target);
            inputStream.getChannel().transferTo(0, replacement.length(), outputStream.getChannel());
            StreamUtils.transfer(inputStream, outputStream);
        } finally {
            StreamUtils.closeStream(inputStream);
            StreamUtils.closeStream(outputStream);
        }
    }
