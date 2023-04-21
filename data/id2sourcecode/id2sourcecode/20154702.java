    private void run() {
        System.out.println("! Statistics for '" + this.logfile + "'");
        System.out.println();
        try {
            ArrayList<IntCounter> channels = new ArrayList<IntCounter>();
            ArrayList<IntCounter> avatars = new ArrayList<IntCounter>();
            Runtime rt = Runtime.getRuntime();
            LogReader reader = new T4CClientReader(new FileInputStream(this.logfile), this.conf);
            List<Message> messages = Collections.synchronizedList(new ArrayList<Message>());
            long nMessages = 0;
            System.out.println("! Messages : [total number , used memory in KB]");
            System.out.println("! -------------");
            Date start = new Date();
            for (Message next = reader.read(); next != null; next = reader.read()) {
                if (nMessages % 1024 == 0) System.out.println(nMessages + "\t" + (rt.totalMemory() - rt.freeMemory()) / 1000);
                IntCounter newChanCount = new IntCounter(next.getChannel(), 1);
                int chanIndex = channels.indexOf(newChanCount);
                if (chanIndex >= 0) ((IntCounter) channels.get(chanIndex)).incCount(1); else channels.add(newChanCount);
                IntCounter newAvCount = new IntCounter(next.getAvatar(), 1);
                int avIndex = avatars.indexOf(newAvCount);
                if (avIndex >= 0) ((IntCounter) avatars.get(avIndex)).incCount(1); else avatars.add(newAvCount);
                messages.add(next);
                nMessages++;
            }
            Date end = new Date();
            System.out.println("! " + nMessages + " messages ; used memory : " + (rt.totalMemory() - rt.freeMemory()) / 1000 + "K ; read in " + (double) (end.getTime() - start.getTime()) / 1000 + " seconds.");
            TreeSet sortedChannels = new TreeSet();
            for (Iterator cit = channels.iterator(); cit.hasNext(); ) sortedChannels.add(cit.next());
            System.out.println();
            System.out.println("! Channels (total : " + channels.size() + ") : [name , instances]");
            System.out.println("! -------------");
            long channols = 0;
            for (Iterator cit = sortedChannels.iterator(); cit.hasNext(); ) {
                IntCounter chan = (IntCounter) cit.next();
                channols += chan.getCount();
                System.out.println(chan.getName() + "\t\t\t" + chan.getCount());
            }
            System.out.println("! Total number of instances -> " + channols);
            TreeSet sortedAvatars = new TreeSet();
            for (Iterator avit = avatars.iterator(); avit.hasNext(); ) sortedAvatars.add(avit.next());
            System.out.println();
            System.out.println("! Avatars (total : " + avatars.size() + ") : [name , instances]");
            System.out.println("! -------------");
            long avators = 0;
            for (Iterator avit = sortedAvatars.iterator(); avit.hasNext(); ) {
                IntCounter avat = (IntCounter) avit.next();
                avators += avat.getCount();
                System.out.println(avat.getName() + "\t\t\t" + avat.getCount());
            }
            System.out.println("! Total number of instances -> " + avators);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(ERROR_RUN);
        }
    }
