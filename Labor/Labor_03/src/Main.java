import java.io.*;

import static java.lang.System.in;


public class Main {

    private static File wd = new File(System.getProperty("user.dir"));

    protected static String print_work_directory() {
        try {
            return wd.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //+ "\nNumber of files: " + wd.listFiles().length;
    }

    protected static void ls(char ch) {
        File[] sub_directories = wd.listFiles();
        if (ch == '-') {
            for (int i = 0; i < sub_directories.length; i++) {
                System.out.println(sub_directories[i]);
            }
        } else if (ch == 'd') { // TODO direcories
            for (int i = 0; i < sub_directories.length; i++) {
                if (!(sub_directories[i].isFile())) {
                    System.out.println(sub_directories[i]);
                }
            }
        } else if (ch == 'l') { // TODO listing with type and size
            for (int i = 0; i < sub_directories.length; i++) {
                System.out.print(sub_directories[i] + "\t" + sub_directories[i].getTotalSpace() + "\t");

                if (!(sub_directories[i].isFile())) {
                    System.out.println("Directory");
                }
                if (sub_directories[i].isFile()) {
                    System.out.println("File");
                }
            }
        } else if (ch == 'f') { // TODO executables?
            for (int i = 0; i < sub_directories.length; i++) {
                if (sub_directories[i].isFile()) {
                    System.out.println(sub_directories[i]);
                }
            }
        }
    }

    protected static void cd(String s) {
        if (s.equals("..")) {
            wd = wd.getParentFile();
        } else {
            File f = new File(wd, s);
            if (!(f.isDirectory())) {
                System.out.println("Nemletezo mappa");
                return;
            }
            wd = f;
        }
    }

    protected static void rm(String s) {
        File f = new File(wd, s);
        boolean ok = f.delete();
        if (!ok) {
            System.out.println("Nemsikerult torolni!");
        }
    }

    protected static void mkdir(String s) {
        File f = new File(wd, s);
        boolean ok = f.mkdir();
        if (!ok) {
            System.out.println("Nemsikerult!");
        }
    }

    protected static void mv(String s1, String s2) {
        File f1 = new File(wd, s1);
        File f2 = new File(wd, s2);
        boolean ok = f1.renameTo(f2);
        if (!ok) {
            System.out.println("Nemsikerult!");
        }
    }

    protected static void cp(String s1, String s2) {
        //TODO fr - opens s1, br - reads the text line by line
        FileReader fr = null;
        try {
            fr = new FileReader(s1);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br = new BufferedReader(fr);

        //TODO fw - opens s2 pw - prints the line into s2
        FileWriter fw = null;
        try {
            fw = new FileWriter(s2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PrintWriter pw = new PrintWriter(fw);

        //TODO reads the file line by line and write it to s2, until EOF
        while (true) {
            String line = null;
            try {
                line = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) break;
            pw.println(line);
        }
        try {
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pw.close();
    }

    protected static void cat(String s1) {
        FileReader fr = null;
        try {
            fr = new FileReader(s1);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br = new BufferedReader(fr);
        while (true) {
            String line = null;
            try {
                line = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) break;
            System.out.println(line);
        }
        try {
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String intro;
        String[] split_intro;

        //TODO the actual solution
        while (true) {
            try {
                intro = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            split_intro = intro.split(" ");

            if (split_intro[0].equals("exit")) {
                System.exit(2022);
            }
            if (split_intro[0].equals("pwd")) {
                System.out.println(print_work_directory());
            }

            if (split_intro[0].equals("cd")) {
                if (split_intro.length == 1) {
                    System.out.println("Hibas atributumok!");
                } else {
                    if (split_intro[1].equals("..")) {
                        cd(split_intro[1]);
                    } else {
                        cd(split_intro[1]);
                    }
                }
            }

            if (split_intro[0].equals("ls")) {
                if (split_intro.length > 1) {
                    if (split_intro[1].equals("-l")) {
                        ls('l');
                    } else if (split_intro[1].equals("-d")) {
                        ls('d');
                    } else if (split_intro[1].equals("-f")) {
                        ls('f');
                    }
                } else {
                    ls('-');
                }
            }

            if (split_intro[0].equals("rm")) {
                rm(split_intro[1]);
            }

            if (split_intro[0].equals("mkdir")) {
                mkdir(split_intro[1]);
            }

            if (split_intro[0].equals("mv")) {
                mv(split_intro[1], split_intro[2]);
            }

            if (split_intro[0].equals("cp")) {
                cp(split_intro[1], split_intro[2]);
            }

            if (split_intro[0].equals("cat")) {
                cat(split_intro[1]);
            }

        }
    }
}

///TODO introduction task
/**
 * intro = reader.readLine();
 * split_intro = intro.split(" ");
 * <p>
 * for (int i = 0; i < split_intro.length; i++) {
 * System.out.println(split_intro[i]);
 * }
 * <p>
 * try {
 * split_intro[0] = reader.readLine();
 * } catch (IOException e) {
 * throw new RuntimeException(e);
 * }
 * while (!split_intro[0].equals("exit")){
 * try {
 * split_intro[0] = reader.readLine();
 * } catch (IOException e) {
 * throw new RuntimeException(e);
 * }
 * }
 * <p>
 * try {
 * split_intro[0] = reader.readLine();
 * } catch (IOException e) {
 * throw new RuntimeException(e);
 * }
 * while (!split_intro[0].equals("exit")){
 * try {
 * split_intro[0] = reader.readLine();
 * } catch (IOException e) {
 * throw new RuntimeException(e);
 * }
 * }
 * <p>
 * try {
 * split_intro[0] = reader.readLine();
 * } catch (IOException e) {
 * throw new RuntimeException(e);
 * }
 * while (!split_intro[0].equals("exit")){
 * try {
 * split_intro[0] = reader.readLine();
 * } catch (IOException e) {
 * throw new RuntimeException(e);
 * }
 * }
 */

//TODO an alternative solution for the problem
/**
 try {
 split_intro[0] = reader.readLine();
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 while (!split_intro[0].equals("exit")){
 try {
 split_intro[0] = reader.readLine();
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }
 */