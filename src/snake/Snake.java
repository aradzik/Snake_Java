package snake;

import java.awt.Color;
import javax.swing.JFrame;

public class Snake {

    public static void main(String[] args) {
       
       JFrame okno = new JFrame("SNAKE");
       Gra gra = new Gra();
       
       okno.setBounds(200, 10, 905, 700); // miejsce i rozmiar okna
       okno.setBackground(Color.BLACK);
       okno.setResizable(false); //zablokowanie powiekszenia
       okno.setVisible(true);
       okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       okno.add(gra);
    } 
}
