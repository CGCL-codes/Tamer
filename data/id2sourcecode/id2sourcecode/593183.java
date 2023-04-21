    private void archiveOldItem(ScheduleItem removedItem) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd@HHmmssS");
            String archiveName = new DllWrapper().getAllUserPath() + "archive\\Schedule-" + df.format(removedItem.getStart()) + " (" + removedItem.getChannel() + ") (" + removedItem.getName() + ").sof";
            File outFile = new File(archiveName);
            outFile = outFile.getCanonicalFile();
            File parent = outFile.getParentFile();
            if (parent.exists() == false) parent.mkdirs();
            FileOutputStream fos = new FileOutputStream(outFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(removedItem);
            oos.close();
            fos.close();
        } catch (Exception e) {
            System.out.println("Error trying to archive old Schedule Item:");
            e.printStackTrace();
        }
    }
