    public static void main(String[] args) throws IOException {
        IsoFile isoFile = new IsoFile(new FileInputStream("/home/sannies/aaa/FBW_fixedres_B_640x360_400.ismv").getChannel());
        Path p = new Path(isoFile);
        UuidBasedProtectionSystemSpecificHeaderBox pssh = (UuidBasedProtectionSystemSpecificHeaderBox) p.getPath("/moov/uuid");
        pssh.getSystemIdString();
    }
