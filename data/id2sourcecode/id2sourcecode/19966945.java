    public static void ParseDownload() {
        int size = MSG.ReadShort(Globals.net_message);
        int percent = MSG.ReadByte(Globals.net_message);
        if (size == -1) {
            Com.Printf("Server heeft dit bestand niet : " + Globals.net_message + "\n");
            if (Globals.cls.download != null) {
                try {
                    Globals.cls.download.close();
                } catch (IOException e) {
                }
                Globals.cls.download = null;
            }
            CL.RequestNextDownload();
            return;
        }
        if (Globals.cls.download == null) {
            String name = DownloadFileName(Globals.cls.downloadtempname).toLowerCase();
            FS.CreatePath(name);
            Globals.cls.download = Lib.fopen(name, "rw");
            if (Globals.cls.download == null) {
                Globals.net_message.readcount += size;
                Com.Printf("Failed to open " + Globals.cls.downloadtempname + "\n");
                CL.RequestNextDownload();
                return;
            }
        }
        try {
            Globals.cls.download.write(Globals.net_message.data, Globals.net_message.readcount, size);
        } catch (Exception e) {
        }
        Globals.net_message.readcount += size;
        if (percent != 100) {
            Globals.cls.downloadpercent = percent;
            MSG.WriteByte(Globals.cls.netchan.message, Defines.clc_stringcmd);
            SZ.Print(Globals.cls.netchan.message, "nextdl");
        } else {
            try {
                Globals.cls.download.close();
            } catch (IOException e) {
            }
            String oldn = DownloadFileName(Globals.cls.downloadtempname);
            String newn = DownloadFileName(Globals.cls.downloadname);
            int r = Lib.rename(oldn, newn);
            if (r != 0) {
                Com.Printf("failed to rename.\n");
            }
            Globals.cls.download = null;
            Globals.cls.downloadpercent = 0;
            CL.RequestNextDownload();
        }
    }
