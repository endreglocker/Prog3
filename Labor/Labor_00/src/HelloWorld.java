public class HelloWorld {
    public static void main(String[] args) {
        //System.out.println("Hello World!");
        int[][] a = new int[4][7];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] = i + j;
            }
        }

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(a[i][j] + "    ");
            }
            System.out.println();
        }

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print((i + 1) + ":" + (j + 1) + "    ");
            }
            System.out.println();
        }
    }
}