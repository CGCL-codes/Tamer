

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import javax.security.auth.Destroyable;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class main {
    public static int thread_number = 1;

    public static List<String> file_list = new ArrayList<>();
    //public static List<Func> func_list = new ArrayList<>();

    public static int partition_number = 1;
    public static float filter_score = 0.15f;
    public static float verify_score = 0.5f;
    public static float final_verify_score = 0.65f;
    public static String outputPath = "/root/data/htc_ast/data/65/15/result.csv";
    public static String outputPath2 = "/root/data/htc_ast/data/result10M.txt";
    public static String outputPath3 = "/root/data/htc_ast/data/65/15/";
    public static String filedir = "/root/data/htc_ast/mydata/id2sourcecode";
    public static String filedir3 = "D:\\AST\\data\\IJaDataset100K\\IJaDataset100K";
    public static String filedir2 = "/root/data/nline_box/data/IJaDataset10M";
    public static String verifypath = "/root/data/nline_box/data/all_clone_pair.csv";
    public static String nonclonepath = "/root/data/nline_box/data/noclone-pair.csv";
    public static HashMap<String, Integer> string2char = new HashMap<>();
    public static HashMap<String, Integer> name_list = new HashMap<>();

    public static boolean readfile(String filepath) throws FileNotFoundException, IOException {
        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                file_list.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                //System.out.println(filelist);
                for (int i = 0; i < filelist.length; i++) {
                    //System.out.println(filepath + "/" + filelist[i]);
                    File readfile = new File(filepath + "/" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        file_list.add(readfile.getAbsolutePath());
                    } else if (readfile.isDirectory()) {
                        readfile(filepath + "/" + filelist[i]);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            //System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return true;
    }

    public static synchronized void updateInvertedIndex(Func func, HashMap<Integer, HashSet<Integer>> invertedBox) {
        var ngramHash = func.ngramHash;
        for (var hash : ngramHash) {
            if (invertedBox.containsKey(hash)) {
                invertedBox.get(hash).add(func.funcorder);
            } else {
                HashSet<Integer> hs = new HashSet<>();
                hs.add(func.funcorder);
                invertedBox.put(hash, hs);
            }
        }
    }

    private static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        for (int vol = 1; vol <= 100; vol++) {
            String dir1 = "D:\\AST\\data\\precision\\" + String.valueOf(vol) + "_a.java";
            System.out.println(dir1);
            File file1 = new File(dir1);
            if (!file1.exists())
                file1.createNewFile();
            String dir2 = "D:\\AST\\data\\precision\\" + String.valueOf(vol) + "_b.java";
            File file2 = new File(dir2);
            if (!file2.exists())
                file2.createNewFile();
        }
        /*
            Random r = new Random();
            int first = r.nextInt(limit_a);
            int first_id = order_list.indexOf(first);
            if (clonePairs.containsKey(first_id) == false)
                continue;
            int second = r.nextInt(clonePairs.get(first_id).size());
            Iterator it = clonePairs.get(first_id).iterator();
            int second_id =0;
            while (second > 0)
            {
                second_id = (Integer)it.next();
                second--;
            }
            String copy_1 = filedir + "\\" + String.valueOf(first_id) + ".java";
            File source1 = new File(copy_1);
            String copy_2 = filedir + "\\" + String.valueOf(second_id) + ".java";
            File source2 = new File(copy_2);
            copyFileUsingFileStreams(source1, file1);
            copyFileUsingFileStreams(source2, file2);*/
        }

    }




