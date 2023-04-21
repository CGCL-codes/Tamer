    public static void exportInstances(Tasks task, final ZipOutputStream stream, List<Instance> instances) throws IOException, SQLException, NoConnectionToDBException, InstanceNotInDBException, InterruptedException {
        task.setOperationName("Exporting instances..");
        int current = 1;
        for (Instance i : instances) {
            task.setStatus("Writing instance " + current + " / " + instances.size());
            task.setTaskProgress(current / (float) instances.size());
            stream.putNextEntry(new ZipEntry("instance_" + i.getId() + ".binary"));
            writeInstanceBinaryToStream(new ObjectOutputStream(stream), i);
            current++;
        }
        task.setTaskProgress(0.f);
        task.setStatus("Writing instance inforamtions..");
        stream.putNextEntry(new ZipEntry("instances.edacc"));
        writeInstancesToStream(new ObjectOutputStream(stream), instances);
        task.setStatus("Done.");
    }
