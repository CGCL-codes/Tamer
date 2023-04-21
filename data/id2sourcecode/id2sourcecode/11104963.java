    public void prepare() {
        if (unlimited) return;
        vm.setStatus(JaguarVM.OK);
        String name = lowerCaseToken(0);
        JaguarRectangle r = vm.getRect(name);
        if (r == null) {
            biRef = null;
            return;
        }
        vm.minimize();
        Jaguar.getRobby().waitForIdle();
        Jaguar.getRobby().delay(20);
        biRef = new JaguarImage(Jaguar.getRobby().createScreenCapture(r));
        if (Jaguar.isLog()) vm.logLine(Jaguar.LOGBLUE, Jaguar.LOGWHITE, Jaguar.LOGPROPORTIONAL, lowerCaseToken(0) + " prefetched", biRef.getImage(), null);
    }
