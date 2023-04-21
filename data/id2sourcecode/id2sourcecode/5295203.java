        Watcher(File d, Hashtable b, Vector l) throws IOException, SecurityException {
            dir = d;
            devices = new File(dir, "devices");
            busses = b;
            listeners = l;
            if (!dir.exists() || !dir.isDirectory()) throw new IOException("is usbdevfs mounted?  " + d.getAbsolutePath());
            while (scan()) continue;
            if (busses.isEmpty()) throw new IOException("no devices; maybe usbdevfs denies read/write access?");
        }
