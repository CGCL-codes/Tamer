package rankia.downloader;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.jar.Pack200.Packer;
import java.util.jar.Pack200.Unpacker;

/**
 * Clase de ayuda para empaquetar y desempaquetar
 * Dado un directorio genera de los .jar los .jar.pack
 * y de los .jar.pack los .jar
 * No elimina ninguno y si ya existe el archivo destino se salta la operacion
 * 
 * 
 * @author Juan Miguel Albisu Frieyro
 *
 */
public class Pack200Helper {

    File baseDir;

    String packSuffix = ".pack";

    String jarSuffix = ".jar";

    FileFilter packfile = new Filter(packSuffix);

    FileFilter jarfile = new Filter(jarSuffix);

    Packer packer;

    Unpacker unpacker;

    boolean deep = true;

    public Pack200Helper(File baseDir, boolean deep) {
        this(baseDir);
        this.deep = deep;
    }

    public Pack200Helper(File baseDir) {
        this.baseDir = baseDir;
        packer = Pack200.newPacker();
        Map<String, String> map = packer.properties();
        map.put(Packer.EFFORT, "5");
        map.put(Packer.MODIFICATION_TIME, Packer.LATEST);
        map.put(Packer.UNKNOWN_ATTRIBUTE, Packer.ERROR);
        unpacker = Pack200.newUnpacker();
    }

    public void createPackFiles() throws IOException {
        packDir(baseDir);
    }

    private void packDir(File dir) throws IOException {
        File[] files = dir.listFiles(jarfile);
        for (File f : files) {
            if (f.isDirectory() && deep) packDir(f); else {
                File twin = new File(f.getParent(), f.getName() + packSuffix);
                if (twin.exists()) continue; else packFile(f);
            }
        }
    }

    private void packFile(File f) throws IOException {
        JarFile jarFile = new JarFile(f);
        FileOutputStream fos = new FileOutputStream(new File(f.getParent(), f.getName() + packSuffix));
        System.out.println("Packing " + f.getAbsolutePath());
        packer.pack(jarFile, fos);
        jarFile.close();
        fos.close();
    }

    public void createJarFiles() throws IOException {
        unpackDir(baseDir);
    }

    private void unpackDir(File dir) throws IOException {
        File[] files = dir.listFiles(packfile);
        for (File f : files) {
            if (f.isDirectory() && deep) unpackDir(f); else {
                String name = f.getName();
                name = name.substring(0, name.length() - packSuffix.length());
                File twin = new File(f.getParent(), name);
                if (twin.exists()) continue; else unpackFile(f);
            }
        }
    }

    private void unpackFile(File f) throws IOException {
        String name = f.getName();
        name = name.substring(0, name.length() - packSuffix.length());
        FileOutputStream fostream = new FileOutputStream(new File(f.getParent(), name));
        JarOutputStream jostream = new JarOutputStream(fostream);
        System.out.println("Unpacking " + f.getAbsolutePath());
        unpacker.unpack(f, jostream);
        jostream.close();
    }

    private class Filter implements FileFilter {

        String suffix;

        public Filter(String suffix) {
            this.suffix = suffix;
        }

        @Override
        public boolean accept(File f) {
            return (f.isDirectory() || f.getName().endsWith(suffix));
        }
    }
}
