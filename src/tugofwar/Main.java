package tugofwar;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {

    final static int screen_w = 800, screen_h = 150;

    public static void main(String[] args) {

        new Game();

    }

}

class Game extends JFrame implements KeyListener, Runnable {


    int x, y;
    double speedup = 1;
    int level = 1;
    int wincount = 0, losecount = 0;
    int ready = 1;
    boolean gamestart = true;
    Thread th;
    Toolkit tk = Toolkit.getDefaultToolkit();
    Image img = tk.getImage(Main.class.getResource("tug.png"));
    Image buffimg;
    Graphics buffg;

    public Game() {

        setVisible(true); // 프레임 화면에 노출
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(Main.screen_w, Main.screen_h);

        JOptionPane.showMessageDialog(null, "← 키를 눌러 시작하고, 줄을 당깁니다." , "게임 방법", JOptionPane.PLAIN_MESSAGE);
        init();
        start();

    }

    public void init(){

        x = 150;
        y = -50;
        gamestart = true;
        setTitle("줄다리기 2.0 | 현재 단계 : " + level + " | 현재 속도 : " + speedup);

    }

    public void start(){

        addKeyListener(this);
        th = new Thread(this);
        th.start();

    }

    public void paint(Graphics g){

        buffimg = createImage(Main.screen_w, Main.screen_h);
        buffg = buffimg.getGraphics();

        update(g);

    }

    public void update(Graphics g){

        Draw_Char();
        g.drawImage(buffimg, 0, 0, this);

    }

    public void Draw_Char(){

        buffg.clearRect(0, 0, Main.screen_w, Main.screen_h);
        buffg.drawImage(img, x, y, this);

    }

    public void check(){

        if(x < 0) {

            Play("win.wav");
            level += 1;
            speedup += 0.18;
            gamestart = false;
            wincount += 1;
            ready = 1;
            JOptionPane.showMessageDialog(null, "Player 승리!", "결과", JOptionPane.PLAIN_MESSAGE);
            init();

        }

        else if (x > 300) {

            Play("lose.wav");
            gamestart = false;
            losecount += 1;
            JOptionPane.showMessageDialog(null, "Com 승리!", "결과", JOptionPane.PLAIN_MESSAGE);
            JOptionPane.showMessageDialog(null, "당신의 총 전적은 "+wincount+"승 "+losecount+"패 입니다.", "게임 종료", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);

        }

    }

    public void right(){

        if(ready != 1)
            x += speedup;

    }

    public void Play(String fileName) {

        try {

            AudioInputStream ais = AudioSystem.getAudioInputStream(Main.class.getResource(fileName));
            Clip clip = AudioSystem.getClip();
            clip.stop();
            clip.open(ais);
            clip.start();

        } catch (Exception err) {

            System.out.println(err.getMessage());

        }

    }





    @Override
    public void run() {

        try{

            do{

                right();
                check();
                repaint();
                Thread.sleep(20);

            } while (gamestart);

        } catch (Exception err) {

            System.out.println(err.getMessage());

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

        // 키보드가 타이핑 될 때 이벤트 처리

    }

    @Override
    public void keyPressed(KeyEvent e) {

        // 키보드가 눌렸을 때 이벤트 처리

    }

    @Override
    public void keyReleased(KeyEvent e) {

        // 키보드가 눌렸다가 떼어졌을 때 이벤트 처리

        int KeyCode = e.getKeyCode();

        if(KeyCode == KeyEvent.VK_LEFT) {

            if (ready == 1)

                ready = 0;

            x -= 10;

        }

    }

}