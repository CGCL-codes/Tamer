    public void setOutputvar(IOutputvar ioutputvar) {
        outputIsDefined = true;
        Outputvar outputvar = (Outputvar) ioutputvar;
        if (!ovSet.add(outputvar)) {
            writer.write("\n Error. Variable " + outputvar + " already defined");
        }
    }
