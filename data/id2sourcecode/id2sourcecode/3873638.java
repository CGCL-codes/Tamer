    public static void main(String[] args) {
        if (args.length == 1 && "writer".equals(args[0])) {
            LicenseMakerFrame.main(new String[0]);
        } else if (args.length > 1 && "batch".equals(args[0])) {
            String[] newArgs = new String[args.length - 1];
            for (int i = 0; i < newArgs.length; i++) {
                newArgs[i] = args[i + 1];
            }
            LicenseBatchWriter.main(newArgs);
        } else {
            DrivingLicenseApp.main(new String[0]);
        }
    }
