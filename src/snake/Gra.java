package snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Gra extends JPanel implements ActionListener, KeyListener {

    private final Timer timer;
    private int delay;//szybkosc węża

    private final int[] SnakeX = new int[750]; //pozycja x snake
    private final int[] SnakeY = new int[750]; //pozycja y snake

    //mozliwe wspolrzedne myszki
    private final int[] eatX = {50, 75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500, 525, 550, 575, 600, 625, 650, 675, 700, 725, 750, 775, 800, 825};
    private final int[] eatY = {100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500, 525, 550, 575, 600};

    private boolean lewa = false;
    private boolean prawa = false;
    private boolean gora = false;
    private boolean dol = false;

    private ImageIcon titleGame;
    private ImageIcon wLewo;
    private ImageIcon wPrawo;
    private ImageIcon wDol;
    private ImageIcon wGore;
    private ImageIcon cialo;
    private ImageIcon jedzenie;
    private ImageIcon woda;

    private int dlugosc = 4; //poczatkowa dlugosc weza

    private int ruch = 0;
    private int gameover = 1;
    private int wynik = 0;
    boolean rysujTekst = false;

    //losowanie pozycji jedzenia
    private final Random losuj = new Random();
    private int xeatLos = losuj.nextInt(32); //32 poniewaz tyle elementw ma tablica eatX
    private int yeatLos = losuj.nextInt(21); //21 poniewaz tyle elementw ma tablica eatY

    public Gra() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }
    public void paint(Graphics g) {
        if (gameover == 0) { //pole do gry
            //ustawiamy gdzie ma byc wąż na poczatku gry
            if (ruch == 0) {
                SnakeX[0] = 425;//to jest glowa
                SnakeX[1] = 400;
                SnakeX[2] = 375;
                SnakeX[3] = 350;
                //roznica 25 bo obrazek jest 25x25
                SnakeY[0] = 300;//glowa
                SnakeY[1] = 300;
                SnakeY[2] = 300;
                SnakeY[3] = 300;
            }
            //tło gry
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 905, 700);
            //wstawienie tytulu gry
            titleGame = new ImageIcon("./title.png");
            titleGame.paintIcon(this, g, 25, 11);
            //plansza
            g.setColor(Color.WHITE);
            g.drawRect(24, 74, 851, 576);
            //pole
            g.setColor(Color.getHSBColor(39, 27, 77));
            g.fillRect(25, 75, 850, 575);
            //rysowanie wody
            woda = new ImageIcon("./woda.png");
            for (int i = 25; i <= 851; i += 25) {
                woda.paintIcon(this, g, i, 74);
            }
            for (int i = 25; i <= 851; i += 25) {
                woda.paintIcon(this, g, i, 626);
            }
            for (int i = 75; i <= 625; i += 25) {
                woda.paintIcon(this, g, 25, i);
            }
            for (int i = 75; i <= 625; i += 25) {
                woda.paintIcon(this, g, 850, i);
            }
            //rysowanie snake
            wPrawo = new ImageIcon("./right.png");
            wPrawo.paintIcon(this, g, SnakeX[0], SnakeY[0]);
            //wyswietlanie wyniku
            g.setColor(Color.red);
            g.setFont(new Font("./arial", Font.PLAIN, 16));
            g.drawString("Wynik :" + wynik, 780, 30);
            //ustawienie odpowiedniego obrazka z glowa do kierunku w ktorym pełza
            for (int i = 0; i < dlugosc; i++) {
                if (i == 0 && prawa) {
                    wPrawo = new ImageIcon("./right.png");
                    wPrawo.paintIcon(this, g, SnakeX[i], SnakeY[i]);
                }
                if (i == 0 && lewa) {
                    wLewo = new ImageIcon("./left.png");
                    wLewo.paintIcon(this, g, SnakeX[i], SnakeY[i]);
                }
                if (i == 0 && dol) {
                    wDol = new ImageIcon("./down.png");
                    wDol.paintIcon(this, g, SnakeX[i], SnakeY[i]);
                }
                if (i == 0 && gora) {
                    wGore = new ImageIcon("./up.png");
                    wGore.paintIcon(this, g, SnakeX[i], SnakeY[i]);
                }
                //dorysowywanie ciala do glowy
                if (i != 0) {
                    cialo = new ImageIcon("./snake.png");
                    cialo.paintIcon(this, g, SnakeX[i], SnakeY[i]);
                }
            }
            //losowanie i narysowanie miejsca myszy
            jedzenie = new ImageIcon("./eat.png");
            if (eatX[xeatLos] == SnakeX[0] && eatY[yeatLos] == SnakeY[0]) {
                dlugosc++;
                xeatLos = losuj.nextInt(32);
                yeatLos = losuj.nextInt(21);
                wynik = wynik + 15;
            }
            jedzenie.paintIcon(this, g, eatX[xeatLos], eatY[yeatLos]);
            //kolizja z samym sobą
            for (int b = 1; b < dlugosc; b++) {
                if (SnakeX[b] == SnakeX[0] && SnakeY[b] == SnakeY[0]) {
                    timer.stop();
                    lewa = false;
                    prawa = false;
                    gora = false;
                    dol = false;
                    ruch = 0;
                    dlugosc = 4;
                    gameover = 1;
                    rysujTekst = true;
                    repaint();
                }
            }
            if( dlugosc == 671){ //wygrana
                g.setColor(Color.BLACK);
            g.fillRect(0, 0, 905, 700);
            titleGame = new ImageIcon("./title.png");
            titleGame.paintIcon(this, g, 24, 11);
            g.setColor(Color.white);
            g.setFont(new Font("arial", Font.PLAIN, 40));
            g.drawString("Wygrałes!!!!!", 370, 300);
            }
        }
        //menu       
        if (gameover == 1) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 905, 700);
            titleGame = new ImageIcon("./title.png");
            titleGame.paintIcon(this, g, 24, 11);
            g.setColor(Color.white);
            g.setFont(new Font("arial", Font.PLAIN, 40));
            g.drawString("MENU", 370, 300);
            g.setFont(new Font("arial", Font.PLAIN, 35));
            g.drawString("Naciśnij klawisz aby wybrać poziom i przejść do gry.", 50, 340);
            g.drawString("1- łatwy", 370, 380);
            g.drawString("2- średni", 365, 420);
            g.drawString("3-  trudny", 360, 460);
            g.setFont(new Font("arial", Font.PLAIN, 20));
            g.drawString("Pomoc:    ESC- wyjście      SPACE- restart", 250, 550);

            if (rysujTekst) {
                g.setColor(Color.BLACK);
                g.fillRect(24, 74, 852, 577);
                g.setColor(Color.white);
                g.setFont(new Font("arial", Font.PLAIN, 40));
                g.drawString("GAME OVER!", 320, 300);
                g.drawString("Twój wynik to: " + wynik, 300, 340);
            }
            //odliczanie zeby zmienilo sie z game over po 2sek na menu
            Timer timer = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    rysujTekst = false;
                    repaint();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
        g.dispose();
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) { //klawisze

        if (e.getKeyCode() == KeyEvent.VK_SPACE) { //reset
            wynik = 0;
            lewa = false;
            prawa = false;
            gora = false;
            dol = false;
            ruch = 0;
            dlugosc = 4;
        }
        if (e.getKeyCode() == KeyEvent.VK_1) { //poziom łatwy
             delay = 400;
            timer.setDelay(delay);
            gameover = 0;
            wynik = 0;
            lewa = false;
            prawa = false;
            gora = false;
            dol = false;
            ruch = 0;
            dlugosc = 4;
        }
        if (e.getKeyCode() == KeyEvent.VK_2) { //poziom sredni
            delay = 170;
            timer.setDelay(delay);
            gameover = 0;
            wynik = 0;
            lewa = false;
            prawa = false;
            gora = false;
            dol = false;
            ruch = 0;
            dlugosc = 4;
        }
        if (e.getKeyCode() == KeyEvent.VK_3) { //poziom trudny
            delay = 50;
            timer.setDelay(delay);
            gameover = 0;
            wynik = 0;
            lewa = false;
            prawa = false;
            gora = false;
            dol = false;
            ruch = 0;
            dlugosc = 4;
        }
        //pauza lub zakonczenie
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            timer.stop();
            int reply = JOptionPane.showConfirmDialog(null, "Czy chcesz zakończyć grę?", "", JOptionPane.YES_NO_OPTION);

            if (reply == JOptionPane.YES_OPTION) {

                System.exit(0);
            } else {
                timer.start();
            }
        }
        //poruszanie
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            ruch++;
            prawa = true;
            if (!lewa) {           //warunek ktory zachowuje to ze jesli wąż idzie w prawą 
                prawa = true;   //strone to nie moze isc w lewą ( jesli nie idzie w lewa(lewa!=true) to idzie w prawa( prawa = true)
            } else {            //dalej analogicznie
                prawa = false;
                lewa = true;
            }
            gora = false;
            dol = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            ruch++;
            lewa = true;
            if (!prawa) {
                lewa = true;
            } else {
                lewa = false;
                prawa = true;
            }
            gora = false;
            dol = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            ruch++;
            gora = true;
            if (!dol) {
                gora = true;
            } else {
                gora = false;
                dol = true;
            }
            lewa = false;
            prawa = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            ruch++;
            dol = true;
            if (!gora) {
                dol = true;
            } else {
                dol = false;
                gora = true;
            }
            lewa = false;
            prawa = false;
        }   
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void actionPerformed(ActionEvent e) {
        requestFocusInWindow();
        timer.start();
        //poruszanie sie
        if (prawa) { // jest prawa jest true
            for (int r = dlugosc - 1; r >= 0; r--) {
                SnakeY[r + 1] = SnakeY[r];
            }
            for (int r = dlugosc; r >= 0; r--) {
                if (r == 0) {
                    SnakeX[r] = SnakeX[r] + 25;
                } else {
                    SnakeX[r] = SnakeX[r - 1];
                }
                if (SnakeX[r] > 825) { // wejscie na teren wody, dalej analogicznie
                    lewa = false;
                    prawa = false;
                    gora = false;
                    dol = false;
                    ruch = 0;
                    dlugosc = 4;
                    gameover = 1;
                    rysujTekst = true;
                    repaint();
                }
            }
        }
        if (lewa) {
            for (int r = dlugosc - 1; r >= 0; r--) {
                SnakeY[r + 1] = SnakeY[r];
            }
            for (int r = dlugosc; r >= 0; r--) {
                if (r == 0) {
                    SnakeX[r] = SnakeX[r] - 25;
                } else {
                    SnakeX[r] = SnakeX[r - 1];
                }
                if (SnakeX[r] < 50) {
                    lewa = false;
                    prawa = false;
                    gora = false;
                    dol = false;
                    ruch = 0;
                    dlugosc = 4;
                    gameover = 1;
                    rysujTekst = true;
                    repaint();
                }
            }
        }
        if (gora) {
            for (int r = dlugosc - 1; r >= 0; r--) {
                SnakeX[r + 1] = SnakeX[r];
            }
            for (int r = dlugosc; r >= 0; r--) {
                if (r == 0) {
                    SnakeY[r] = SnakeY[r] - 25;
                } else {
                    SnakeY[r] = SnakeY[r - 1];
                }
                if (SnakeY[r] < 100) {
                    lewa = false;
                    prawa = false;
                    gora = false;
                    dol = false;
                    ruch = 0;
                    dlugosc = 4;
                    gameover = 1;
                    rysujTekst = true;
                    repaint();
                }
            }
        }
        if (dol) {
            for (int r = dlugosc - 1; r >= 0; r--) {
                SnakeX[r + 1] = SnakeX[r];
            }
            for (int r = dlugosc; r >= 0; r--) {
                if (r == 0) {
                    SnakeY[r] = SnakeY[r] + 25;
                } else {
                    SnakeY[r] = SnakeY[r - 1];
                }
                if (SnakeY[r] > 600) {
                    lewa = false;
                    prawa = false;
                    gora = false;
                    dol = false;
                    ruch = 0;
                    dlugosc = 4;
                    gameover = 1;
                    rysujTekst = true;
                    repaint();
                }
            }
        }
        repaint();
    }
}