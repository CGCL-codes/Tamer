    private String createChannelsC() {
        String channels = "";
        for (String chan : project.getChannels()) {
            channels += "\"" + chan + "\", ";
        }
        return "std::string channels[] = {" + channels + "};\n" + "#define CHANNELS_COUNT " + project.getChannels().size() + "\n";
    }
