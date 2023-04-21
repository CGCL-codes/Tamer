    protected int importProject(File ifile, int newowner) {
        int rval = -1;
        if (mode == MODE_DB) {
            try {
                ProjectInfo pi = new ProjectInfo(1, "none");
                FileInputStream fis = new FileInputStream(ifile);
                DataInputStream fdis;
                fdis = new DataInputStream(new BufferedInputStream(fis));
                byte[] sig = new byte[8];
                fdis.readFully(sig);
                if (FILE_SIG.equals(new String(sig))) {
                    System.out.println("Magic matched");
                } else {
                    System.out.println("This doesn't appear to be a collabREate binary file");
                    return -1;
                }
                int ver = fdis.readInt();
                System.out.println("File format version " + ver);
                byte[] gpid = new byte[GPID_SIZE];
                fdis.readFully(gpid);
                System.out.println("importing " + Utils.toHexString(gpid));
                byte[] hash = new byte[MD5_SIZE];
                fdis.readFully(hash);
                System.out.println("(" + Utils.toHexString(hash) + ")");
                long sub = fdis.readLong();
                long pub = fdis.readLong();
                System.out.println("s " + sub + " p " + pub);
                String desc = fdis.readUTF();
                System.out.println("desc: " + desc);
                CollabreateOutputStream os = new CollabreateOutputStream();
                os.writeInt(newowner);
                os.write(gpid);
                os.write(hash);
                os.writeUTF(desc);
                os.writeLong(pub);
                os.writeLong(sub);
                send_data(MNG_PROJECT_MIGRATE, os.toByteArray());
                int messagesize = dis.readInt();
                if (messagesize != 12) {
                    System.err.println("protocol dictates 12 byte PROJECT_MIGRATE_REPLY, but recieved: " + messagesize);
                    return rval;
                }
                int testcmd = dis.readInt();
                if (testcmd != MNG_PROJECT_MIGRATE_REPLY) {
                    System.err.println("protocol dictates PROJECT_MIGRATE_REPLY, but recieved: " + testcmd);
                    return rval;
                }
                int status = dis.readInt();
                if (status != MNG_MIGRATE_REPLY_SUCCESS) {
                    System.err.println("Project migrate did not succeed on server, check server logs for more info");
                    return rval;
                } else {
                    System.out.println("Project creation succeeded on server");
                }
                int tag = fdis.readInt();
                while (tag == TAG) {
                    long updateid = fdis.readLong();
                    int uid = fdis.readInt();
                    int pid = fdis.readInt();
                    int cmd = fdis.readInt();
                    int datalen = fdis.readInt();
                    byte[] data = new byte[datalen];
                    fdis.readFully(data);
                    System.out.print(".");
                    CollabreateOutputStream cos = new CollabreateOutputStream();
                    cos.writeInt(newowner);
                    cos.writeInt(pid);
                    cos.writeInt(cmd);
                    cos.writeInt(data.length);
                    cos.write(data);
                    send_data(MNG_MIGRATE_UPDATE, cos.toByteArray());
                    tag = fdis.readInt();
                }
                if (tag != ENDTAG) {
                    System.err.println("Error: didn't end update processing loop with ENDTAG");
                } else {
                    rval = 0;
                }
                fdis.close();
                fis.close();
            } catch (Exception ex) {
                System.err.println("Error importing project from " + ifile.getAbsolutePath() + ": " + ex.getMessage());
                ex.printStackTrace();
            }
            System.out.println("");
        } else {
            System.err.println("it appears that the server is configured for BASIC mode");
        }
        return rval;
    }
