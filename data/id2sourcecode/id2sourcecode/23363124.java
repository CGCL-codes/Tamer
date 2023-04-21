    public void deploy() throws BuildException {
        System.out.println("*** Deploying: " + sourceJar);
        System.out.println("*** JBoss dir: " + jbossDeployDir);
        File source = new File(sourceJar);
        File dest = new File(jbossDeployDir + "/deploy/" + (new File(sourceJar).getName()));
        task.log("*** Copying: " + source.getAbsolutePath() + " to " + dest.getAbsolutePath());
        try {
            FileUtils.copyFile(source, dest, true);
        } catch (Exception e) {
            throw new BuildException("Error copying file " + sourceJar + " to " + jbossDeployDir + ": " + e.toString());
        }
        task.log("*** Waiting 10s for JBoss to deploy " + "new jar file...");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {
        }
        System.out.println();
    }
