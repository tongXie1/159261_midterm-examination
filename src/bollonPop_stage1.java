import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class bollonPop_stage1 extends GameEngine{
    public static void main(String[] args) {
        createGame(new bollonPop_stage1());
    }

    //-------------------------------------------------------
    // bollon
    //-------------------------------------------------------
    int num;
    ArrayList<Integer> radius;
    ArrayList<Color> color;
    ArrayList<Double> positionX;
    ArrayList<Double> positionY;
    ArrayList<Double> speedY;
    public void initBollon(){
            num = 1;

            radius = new ArrayList<>();
            color = new ArrayList<>();
            positionX = new ArrayList<>();
            positionY = new ArrayList<>();
            speedY = new ArrayList<>();

            createBollon(0);
        }
        public void createBollon(int i){
            radius.add(i,20 + random.nextInt(20));
            if (random.nextInt(2) == 0) color.add(i,green);
            else color.add(i,red);

            int minX = radius.get(i);
            int maxX = width() - radius.get(i);
            positionX.add(i, random.nextDouble(maxX - minX + 1) + minX) ;
            positionY.add(i, (double) -radius.get(i));

            speedY.add(i,rand(100) + speed * 50);
        }
    public void updateBollon(double dt){
        for(int i = 0;i < num;i++){
            positionY.set(i, positionY.get(i) + speedY.get(i) * dt);
        }
    }
    public void drawBollon(int i){
        changeColor(color.get(i));
        drawSolidCircle(positionX.get(i),positionY.get(i),radius.get(i));
    }
    //-------------------------------------------------------
    // Game
    //-------------------------------------------------------

    double speed;
    double time;
    double timeDelay;
    double timeAppear;
    Random random = new Random();
    @Override
    public void init() {
        setWindowSize(500,500);
        initBollon();
    }

    @Override
    public void update(double dt) {
        timeDelay += dt;
        time += dt;
        speed = time / 10.0 + 1.0;

        updateBollon(dt);
    }

    @Override
    public void paintComponent() {
        changeBackgroundColor(black);
        clearBackground(width(),width());

        timeAppear = random.nextDouble(2) / speed;
        if(timeDelay > timeAppear){
            timeDelay = 0;
            createBollon(num);
            num++;
        }

        for(int i = 0; i < num;i++) drawBollon(i);
    }
}
