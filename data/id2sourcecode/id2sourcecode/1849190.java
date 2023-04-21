    public OutputPort getOutputPort(String file, boolean append) throws FileNotFoundException, IOException {
        if (file.equals("/dev/null")) {
            return new StreamOutputPort(new NullOutputStream());
        } else {
            URL url = Util.tryURL(file);
            if (url != null) return new StreamOutputPort(url.openConnection().getOutputStream());
        }
        return new FileOutputPort(getFile(file), append);
    }
