import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Main {
    private static ArrayList<Beer> container = new ArrayList<>();
    public static String file_name;

    public static void add(String name, String style, String strength) {
        double d_strength = Double.parseDouble(strength);
        Beer elem = new Beer(name, style, d_strength);
        container.add(elem);
    }

    public static void list() {
        for (int i = 0; i < container.size(); i++) {
            System.out.println(container.get(i).toString());
        }
    }

    public static void load() throws IOException, ClassNotFoundException {
        FileInputStream f = new FileInputStream(file_name);
        //System.out.println(file_name);
        ObjectInputStream in = new ObjectInputStream(f);

        container = (ArrayList<Beer>) in.readObject();

        in.close();
    }

    public static void save(String doc) throws IOException {
        file_name = doc;

        FileOutputStream f = new FileOutputStream(doc);
        ObjectOutputStream out = new ObjectOutputStream(f);

        out.writeObject(container);
        out.close();
    }

    public static void list_name() {
        Collections.sort(container, new NameComparator());
        list();
    }

    public static void list_style() {
        Collections.sort(container, new StyleComparator());
        list();
    }

    public static void list_strength() {
        Collections.sort(container, new StrengthComparator());
        list();
    }

    public static void search(String s) {
        for (Beer beer : container) {
            if (beer.getName().equals(s)) {
                System.out.println(beer.toString());
            }
        }
    }

    public static void find(String s) {
        for (Beer beer : container) {
            if (beer.getName().contains(s)) {
                System.out.println(beer.toString());
            }
        }
    }

    public static void delete(String s) {
        Iterator<Beer> asd = container.iterator();
        while (asd.hasNext()) {
            if (asd.next().getName().equals(s)) {
                asd.remove();
            }
        }
    }

    public static void search_by_param(String type, String param) {
        for (Beer beer : container) {
            if ("name".equals(type) && beer.getName().contains(param)) {
                System.out.println(beer.toString());
            } else if ("style".equals(type) && beer.getStyle().contains(param)) {
                System.out.println(beer.toString());
            } else if ("strength".equals(type) && Double.parseDouble(param) == beer.getStrength()) {
                System.out.println(beer.toString());
            }
        }
    }

    public static void find_by_param(String type, String param) {
        for (Beer beer : container) {
            if ("name".equals(type) && beer.getName().contains(param)) {
                System.out.println(beer.toString());
            } else if ("style".equals(type) && beer.getStyle().contains(param)) {
                System.out.println(beer.toString());
            } else if ("strength".equals(type) && Double.parseDouble(param) <= beer.getStrength()) {
                System.out.println(beer.toString());
            } else if ("weaker".equals(type) && Double.parseDouble(param) >= beer.getStrength()) {
                System.out.println(beer.toString());
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Beer a1 = new Beer("Soproni", "Ipa", 4.2);
        Beer a2 = new Beer("Stalopramen", "Dark", 9.7);

        System.out.println(a1.toString());
        System.out.println(a2.toString());

        container.add(a1);
        container.add(a2);

        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader input = new BufferedReader(in);
        String a = input.readLine();
        String[] line;

        line = a.split(" ");

        System.out.println(line[0] + " " + line.length);

        while (!"exit".equals(line[0])) {
            a = input.readLine();
            line = a.split(" ");
            //System.out.println(line[0] + " " + line.length);
            if ("add".equals(line[0])) {
                add(line[1], line[2], line[3]);
            } else if ("list".equals(line[0])) {
                if (line.length > 1) {
                    if ("name".equals(line[1])) {
                        list_name();
                    } else if ("style".equals(line[1])) {
                        list_style();
                    } else if ("strenght".equals(line[1])) {
                        list_strength();
                    }
                } else {
                    list();
                }
            } else if ("load".equals(line[0])) {
                load();
            } else if ("save".equals(line[0])) {
                save(line[1]);
            } else if ("search".equals(line[0])) {
                if (line.length > 2) {
                    search_by_param(line[1], line[2]);
                } else {
                    search(line[1]);
                }

            } else if ("find".equals(line[0])) {
                if (line.length > 2) {
                    find_by_param(line[1], line[2]);
                } else {
                    find(line[1]);
                }

            } else if ("delete".equals(line[0])) {
                delete(line[1]);
            }
        }
    }
}