    public static void main(String[] args) {
        Properties arguments = ArgParser.ArgsToProperties(args);
        if (arguments == null || arguments.size() < 2) {
            ShowDiscription();
            return;
        }
        String sourcefilename = arguments.getProperty("unnamed0");
        File sourcefile = new File(sourcefilename);
        if (!sourcefile.isFile()) {
            System.out.println("ERROR: " + sourcefilename + " is not a file!");
            return;
        }
        boolean removeDest = false;
        if (arguments.containsKey("force")) {
            removeDest = true;
        }
        String destfilename = arguments.getProperty("unnamed1");
        File destfile = new File(destfilename);
        if (destfile.isFile()) {
            if (removeDest == false) {
                System.out.println("ERROR: " + destfilename + " already exists!");
                return;
            } else {
                destfile.delete();
            }
        }
        if (arguments.containsKey("rg")) {
            removeGroupName = true;
            System.out.println("Removing the Groupname from all Variables and Dimension!");
        }
        NetcdfFile source = null;
        NetcdfFileWriteable dest = null;
        try {
            source = NetcdfFile.open(sourcefilename);
            dest = NetcdfFileWriteable.createNew(destfilename);
            List<Variable> vars = source.getVariables();
            List<Dimension> dims = source.getDimensions();
            destdims = new ArrayList<Dimension>();
            System.out.println("Dimensions:");
            int unnamed = 0;
            for (Variable var : vars) {
                List<Dimension> vardims = var.getDimensions();
                for (Dimension dim : vardims) {
                    if (dim.getName() == null) {
                        dim.setName("unnamed" + unnamed);
                        unnamed++;
                    }
                    if (dim.getGroup() == null) {
                        dim.setGroup(source.getRootGroup());
                    }
                    if (!dims.contains(dim)) dims.add(dim);
                }
                if (var.getDataType() == DataType.STRUCTURE) {
                    Structure struct = (Structure) var;
                    List<Variable> svars = struct.getVariables();
                    for (Variable svar : svars) {
                        List<Dimension> svardims = svar.getDimensions();
                        for (Dimension dim : svardims) {
                            if (dim.getName() == null) {
                                dim.setName("unnamed" + unnamed);
                                unnamed++;
                            }
                            if (dim.getGroup() == null) {
                                dim.setGroup(source.getRootGroup());
                            }
                            if (!dims.contains(dim)) dims.add(dim);
                        }
                    }
                }
            }
            ArrayList<String> flatnames = new ArrayList<String>();
            for (Dimension dim : dims) {
                String flatname = Name2FlatName(dim.getName());
                if (!flatnames.contains(flatname)) {
                    flatnames.add(flatname);
                    System.out.println(dim.toString());
                    destdims.add(dest.addDimension(flatname, dim.getLength()));
                }
            }
            for (Variable var : vars) {
                String flatname = Name2FlatName(var.getName());
                System.out.println("Define: " + var.getName() + " -> " + flatname);
                if (var.getDataType() != DataType.STRUCTURE) {
                    Variable temp = dest.addVariable(flatname, var.getDataType(), getFlatDims(var));
                    List<Attribute> atts = var.getAttributes();
                    for (Attribute att : atts) dest.addVariableAttribute(temp, att);
                } else {
                    Structure struct = (Structure) var;
                    List<Variable> svars = struct.getVariables();
                    for (Variable svar : svars) {
                        System.out.println("Define: " + var.getName() + "." + svar.getShortName() + " -> " + flatname + "_" + svar.getShortName());
                        Variable temp = dest.addVariable(flatname + "_" + svar.getShortName(), svar.getDataType(), getFlatDims(svar));
                        List<Attribute> atts = svar.getAttributes();
                        for (Attribute att : atts) dest.addVariableAttribute(temp, att);
                    }
                }
            }
            List<Attribute> gatts = source.getGlobalAttributes();
            for (Attribute att : gatts) {
                Attribute natt = new Attribute(Name2FlatName(att.getName()), att);
                dest.addGlobalAttribute(natt);
            }
            dest.create();
            for (Variable var : vars) {
                String flatname = Name2FlatName(var.getName());
                try {
                    if (var.getDataType() != DataType.STRUCTURE) {
                        System.out.println("Write: " + var.getName() + " -> " + flatname);
                        dest.write(flatname, var.read());
                    } else {
                        Structure struct = (Structure) var;
                        List<Variable> svars = struct.getVariables();
                        for (Variable svar : svars) {
                            System.out.println("Write: " + var.getName() + "." + svar.getShortName() + " -> " + flatname + "_" + svar.getShortName());
                            dest.write(flatname + "_" + svar.getShortName(), svar.read());
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("        -> unable to write data:");
                    System.out.println("        -> " + ex.toString());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (source != null) source.close();
                if (dest != null) dest.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
