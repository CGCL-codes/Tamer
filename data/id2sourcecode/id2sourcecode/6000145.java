    @Override
    public String getLongDescription(final String cmd) {
        String dsc = null;
        if ("plug-in".equals(cmd)) {
            dsc = "\tLoad plugin. This command takes as argument the name of\n" + "\tthe  class  that  implements  the  plugin, that is then\n" + "\tloaded from your classpath. The plugin then behaves like\n" + "\tany other built-in command (including help and completion).\n\n" + "\tWriting a  plugin is  simple: Just  write a  class that\n" + "\timplements the well documented henplus.Command interface.\n" + "\tYou can just simply derive from henplus.AbstractCommand\n" + "\tthat already implements the default behaviour. An example\n" + "\tof a plugin is the henplus.SamplePlugin that does nothing\n" + "\tbut shows how it works; try it:\n\n" + "\tplug-in henplus.SamplePlugin\n\n" + "\tOn exit of HenPlus, all names of the plugin-classes are\n" + "\tstored, so that  they are automatically  loaded on next\n" + "\tstartup.";
        } else if ("plug-out".equals(cmd)) {
            dsc = "\tUnload plugin. Unload a previously loaded plugin. This\n" + "\tcommand provides completion for the class name.\n";
        } else if ("list-plugins".equals(cmd)) {
            dsc = "\tList the plugins loaded. The plugins, that are actually\n" + "\tloaded have a little star (*) in the first column. If it\n" + "\tis not loaded, then you have to extend your CLASSPATH to\n" + "\taccess the plugins class.";
        }
        return dsc;
    }
