package net.bpfurtado.tas.builder.scenespanel;

import java.util.Collection;
import net.bpfurtado.tas.model.Scene;

public interface ScenesSource {

    Collection<Scene> getScenes();

    void switchTo(Scene selectedScene, int selectedSceneIdx);
}
