    public static void init_water(Caps caps) {
        String os_name = os_name();
        String user_home_folder = System.getProperty("user.home");
        String runtimeFolder, userFolder;
        user_home_folder = user_home_folder.replace('\\', '/');
        System.out.println("OS name: " + os_name);
        if (os_name == "Windows") {
            runtimeFolder = "C:/Program Files/water/" + waterVersion + "/";
            userFolder = user_home_folder + "/Application Data/water/";
        } else if (os_name == "Mac") {
            runtimeFolder = "/Applications/water/" + waterVersion + "/";
            userFolder = user_home_folder + "/water/";
        } else {
            runtimeFolder = "../";
            userFolder = user_home_folder + "/water/";
        }
        initWw(null);
        importLogicals(caps, stringAppend(stringAppend("<logical user=<resource '", userFolder, "' read write append create execute/>"), stringAppend(" water_runtime=<resource '", runtimeFolder, "' read execute/>"), "/>"));
        File user_folder_java = new File(userFolder);
        if (!(user_folder_java.exists())) {
            user_folder_java.mkdir();
        }
    }
