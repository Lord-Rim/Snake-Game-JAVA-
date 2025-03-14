import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Myframe extends JFrame implements KeyListener, ActionListener {

    JLayeredPane layeredPane;

    JLabel[] player = new JLabel[290]; //player

    JLabel food,score,death_message, gameName;

    String score_str;
    int score_num=0;

    JPanel playArea,scoreBoard,death_screen, main_Menu;

    JButton playButton;

    int x=150,y=150,direction = 0,length = 1,pos[] = new int[600];

    //keeps track of food on the screen
    int isFoodThere = 0,food_pos[] = new int[2],foodScore=20;
    double foodTime=0;

    int size = 510,time=100; //size of all the screens and SnakeSpeed
    int move_allow = 0;//defined and initialized to counter a glitch

    boolean dead = true;

    ImageIcon head_upImg,head_rightImg,head_leftImg,head_downImg,bodyImg;


    Myframe(){

        head_upImg = new ImageIcon("src/head_up.png");
        head_downImg = new ImageIcon("src/head_down.png");
        head_rightImg = new ImageIcon("src/head_right.png");
        head_leftImg = new ImageIcon("src/head_left.png");

        bodyImg = new ImageIcon("src/body.png");

        Font scoreFont = new Font(Font.SERIF,Font.BOLD,30);
        Font deathFont = new Font(Font.SERIF,Font.BOLD,50);
        Font nameFont = new Font(Font.SERIF,Font.BOLD,40);
        Font buttonFont = new Font(Font.SERIF,Font.BOLD,50);

        player[0] = new JLabel();
        player[0].setOpaque(true);
        //player[0].setBackground(Color.RED);
        player[0].setIcon(head_upImg);
        player[0].setVisible(true);

        food = new JLabel();
        food.setOpaque(true);
        food.setBackground(Color.green);
        food.setVisible(true);

        score = new JLabel();
        score.setFont(scoreFont);
        score.setBounds(0,0,150,30);
        score.setText("Score: 0");
        score.setVisible(true);

        death_message = new JLabel();
        death_message.setFont(nameFont);
        death_message.setBounds(70,100,1000,100);
        death_message.setText("You died");
        death_message.setVisible(true);

        // Defining and initialising Main_menu

        gameName = new JLabel();
        gameName.setFont(deathFont);
        gameName.setBounds(30,100,1000,50);
        gameName.setText("THE SNAKE GAME");
        gameName.setForeground(Color.RED);
        gameName.setVisible(true);

        playButton = new JButton();
        playButton.setText("PLAY");
        playButton.setFont(buttonFont);
        playButton.setBounds(150,200,200,50);
        playButton.setFocusable(false);
        playButton.addActionListener(this);

        main_Menu = new JPanel();
        main_Menu.setBounds(0,0,size,size);
        main_Menu.setBackground(Color.BLACK);
        main_Menu.setLayout(null);
        main_Menu.setVisible(true);
        main_Menu.add(gameName);
        main_Menu.add(playButton);

        playArea = new JPanel();
        playArea.setBounds(0,0,size,size);
        playArea.setBackground(Color.black);
        playArea.setLayout(null);
        playArea.setVisible(false);
        playArea.add(player[0]);
        playArea.add(food);

        death_screen = new JPanel();
        death_screen.setBounds(0,0,size,size);
        death_screen.setBackground(Color.RED);
        death_screen.setLayout(null);
        death_screen.setVisible(false);
        death_screen.add(death_message);

        scoreBoard = new JPanel();
        scoreBoard.setBounds(380,10,150,30);
        scoreBoard.setBackground(Color.gray);
        scoreBoard.setLayout(null);
        scoreBoard.add(score);
        scoreBoard.setVisible(false);

        layeredPane = new JLayeredPane();
        layeredPane.setBounds(20,50,size,size);

        layeredPane.add(main_Menu,Integer.valueOf(0));
        layeredPane.add(playArea,Integer.valueOf(1));
        layeredPane.add(death_screen,Integer.valueOf(2));

        this.setTitle("SNAKE GAME");
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(size+50,size+100);
        this.addKeyListener(this);
        this.setLayout(null);
        this.setResizable(false);

        this.add(scoreBoard);
        this.add(layeredPane);

        this.setVisible(true);

        grow();

        while(true) {
            if (!dead) {
                if (direction == 0) {
                    y -= 30;
                    player[0].setIcon(head_upImg);
                }
                if (direction == 1) {
                    x += 30;
                    player[0].setIcon(head_rightImg);
                }
                if (direction == 2) {
                    x -= 30;
                    player[0].setIcon(head_leftImg);
                }
                if (direction == 3) {
                    y += 30;
                    player[0].setIcon(head_downImg);
                }
                if (isFoodThere == 0) {
                    food();
                    isFoodThere = 1;
                }
                move();
                if (move_allow == 0) {
                    move_allow = 1;
                }
                foodTime +=1;
                if (foodTime >= (double) 1000 /time){
                    foodTime = 0;
                    foodScore -=1;
                }
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("y");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //Checks to see of the User has pressed any keys
    @Override
    public void keyPressed(KeyEvent e) {
        //checks of the keys pressed are the ones needed
        if(e.getKeyChar() == 'w' || e.getKeyCode() == 38){
            if(direction !=3 && move_allow == 1){
                direction = 0;
                move_allow =0;
            }
        }
        if(e.getKeyChar() == 's' || e.getKeyCode() == 40){
            if(direction !=0 && move_allow == 1){
                direction = 3;
                move_allow =0;
            }
        }
        if(e.getKeyChar() == 'd' || e.getKeyCode() == 39){
            if(direction !=2 && move_allow == 1){
                direction = 1;
                move_allow =0;
            }
        }
        if(e.getKeyChar() == 'a' || e.getKeyCode() == 37){
            if(direction !=1 && move_allow == 1){
                direction = 2;
                move_allow =0;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    //Moves the snake 30 px infront(depending upon the direction)
    public void move(){
        death_check();
        for (int i = length*2; i >=0; i--) {
            pos[i+2] = pos[i];
        }
        if(x >= size){
            x=0;
        }
        else if(x < 0){
            x =size;
        }
        if(y >= size){
            y=0;
        }
        else if(y < 0){
            y =size;
        }
        pos[0] = x;
        pos[1] = y;

        for (int i = length; i >0; i--) {
            player[i].setLocation(player[i-1].getX(), player[i-1].getY());
            player[i].setVisible(true);
        }
        player[0].setBounds(pos[0],pos[1],30,30);

        food_check();
    }

    //Checks if the x and y coordinates of the head of the snake matches with any of the parts of the snake
    public void death_check(){
        String dmessage;
        for (int i = 0; i <= length*2; i =i+2) {
            if (x == pos[i]){
                if(y == pos[i+1]){
                    dead = true;
                    dmessage = "Your Score : "+score_num;
                    death_message.setText(dmessage);
                    try {
                        Thread.sleep(1000); // Simulate some work
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    playArea.setVisible(false);
                    death_screen.setVisible(true);
                    scoreBoard.setVisible(false);
                }
            }
        }
    }

    //Randomly generates the food particle at any part of the map
    // (This means the snake has encountered the body of the snake)
    public void food(){
        int num;
        food_pos[0] = (int)(Math.random() * size-10);
        num = (food_pos[0]/30);
        food_pos[0] = num*30;
        food_pos[1] = (int)(Math.random() * size-10);
        num = (food_pos[1]/30);
        food_pos[1] = num*30;
        food.setBounds(food_pos[0],food_pos[1],30,30);
    }

    //Checks if the X and Y coordinates of the food matches with the head of the snake
    // (this means snake has encountered the food)
    public void food_check(){
        if(x == food_pos[0]){
            if(y == food_pos[1]){
                isFoodThere = 0;
                length +=1;
                score_num +=foodScore;
                score_str = "Score: " + score_num;
                score.setText(score_str);
                foodScore = 20;
                grow();
            }
        }
    }

    //This grows the body of the snake by 1 (30px to 30px)
    public void grow(){

        player[length] = new JLabel();
        player[length].setOpaque(true);
        player[length].setIcon(bodyImg);
        player[length].setVisible(false);

        player[length].setBounds(length*2+1,length*2+2,30,30);
        playArea.add(player[length]);
    }

    //Checks to see if the play button is pressed
    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==playButton){
            main_Menu.setVisible(false);
            playArea.setVisible(true);
            scoreBoard.setVisible(true);
            dead = false;
        }

    }
}
