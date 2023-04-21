    void mount(String driveLetter, String mountedVolume) {
        Dokan.removeMountPoint(driveLetter);
        this.mountedVolume = mountedVolume;
        DokanOptions dokanOptions = new DokanOptions();
        dokanOptions.mountPoint = driveLetter;
        dokanOptions.threadCount = Main.writeThreads;
        this.driveLetter = driveLetter;
        log.info("######## mounting " + mountedVolume + " to " + this.driveLetter + " #############");
        int result = Dokan.mount(dokanOptions, this);
        if (result < 0) {
            System.out.println("Unable to mount volume because result = " + result);
            log.error("Unable to mount volume because result = " + result);
            if (result == -1) System.out.println("General Error");
            if (result == -2) System.out.println("Bad Drive letter");
            if (result == -3) System.out.println("Can't install driver");
            if (result == -4) System.out.println("Driver something wrong");
            if (result == -5) System.out.println("Can't assign a drive letter or mount point");
            if (result == -6) System.out.println("Mount point is invalid");
            System.exit(-1);
        } else {
            log.info("######## mounted " + mountedVolume + " to " + this.driveLetter + " with result " + result + " #############");
        }
    }
