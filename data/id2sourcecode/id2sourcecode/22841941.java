    public StorageHandler(String name, Context context) {
        this.name = name;
        fileWriter = null;
        fileReader = null;
        state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            readable = writeable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            readable = true;
            writeable = false;
        } else {
            readable = writeable = false;
        }
        String path;
        path = File.separator;
        path = path.concat("Android").concat(File.separator).concat("data").concat(File.separator).concat("pckt.Test").concat(File.separator).concat("files").concat(File.separator);
        file = new File(Environment.getExternalStorageDirectory() + path, this.name);
    }
