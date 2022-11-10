package org.example;

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

public class main
{
    public static int thread_number = 8;

    public static List<String> file_list = new ArrayList<>();
    //public static List<Func> func_list = new ArrayList<>();

    public static int partition_number = 1;
    public static float filter_score = 0.15f;
    public static float verify_score = 0.5f;
    public static float final_verify_score = 0.65f;
    public static String outputPath = "/root/data/htc_ast/data/65/15/result.csv";
    public static String outputPath2 = "/root/data/htc_ast/data/result10M.txt";
    public static String filedir = "/root/data/htc_ast/mydata/id2sourcecode";
    public static String filedir3 = "D:\\AST\\ngram\\src\\main\\resources\\testfile2";
    public static String filedir2 = "/root/data/nline_box/data/IJaDataset10M";
    public static String verifypath = "/root/data/nline_box/data/all_clone_pair.csv";
    public static String nonclonepath = "/root/data/nline_box/data/noclone-pair.csv";
    public static HashMap<String, Integer> string2char = new HashMap<>();
    public static HashMap<String, Integer> name_list = new HashMap<>();

    public static boolean readfile(String filepath) throws FileNotFoundException, IOException {
        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                file_list.add(file.getAbsolutePath());}

            else if (file.isDirectory()) {
                String[] filelist = file.list();
                //System.out.println(filelist);
                for (int i = 0; i < filelist.length; i++) {
                    //System.out.println(filepath + "/" + filelist[i]);
                    File readfile = new File(filepath + "/" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        file_list.add(readfile.getAbsolutePath());}
                    else if (readfile.isDirectory()) {
                        readfile(filepath + "/" + filelist[i]);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            //System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return true;
    }
    public static synchronized void updateInvertedIndex(Func func, HashMap<Integer, HashSet<Integer>> invertedBox)
    {
        var ngramHash = func.ngramHash;
        for (var hash : ngramHash) {
            if (invertedBox.containsKey(hash)) {
                invertedBox.get(hash).add(func.funcorder);
            }
            else {
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
        try {
            readfile(filedir);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath2));

        name_list.put("WhileStmt", 0);
        name_list.put("IfStmt", 1);
        name_list.put("SwitchStmt", 2);
        name_list.put("TryStmt", 3);
        name_list.put("ForEachStmt", 4);
        name_list.put("ThrowStmt", 5);
        name_list.put("ForStmt", 6);
        name_list.put("DoStmt", 7);
        name_list.put("SynchronizedStmt", 8);
        string2char.put("UnknownType", 0);
        string2char.put("CompilationUnit", 1);
        string2char.put("ClassOrInterfaceDeclaration", 2);
        string2char.put("MethodDeclaration", 3);
        string2char.put("ConstructorDeclaration", 4);
        string2char.put("Parameter", 5);
        string2char.put("ClassOrInterfaceType", 6);
        string2char.put("BlockStmt", 7);
        string2char.put("ExpressionStmt", 8);
        string2char.put("IfStmt", 9);
        string2char.put("ReturnStmt", 10);
        string2char.put("VariableDeclarationExpr", 11);
        string2char.put("UnaryExpr", 12);
        string2char.put("BinaryExpr", 13);
        string2char.put("NameExpr", 14);
        string2char.put("VariableDeclarator", 15);
        string2char.put("InstanceOfExpr", 16);
        string2char.put("SwitchStmt", 17);
        string2char.put("AssignExpr", 18);
        string2char.put("MethodCallExpr", 19);
        string2char.put("FieldAccessExpr", 20);
        string2char.put("SwitchEntry", 21);
        string2char.put("EnclosedExpr", 22);
        string2char.put("CastExpr", 23);
        string2char.put("ObjectCreationExpr", 24);
        string2char.put("TypeParameter", 25);
        string2char.put("ArrayType", 26);
        string2char.put("TryStmt", 27);
        string2char.put("ForEachStmt", 28);
        string2char.put("CatchClause", 29);
        string2char.put("WildcardType", 30);
        string2char.put("MarkerAnnotationExpr", 31);
        string2char.put("ArrayCreationExpr", 32);
        string2char.put("WhileStmt", 33);
        string2char.put("ThrowStmt", 34);
        string2char.put("ArrayCreationLevel", 35);
        string2char.put("ForStmt", 36);
        string2char.put("ArrayAccessExpr", 37);
        string2char.put("ClassExpr", 38);
        string2char.put("DoStmt", 39);
        string2char.put("ArrayInitializerExpr", 40);
        string2char.put("ExplicitConstructorInvocationStmt", 41);
        string2char.put("ConditionalExpr", 42);
        string2char.put("SingleMemberAnnotationExpr", 43);
        string2char.put("FieldDeclaration", 44);
        string2char.put("SynchronizedStmt", 45);
        string2char.put("AssertStmt", 46);
        string2char.put("InitializerDeclaration", 47);
        string2char.put("ThisExpr", 48);
        string2char.put("LabeledStmt", 49);
        string2char.put("BreakStmt", 50);
        string2char.put("ContinueStmt", 51);
        string2char.put("LocalClassDeclarationStmt", 52);
        string2char.put("SuperExpr", 53);
        string2char.put("NormalAnnotationExpr", 54);
        string2char.put("MemberValuePair", 55);
        string2char.put("Name", 56);
        string2char.put("LambdaExpr", 57);
        string2char.put("SimpleName", 58);
        string2char.put("Modifier", 59);
        string2char.put("PrimitiveType", 60);
        string2char.put("IntegerLiteralExpr", 61);
        string2char.put("NullLiteralExpr", 62);
        string2char.put("VoidType", 63);
        string2char.put("StringLiteralExpr", 64);
        string2char.put("BooleanLiteralExpr", 65);
        string2char.put("DoubleLiteralExpr", 66);
        string2char.put("CharLiteralExpr", 67);
        string2char.put("LongLiteralExpr", 68);
        string2char.put("EmptyStmt", 69);

        for (int kk = 1; kk <= 1; kk++) {
            bw.write("filter score is:" + filter_score);
            bw.newLine();
            bw.newLine();
            TaskList<Func> funcTaskList = new TaskList<>();
            HashMap<Integer, HashSet<Integer>> invertedBox = new HashMap<>();
            for (int nnn = 15; nnn >= 15; nnn -= 1) {
                //TaskList<String> parseTaskList = new TaskList<>(file_list);
                //file_list.clear();
                //System.out.println(parseTaskList.size());

                bw.flush();
                bw.write("This is ngram of " + nnn);
                bw.newLine();
                bw.flush();

                long startTime = System.currentTimeMillis();
                //System.out.println(file_list.size());
                int parseWorkload = file_list.size() / thread_number + (file_list.size() % thread_number != 0 ? 1 : 0);
                AtomicInteger countnumber = new AtomicInteger();
                int totalsize = file_list.size();
                //int current_index = 0;
                TaskList<String> parseTaskList = new TaskList<>(file_list);
                    /*for (int indexx = current_index; indexx <= current_index + 1000000 && indexx <file_list.size(); indexx++)
                    {
                        parseTaskList.addItem(file_list.get(indexx));
                    }*/
                //current_index += 1000000;
                ArrayList<Thread> parseThreadList = new ArrayList<>();
                for (int i = 0; i < thread_number; i++) {
                    var finalStartIndex = i * parseWorkload;
                    var finalStopIndex = Math.min((i + 1) * parseWorkload, file_list.size());
                    int finalNnn = nnn;
                    var thread = new Thread(() -> {
                        CompilationUnit cu1;
                        String fn = parseTaskList.getTask();
                        while (fn != null) {
                            try {
                                String code = readToString(fn);
                                int flag = 1;
                                if (flag == 0) {
                                    if (code.indexOf(" class ") != -1)
                                        code = code.substring(code.indexOf(" class ") + 1);
                                }
                                else
                                {code = "class _a {" + code + "}";}
                                cu1 = StaticJavaParser.parse(code);
                                Func newfun = new Func(fn, finalNnn, string2char, name_list, cu1);
                                cu1 = null;
                                newfun.funcorder = funcTaskList.size();
                                if (newfun.funcLen >= 50) {
                                    funcTaskList.addItem(newfun);
                                    updateInvertedIndex(newfun, invertedBox);
                                }
                                fn = parseTaskList.getTask();

                            } catch (Exception e) {
                                fn = parseTaskList.getTask();
                            }
                        }

                    });
                    thread.start();
                    parseThreadList.add(thread);
                }
                for (var thread : parseThreadList) {
                    try {
                        thread.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(funcTaskList.size());
                long endTime = System.currentTimeMillis();
                long parseTime = endTime - startTime;
                bw.write("Parse time: " + parseTime / 1000f);
                bw.flush();
                //System.out.println(funcTaskList.size());
                //System.out.println(invertedBox.size());
                long startTime_2 = System.currentTimeMillis();
                Map<Integer, HashSet<Integer>> clonePairs = new HashMap<>();
                AtomicLong totalClonePairsNum = new AtomicLong();
                var partitionSize = funcTaskList.size() / partition_number + (funcTaskList.size() % partition_number != 0 ? 1 : 0);
                for (int i = 0; i < partition_number; i++) {
                    var startIndex = i * partitionSize;
                    var stopIndex = Math.min((i + 1) * partitionSize, funcTaskList.size());
                    var totalWorkload = stopIndex - startIndex;
                    //var detectWorkload = totalWorkload / thread_number + (totalWorkload % thread_number != 0 ? 1 : 0);

                    ArrayList<Thread> detectThreadList = new ArrayList<>();
                    for (int j = 0; j < thread_number; j++) {
                        //var finalStartIndex = startIndex + j * detectWorkload;
                        //var finalStopIndex = Math.min(startIndex + (j + 1) * detectWorkload, stopIndex);
                        var thread = new Thread(() -> {
                            var funcC = funcTaskList.getTask();
                            while (funcC != null) {
                                HashSet<Integer> cloneCandidate = new HashSet<>();
                                HashSet<Integer> res = new HashSet<>();
                                int order = funcC.funcorder;
                                int i1 = funcTaskList.size();
                                int functimeslist[] = new int[i1 + 1];
                                for (int jk = 0; jk < i1 + 1; jk++)
                                    functimeslist[jk] = 0;
                                //System.out.println(functimeslist[5]);
                                //List<Integer> functimeslist = new ArrayList<>();
                                //for (int myconut = 0; myconut < i1; myconut++)
                                //functimeslist.add(0);

                                for (var onegramHash : funcC.ngramHash) {
                                    if (invertedBox.containsKey(onegramHash)) {
                                        for (var everyone : invertedBox.get(onegramHash))
                                        {
                                            functimeslist[everyone]++;

                                        }
                                    }
                                }
                                for (int newconut = 0; newconut < i1; newconut++) {
                                    if (functimeslist[newconut] >= 1 && newconut > order) {
                                        try {
                                            var funcB = funcTaskList.getItem(newconut);

                                            var nGramVerifyScore = Func.nLineVerify(funcC, funcB, invertedBox, functimeslist);
                                            //System.out.println(nGramVerifyScore);
                                            if (nGramVerifyScore >= verify_score) {
                                                res.add(funcB.funcId);

                                            } else if (nGramVerifyScore >= filter_score) {
                                                //System.out.println(nGramVerifyScore);
                                                var finalscore = funcC.Caculate_similarity_of_Func(funcB);
                                                //System.out.println(funcC.funcId + "   " + funcB.funcId + "   finalscore is:    " + finalscore);
                                                if (finalscore >= final_verify_score) {
                                                    res.add(funcB.funcId);

                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            //System.out.println(candidate);
                                            return;
                                        }
                                    }
                                }
                                if (!res.isEmpty()) {
                                    clonePairs.put(funcC.funcId, res);
                                }
                                funcC = funcTaskList.getTask();
                            }
                        });
                        thread.start();
                        detectThreadList.add(thread);
                    }
                    for (var thread : detectThreadList) {
                        try {
                            thread.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    long endTime_2 = System.currentTimeMillis();
                    long totalTime = endTime_2 - startTime_2;
                    System.out.println("Detection time: " + totalTime / 1000f);
                    bw.write("Totol time: " + (totalTime + parseTime) / 1000f);
                    bw.newLine();
                    File writeFile = new File(outputPath);
                    try {
                        BufferedWriter bw_2 = new BufferedWriter(new FileWriter(writeFile));
                        for (var entry : clonePairs.entrySet()) {
                            var funcBId = entry.getKey();
                            for (var funcCId : entry.getValue()) {
                                bw_2.write(funcBId + "," + funcCId);
                                bw_2.newLine();
                            }
                        }
                        bw_2.flush();
                        bw_2.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //System.out.println(clonePairs.size());
                }
            }
            filter_score -= 0.05f;
        }
        bw.close();
    }

}
