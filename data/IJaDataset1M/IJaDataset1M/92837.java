package com.briansteen.example;

import java.awt.Color;
import org.sunflow.math.Point3;
import org.sunflow.math.Vector3;
import com.briansteen.SunflowAPIAPI;

public class FunkyScene {

    private SunflowAPIAPI sunflow = new SunflowAPIAPI();

    private int sceneWidth = 640;

    private int sceneHeight = 480;

    private int particleAmount = 5000;

    public FunkyScene() {
        sunflow.setWidth(sceneWidth);
        sunflow.setHeight(sceneHeight);
        sunflow.setThinlensCamera("thinLensCamera", 50f, (float) sceneWidth / sceneHeight, 0, 0, 7, 1, 0, 0);
        sunflow.setSunSkyLight("mySunskyLight");
        sunflow.setPointLight("myPointLight", new Point3(0, 5, 5), new Color(255, 255, 255));
        sunflow.setDirectionalLight("myDirectionalLight", new Point3(-2, 3, 0), new Vector3(0, 0, 0), 3, new Color(1f, 0f, 0f));
        sunflow.setSphereLight("mySphereLight", new Point3(0, 20, -5), new Color(0, 0, 255), 32, 10);
        int particleID = 0;
        for (int i = 0; i < particleAmount; i++) {
            float x = (float) Math.sin(i * .1f) * (i * .01f);
            float y = i * .02f;
            float z = (float) Math.cos(i * .5f) * (i * .05f);
            float colorR = (float) Math.abs(Math.atan(x));
            if (colorR < 0f) colorR = 0f;
            if (colorR > 1f) colorR = 1f;
            float colorG = y;
            if (colorG < 0f) colorG = 0f;
            if (colorG > 1f) colorG = 1f;
            float colorB = z;
            if (colorB < 0f) colorB = 0f;
            if (colorB > 1f) colorB = 1f;
            sunflow.setShinyDiffuseShader("myShinyDiffuseShader" + particleID, new Color(colorR, colorG, colorB), 2f);
            sunflow.drawSphere("sphere" + particleID++, x, y, z, .24f);
            x += (float) Math.cos(i * .5f) * (i * .015f);
            y += (float) Math.cos(i * .5f) * (i * .015f);
            z += (float) Math.cos(i * .5f) * (i * .015f);
            sunflow.setGlassShader("myGlassShader" + particleID, new Color(1f, 1f, 1f), 2.5f, 3f, new Color(1f, 1f, 1f));
            sunflow.drawSphere("sphere" + particleID++, x, y, z, .24f);
        }
        sunflow.setAmbientOcclusionEngine(new Color(1f, 1f, 1f), new Color(0, 0, 0), 132, 10f);
        sunflow.render();
    }

    public static void main(String args[]) {
        FunkyScene main = new FunkyScene();
    }
}
