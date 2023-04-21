    public String execute(String commandArgs, Map<String, Object> context) {
        String locations[] = commandArgs.split(Constants.SPACE);
        if (locations.length != 2) {
            return "Invalid syntax. Try copy [source directory] [destination directory]";
        }
        String pwd = (String) context.get(Constants.KEY_PWD);
        String src = locations[0];
        String destn = locations[1];
        src = !src.startsWith(pwd) ? (pwd + File.separator + src) : src;
        try {
            FileUtils.copyFileToDirectory(new File(src), new File(destn));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Copied " + src + " to " + destn;
    }
