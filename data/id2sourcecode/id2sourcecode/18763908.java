    public static GL20ResourceShaderFragment getShaderFragment(ChannelMap channelMap) {
        if (channelMap == null) return null;
        if (singleton == null) new GL20ShaderServer();
        GL20ResourceShaderFragment returnValue = null;
        final int fragmentCount = singleton.fragmentStack.size();
        int fragmentIndex = 0;
        while (fragmentIndex < fragmentCount) {
            if (singleton.fragmentStack.get(fragmentIndex).getChannelMap() == channelMap) break; else fragmentIndex++;
        }
        if (fragmentIndex >= fragmentCount) {
            GL20ResourceShaderFragment newFragment = null;
            if (channelMap instanceof ColorMap) {
                if (channelMap instanceof Graytone) newFragment = new GL20ResourceShaderFragmentGraytone(); else if (channelMap instanceof RGBColor) newFragment = new GL20ResourceShaderFragmentRGBColor();
            } else if (channelMap instanceof ChannelMapNode) {
                if (channelMap instanceof ChannelBlend) newFragment = new GL20ResourceShaderFragmentBlend();
            }
            if (newFragment != null) {
                newFragment.setChannelMap(channelMap);
                newFragment.update();
                returnValue = newFragment;
                singleton.fragmentStack.add(newFragment);
            }
        } else returnValue = singleton.fragmentStack.get(fragmentIndex);
        return returnValue;
    }
