    private static void license(File f) throws Exception {
        if (f.isDirectory()) {
            File fs[] = f.listFiles();
            for (File file : fs) {
                license(file);
            }
        } else if (f.getName().endsWith(".java")) {
            StringBuilder sb = new StringBuilder();
            sb.append("/* Copyright 2008 the original author or authors.\n");
            sb.append("*\n");
            sb.append("* Licensed under the Apache License, Version 2.0 (the \"License\");\n");
            sb.append("* you may not use this file except in compliance with the License.\n");
            sb.append("* You may obtain a copy of the License at\n");
            sb.append("*\n");
            sb.append("*      http://www.apache.org/licenses/LICENSE-2.0\n");
            sb.append("*\n");
            sb.append("* Unless required by applicable law or agreed to in writing, software\n");
            sb.append("* distributed under the License is distributed on an \"AS IS\" BASIS,\n");
            sb.append("* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n");
            sb.append("* See the License for the specific language governing permissions and\n");
            sb.append("* limitations under the License.\n");
            sb.append("*/\n");
            BufferedReader fr = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = fr.readLine()) != null) {
                sb.append(line + "\n");
            }
            fr.close();
            FileWriter fw = new FileWriter(f);
            fw.write(sb.toString());
            fw.close();
        }
    }
