import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

public class bollonPop_stage2 extends GameEngine {
    public static void main(String[] args) {
        createGame(new bollonPop_stage2());
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
    ArrayList<Boolean> visible;
    public void initBollon(){
        num = 1;

        radius = new ArrayList<>();
        color = new ArrayList<>();
        positionX = new ArrayList<>();
        positionY = new ArrayList<>();
        speedY = new ArrayList<>();
        visible = new ArrayList<>();

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

        visible.add(i,true);
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
    // clicked
    //-------------------------------------------------------

    int numPop;
    ArrayList<Double> popPositionX;
    ArrayList<Double> popPositionY;
    ArrayList<Color> popColor;
    ArrayList<Double> popRadius;
    ArrayList<Double> popTimer;
    ArrayList<Double> popDuration;
    ArrayList<Boolean> popActive;
    public void initPop(){
        numPop = 0;
        popPositionX = new ArrayList<>();
        popPositionY = new ArrayList<>();
        popColor = new ArrayList<>();
        popRadius = new ArrayList<>();
        popTimer = new ArrayList<>();
        popDuration = new ArrayList<>();
        popActive = new ArrayList<>();
    }
    public void createPop(double x, double y,double radius,Color color,int i) {
        popPositionX.add(i,x);
        popPositionY.add(i,y);
        popRadius.add(i,radius);
        popColor.add(i,color);

        popTimer.add(i, (double) 0);
        popDuration.add(i,0.2);
        popActive.add(i,true);
    }

    // Function to update the explosion
    public void updateExplosion(double dt,int i) {
        // If the explosion is active
        if(popActive.get(i)) {
            // Increment timer
            double t = popTimer.get(i) + dt;
            popTimer.set(i,t);
            double r = popRadius.get(i) + 5;
            popRadius.set(i,r);

            // Check to see if explosion has finished
            if(popTimer.get(i) >= popDuration.get(i)) {
                popActive.set(i,false);
            }
        }
    }

    // Function to get frame of animation
    public int getAnimationFrame(double timer, double duration, int numFrames) {
        // Get frame
        int i = (int)floor(((timer % duration) / duration) * numFrames);
        // Check range
        if(i >= numFrames) {
            i = numFrames-1;
        }
        // Return
        return i;
    }

    // Function to draw the explosion
    public void drawExplosion(int i) {
        // Select the right image
        if(popActive.get(i)) {
            // Draw the explosion frame
            int j = getAnimationFrame(popTimer.get(i), popDuration.get(i), 30);
            changeColor(popColor.get(i));
            drawCircle(popPositionX.get(i),popPositionY.get(i),popRadius.get(i),5);
        }
    }

    //-------------------------------------------------------
    // Game
    //-------------------------------------------------------

    double speed;
    double time;
    double timeDelay;
    double timeAppear;
    Random random = new Random();
    AudioClip pop;
    @Override
    public void init() {
        setWindowSize(500,500);
        initBollon();
        initPop();
        pop = loadAudio("src//pop.wav");
    }

    @Override
    public void update(double dt) {
        timeDelay += dt;
        time += dt;
        speed = time / 10.0 + 1.0;

        updateBollon(dt);

        for(int i = 0;i < numPop;i++){
            updateExplosion(dt,i);
        }
    }

    @Override
    public void paintComponent() {
        changeBackgroundColor(black);
        clearBackground(width(),width());

        for (int i = 0; i < numPop;i++){
            if (popActive.get(i)) drawExplosion(i);
        }

        timeAppear = random.nextDouble(2) / speed;
        if(timeDelay > timeAppear){
            timeDelay = 0;
            createBollon(num);
            num++;
        }

        for(int i = 0; i < num;i++){
            if (visible.get(i)) drawBollon(i);
        }

    }
    //-------------------------------------------------------
    // Mouse functions
    //-------------------------------------------------------
    public void mouseClicked(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        for (int i = 0;i < num;i++){
            if (distance(x,y,positionX.get(i),positionY.get(i)) < radius.get(i) * 1.2){
                createPop(positionX.get(i),positionY.get(i),radius.get(i),color.get(i),numPop);
                numPop++;
                visible.set(i,false);
                playAudio(pop);

                for (int j = 0;j < num;j++){
                    if (j != i){
                        if(distance(x,y,positionX.get(j),positionY.get(j)) < radius.get(i) + radius.get(j)){
                            createPop(positionX.get(j),positionY.get(j),radius.get(j),color.get(j),numPop);
                            numPop++;
                            visible.set(j,false);
                        }
                    }
                }

                positionX.set(i, (double) 0);
                positionY.set(i, (double) 0);
            }
        }
    }
}
