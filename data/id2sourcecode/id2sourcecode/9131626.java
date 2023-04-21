    public static void main(String[] argv) throws IOException, VSMException {
        ImageDiskCreateDM instance = null;
        if (argv.length != 17) {
            System.out.println("usage: args are:\ninetaddr port user pw target image_disk_number image_disk_device_type image_disk_allocation_type allocation_area_name_or_volser allocation_unit_size image_disk_size image_disk_mode image_disk_formatting image_disk_label read_password write_password multi_password");
            System.exit(1);
        }
        System.out.println("Args are: " + argv[0] + " " + argv[1] + " " + argv[2] + " " + argv[3] + " " + argv[4] + " " + argv[5] + " " + argv[6] + " " + argv[7] + " " + argv[8] + " " + argv[9] + " " + argv[10] + " " + argv[11] + " " + argv[12] + " " + argv[13] + " " + argv[14] + " " + argv[15] + " " + argv[16]);
        instance = new ImageDiskCreateDM(argv[0], Integer.valueOf(argv[1]).intValue(), argv[2], argv[3], argv[4], argv[5], argv[6], argv[7], argv[8], Integer.valueOf(argv[9]).intValue(), Integer.valueOf(argv[10]).intValue(), argv[11], Integer.valueOf(argv[12]).intValue(), argv[13], argv[14], argv[15], argv[16]);
        ParameterArray pA = instance.doIt();
        System.out.println("Returns from call to " + instance.getFunctionName() + ":");
        System.out.println(pA.prettyPrintAll());
    }
