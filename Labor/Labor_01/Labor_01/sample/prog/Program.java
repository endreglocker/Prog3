package sample.prog;
import sample.echo.*; // minden osztaly importalasa a csomagbol
import sample.calc.Calculator; // a Calculator osztaly beimportalasa
public class Program
{
 public static void main(String[] args)
 {
 int x = 5;
 int y = 8;
 Calculator c = new Calculator();
 int sum = c.add(x, y);
 Echo e = new Echo();
 int sumEcho = e.number(sum);
 System.out.println("sumEcho=" + sumEcho);
 }
}