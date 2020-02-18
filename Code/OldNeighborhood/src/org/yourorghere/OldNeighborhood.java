package org.yourorghere;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

/**
 * An Old residental area
 * author:
 *  - Anfal Al-Atawi
 *  - Najwa Noorwali
 *  - Razan Sonbul
 *  - Nouf Al-Kediwi
 *  - Rahaf Al-Motary
 *  - Bayader Al-Sahafi
 */
public class OldNeighborhood implements GLEventListener, KeyListener {

    public static Texture Beglah, Qurtasiah, Mathanah, Mugawil, school, flowers, schoolSpray, doodles, spray;
    static float angle = 0.0f, moveForward = 5, moveLeft = -8;
    static boolean move = true , stopRotating = false;
    // actual vector representing the camera's direction
    static float cameraX = 0f, cameraY = 3f, cameraZ = 7;
    // position of the camera
    static float locationX = 0, locationY = 4, locationZ = -5;

    // for the fan:
    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new OldNeighborhood());
        frame.add(canvas);
        frame.setSize(640, 480);

        // Detecting Pressed Keys ------------------------------------------------------
        KeyListener listener = new KeyListener() {

            @Override

            public void keyPressed(KeyEvent event) {

                System.out.println("KEY EVENT ENTERED.");
                // Locations ----------------------
                if (event.getKeyCode() == KeyEvent.VK_LEFT) {
                    System.out.println("KEY EVENT LEFT.");
                    locationX--;
                }
                if (event.getKeyCode() == KeyEvent.VK_DOWN) {
                    System.out.println("KEY EVENT DOWN.");
                    locationY--;
                }
                if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
                    System.out.println("KEY EVENT RIGHT.");
                    locationX++;
                }
                if (event.getKeyCode() == KeyEvent.VK_UP) {
                    System.out.println("KEY EVENT UP.");
                    locationY++;
                }

                // Locations ----------------------
                if (event.getKeyCode() == KeyEvent.VK_A) {
                    System.out.println("KEY EVENT A.");
                    cameraX--;
                }
                if (event.getKeyCode() == KeyEvent.VK_S) {
                    System.out.println("KEY EVENT S.");
                    cameraZ--;
                }
                if (event.getKeyCode() == KeyEvent.VK_D) {
                    System.out.println("KEY EVENT D.");
                    cameraX++;
                }
                if (event.getKeyCode() == KeyEvent.VK_W) {
                    System.out.println("KEY EVENT W.");
                    cameraZ++;
                }

            }

            @Override

            public void keyReleased(KeyEvent event) {

                //printEventInfo("Key Released", event);
            }

            @Override

            public void keyTyped(KeyEvent event) {

                printEventInfo("Key Typed", event);

            }

            private void printEventInfo(String str, KeyEvent e) {

                System.out.println(str);

                int code = e.getKeyCode();

                System.out.println("   Code: " + KeyEvent.getKeyText(code));

                System.out.println("   Char: " + e.getKeyChar());

                int mods = e.getModifiersEx();

                System.out.println("    Mods: "
                        + KeyEvent.getModifiersExText(mods));

                System.out.println("    Location: "
                        + keyboardLocation(e.getKeyLocation()));

                System.out.println("    Action? " + e.isActionKey());

            }

            private String keyboardLocation(int keybrd) {

                switch (keybrd) {

                    case KeyEvent.KEY_LOCATION_RIGHT:

                        return "Right";

                    case KeyEvent.KEY_LOCATION_LEFT:

                        return "Left";

                    case KeyEvent.KEY_LOCATION_NUMPAD:

                        return "NumPad";

                    case KeyEvent.KEY_LOCATION_STANDARD:

                        return "Standard";

                    case KeyEvent.KEY_LOCATION_UNKNOWN:

                    default:

                        return "Unknown";

                }

            }

        };
        // End of Detecting Pressed Keys ----------------------------------------------------
        canvas.addKeyListener(listener);

        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        try {
            // loading the texture:
            Qurtasiah = TextureIO.newTexture(new File("q.jpg"), true);
            Mathanah = TextureIO.newTexture(new File("am.jpg"), true);
            Beglah = TextureIO.newTexture(new File("b.jpg"), true);
            Mugawil = TextureIO.newTexture(new File("Mugawil.png"), true);
            school = TextureIO.newTexture(new File("school.jpg"), true);
            flowers = TextureIO.newTexture(new File("f.jpg"), true);
            schoolSpray = TextureIO.newTexture(new File("FaceSpray.jpg"), true);
            doodles = TextureIO.newTexture(new File("Doodles.jpg"), true);
            spray = TextureIO.newTexture(new File("ssp.jpg"), true);
        } catch (IOException ex) {
            System.err.println(ex);
        }

        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(148 / 255f, 214 / 255f, 1f, 0.5f);
   
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!

            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    /*
    This method is where all the objects are called
    */
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        GLUT glut = new GLUT();
        GLU glu = new GLU();

        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        // Reset the current matrix to the "identity"
        gl.glLoadIdentity();

        // Set the camera
        glu.gluLookAt(cameraX, cameraY, cameraZ, //  position of your camera
                locationX, locationY, locationZ, // where the camera is looking at
                0.0f, 1.0f, 0.0f); // tilt

        // Lighting 
        // Turning on the light comonents
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
        gl.glEnable(GL.GL_NORMALIZE);
        gl.glShadeModel(GL.GL_SMOOTH);
        gl.glEnable(GL.GL_COLOR_MATERIAL);

        // Setting different light parameters
        float[] ambientLight = {0.5f, 0.5f, 0.5f, 0f};  // weak white ambient 
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0);

        float[] diffuseLight = {1f, 1f, 1f, 3f};  // multicolor diffuse 
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0);

        float[] positionLight = {-3f, 2f, 0f};  // coordinates of the light
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, positionLight, 0);

        float[] specularLight = {1.0f, 1.0f, 1.0f, 1.0f}; // intensety of the shine
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specularLight, 0);

        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPOT_DIRECTION, specularLight, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPOT_EXPONENT, specularLight, 0);
        
        // Drawing 
        // Move the "drawing cursor" around

        gl.glEnable(GL_BLEND);
        gl.glEnable(GL_DEPTH_TEST);
        
        // Draw axis 
//        gl.glColor3f(0, 1, 0);
//        gl.glBegin(GL.GL_LINES);
//        gl.glVertex3f(0, 5, 0);
//        gl.glVertex3f(0, -5, 0);
//        gl.glVertex3f(-5, 0, 0);
//        gl.glVertex3f(5, 0, 0);
//        gl.glEnd();
        
        gl.glTranslatef(0f, 0f, -6.0f);

        gl.glScalef(0.5f, 0.5f, 0.5f);

        gl.glPushMatrix();
        
        // Ground (gray)
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, 0f);
        gl.glColor3f(139 / 255f, 139 / 255f, 139 / 255f);
        gl.glScalef(25, 0.5f, 20);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // The park in the front (Barha)
        gl.glPushMatrix();
        gl.glTranslatef(-3.3f, 0.27f, 7f);
        gl.glColor3f(196 / 255f, 168 / 255f, 141 / 255f);
        gl.glScalef(8f, 0f, 5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        
        // The park in the back (Barha)
        gl.glPushMatrix();
        gl.glTranslatef(2.5f, 0.27f, -5f);
        gl.glColor3f(196 / 255f, 168 / 255f, 141 / 255f);
        gl.glScalef(6f, 0f, 5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        
        // unfinished building ground
        gl.glPushMatrix();
        gl.glTranslatef(9f, 0.27f, -6f);
        gl.glColor3f(196 / 255f, 168 / 255f, 141 / 255f);
        gl.glScalef(4f, 0f, 5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Objects of the front park 
        gl.glPushMatrix();
        gl.glTranslatef(-6.3f, 0.26f, 5f);
        gl.glScalef(0.6f, 0.6f, 0.6f);
        park(gl, glut);
        gl.glPopMatrix();

        //Trash in the front
        gl.glPushMatrix();
        gl.glTranslatef(0f, 1f, 5.7f);
        old_bin(gl, glut);
        gl.glPopMatrix();
        
        //Trash in the back
        gl.glPushMatrix();
        gl.glTranslatef(19f, 1f, -20f);
        gl.glRotatef(160, 0, 1, 0);
        old_bin(gl, glut);
        gl.glPopMatrix();

        // rocks
        gl.glPushMatrix();
        gl.glTranslatef(5f, -3f, 3f);
        gl.glScalef(2f, 2f, 2f);
        rock(gl, glut);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-3f, -3f, 7f);
        gl.glScalef(2f, 2f, 2f);
        rock(gl, glut);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-4f, -3f, 7f);
        gl.glScalef(2f, 2f, 2f);
        rock(gl, glut);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-3.5f, -3f, 7f);
        gl.glScalef(2f, 2f, 2f);
        rock(gl, glut);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -3f, 7f);
        gl.glScalef(2f, 2f, 2f);
        rock(gl, glut);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -2.95f, 6.2f);
        gl.glScalef(2f, 2f, 2f);
        rock(gl, glut);
        gl.glPopMatrix();

        // masjid (front)
        gl.glPushMatrix();
        gl.glTranslatef(-2.5f, 2.1f, 2.8f);
        gl.glScalef(2, 2, 2);
        MasjedOld(glut, gl);
        gl.glPopMatrix();
        
        // masjid (back)
        gl.glPushMatrix();
        gl.glTranslatef(-1.5f, 2.1f, -1.8f);
        gl.glScalef(2, 2, 2);
        gl.glRotatef(90, 0, 1, 0);
        MasjedOld(glut, gl);
        gl.glPopMatrix();

        // School 
        gl.glPushMatrix();
        gl.glTranslatef(-5.5f, 1.32f, 6.6f);
        gl.glRotatef(-90, 0, 1, 0);
        oldSchool(glut, gl);
        
        // drawing on school walls (1)
        gl.glPushMatrix();
        gl.glTranslatef(2.76f, -0.97f, -12.5f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glRotated(90, 0, 1, 0);
        gl.glEnable(GL.GL_TEXTURE_2D);
        schoolSpray.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(0.75f, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(0.75f, 0.65f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 0.65f);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glPopMatrix();
        
        // drawing on school walls (2)
        gl.glPushMatrix();
        gl.glTranslatef(2.76f, -0.97f, -14.47f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glRotated(90, 0, 1, 0);
        gl.glEnable(GL.GL_TEXTURE_2D);
        doodles.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(1.3f, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(1.3f, 0.55f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 0.55f);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glPopMatrix();
        
        // drawing on school walls (3)
        gl.glPushMatrix();
        gl.glTranslatef(2.5f, -0.97f, -16.76f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glRotated(180, 0, 1, 0);
        gl.glEnable(GL.GL_TEXTURE_2D);
        spray.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(1.3f, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(1.3f, 0.55f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 0.55f);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glPopMatrix();

        gl.glPopMatrix();
        
        
        // Tall building
        gl.glPushMatrix();
        gl.glTranslatef(-9, 2.6f, -16f);
        gl.glRotatef(180, 0, 1, 0);
        TallBuilding(glut, gl, 2, 255, 202, 125);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-9, 2.6f, 17);
        TallBuilding(glut, gl, 1, 210, 191, 135);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-3.5f, 2.6f, 17);
        TallBuilding(glut, gl, 3, 166, 130, 106);
        gl.glPopMatrix();

 
        // old houses
        
        gl.glPushMatrix();
        gl.glRotatef(90, 0, 1, 0);
        gl.glTranslatef(-8, 0.74f, 9);
        oldHouse(glut, gl, 119, 140, 133, 90, 112, 110);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(90, 0, 1, 0);
        gl.glTranslatef(-5.6f, 0.74f, 9);
        oldHouse2(glut, gl, 170, 147, 129, 85, 65, 86);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(4.2f, 0.74f, 7.1f);
        oldHouse(glut, gl, 215, 210, 180, 172, 134, 72);
        gl.glPopMatrix();
        
        // Unfinished building (basically one building combined munltiple time to look as one building)
        gl.glPushMatrix();
        
        gl.glTranslatef(0f, 0f, -2f);
        
        gl.glPushMatrix();
        gl.glTranslatef(2f, 0.97f, -5.5f);
        gl.glRotatef(-90, 0, 1, 0);
        gl.glScalef(0.59f, 0.59f, 0.59f);
        UnfinishedBuilding(glut, gl);
        gl.glPopMatrix();
        
         gl.glPushMatrix();
        gl.glTranslatef(2f, 0.97f, -3.15f);
        gl.glRotatef(-90, 0, 1, 0);
        gl.glScalef(0.59f, 0.59f, 0.59f);
        UnfinishedBuilding(glut, gl);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(8.36f, 0.97f, 3.05f);
        gl.glRotatef(0, 0, 1, 0);
        gl.glScalef(0.59f, 0.59f, 0.59f);
        UnfinishedBuilding(glut, gl);
        gl.glPopMatrix();
        
        gl.glPopMatrix();

        // House witth balcony in the front
        gl.glPushMatrix();
        gl.glTranslatef(9.22f, 1.6f, 1.2f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(0.55f, 0.55f, 0.55f);
        oldHouseWithBalcony(glut, gl, 141, 109, 94, 163, 146, 143);
        gl.glPopMatrix();

        // House with wall in the front
        gl.glPushMatrix();
        
        gl.glTranslatef(-4f, 0.73f, 7);
        
        gl.glRotatef(90, 0, 1, 0);
        
        oldHouseWithWall(glut, gl, 1, 255, 202, 125, 147, 95, 2, 245, 210, 145);
        
        // Spray on the wall
        gl.glPushMatrix();
        gl.glEnable(GL_DEPTH_TEST);
        gl.glTranslatef(-2.29f, -0.4f, -5f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glRotated(90, 0, 1, 0);
        gl.glEnable(GL.GL_TEXTURE_2D);
        Mugawil.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(0.63f, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(0.63f, 0.63f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 0.63f);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glDisable(GL_DEPTH_TEST);
        gl.glPopMatrix();
        
        gl.glPopMatrix();

        // Another old house with walls (beside the unfinished building)
        gl.glPushMatrix();
        gl.glTranslatef(9f, 1.13f, 1);
        gl.glRotatef(270, 0, 1, 0);
        gl.glScalef(0.45f, 0.45f, 0.45f);
        oldHouseWithWall2(gl, glut, 266, 174, 93, 112, 87, 46);
        gl.glPopMatrix();

        // House with walls in the back
        gl.glPushMatrix();
        gl.glTranslatef(-4f, 0.73f, -8.5f);
        gl.glRotatef(180, 0, 1, 0);
        oldHouseWithWall(glut, gl, 2, 135, 146, 137, 98, 105, 98, 186, 197, 184);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-3.3f, 0.74f, -4.5f);
        gl.glRotatef(90, 0, 1, 0);
        oldHouse(glut, gl, 183, 135, 125, 186, 180, 172);
        gl.glPopMatrix();

        // house witth balcony in the back
        gl.glPushMatrix();
        gl.glTranslatef(-9.2f, 1.6f, -9.5f);
        gl.glRotatef(180, 0, 1, 0);
        gl.glScalef(0.55f, 0.55f, 0.55f);
        oldHouseWithBalcony(glut, gl, 172, 134, 72, 213, 208, 178);
        gl.glPopMatrix();

        // another old houses
        gl.glPushMatrix();
        gl.glTranslatef(1.49f, 0.74f, -6.7f);
        gl.glRotatef(180, 0, 1, 0);
        oldHouse2(glut, gl, 170, 147, 129, 85, 65, 86);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(3.4f, 0.74f, -8.2f);
        gl.glRotatef(90, 0, 1, 0);
        oldHouse2( glut,  gl, 141, 162, 124, 93, 110, 97);
        gl.glPopMatrix();
        
        // cars
        gl.glPushMatrix();
        gl.glTranslatef(-7.2f, 0.18f, 8.5f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        drawCar(gl, glut, 1f, 1f, 1f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-7.2f, 0.18f, 7.5f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        drawCar(gl, glut, 67 / 255f, 87 / 255f, 114 / 255f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-8.6f, 0.18f, 7.1f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(90, 0, 1, 0);
        drawCar(gl, glut, 183 / 255f, 24 / 255f, 0f);
        gl.glPopMatrix();

        gl.glPushMatrix();                                                      
        
        // Green car animation
        gl.glTranslatef(moveForward, 0.37f, moveLeft);                                    
        
        System.out.println("move forward = "+moveForward+"\nmove left = "+moveLeft+"\nangle = "+angle);
        
        if(move && !stopRotating){
            
        moveForward = moveForward -0.15f;
            
        if(moveForward<0 ){
        angle = angle+1f;
        moveLeft = moveLeft +0.13f;
        }
        
        if(moveLeft >= -5){
            angle = angle-3;
            move = false;
        stopRotating = true;}
            
        }
        
        if(stopRotating){
             moveForward = moveForward -0.08f;
             
             if(moveForward<-6.7f)
                 stopRotating = false;
        }
        
        gl.glPushMatrix();   

        gl.glTranslatef(-0.75f, -0.2f, 11.7f);
        gl.glRotatef(angle, 0, 1, 0);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        drawCar(gl, glut, 105 / 255f, 122 / 255f, 71 / 255f);
        
        gl.glPopMatrix();
        
        gl.glPopMatrix();

        // cars, beside the mousque
        gl.glPushMatrix();
        gl.glTranslatef(0.38f, 0.18f, 2.5f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(180, 0, 1, 0);
        drawCar(gl, glut, 199 / 255f, 130 / 255f, 45 / 255f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-6.6f, 0.18f, 2);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(90, 0, 1, 0);
        drawCar(gl, glut, 0f, 0f, 0f);
        gl.glPopMatrix();

        // Black and dark blue cars, beside the balacony building
        gl.glPushMatrix();
        gl.glTranslatef(6f, 0.18f, 2.2f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(280, 0, 1, 0);
        drawCar(gl, glut, 0f, 0f, 0f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(5.52f, 0.18f, 2.2f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(277, 0, 1, 0);
        drawCar(gl, glut, 32 / 255f, 53 / 255f, 80 / 255f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-9.2f, 0.18f, 4.8f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(117, 0, 1, 0);
        drawCar(gl, glut, 1f, 1f, 1f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-8.6f, 0.18f, 4.8f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(120, 0, 1, 0);
        drawCar(gl, glut, 123 / 255f, 13 / 255f, 74 / 255f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-8.2f, 0.18f, 4.8f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(110, 0, 1, 0);
        drawCar(gl, glut, 198 / 255f, 132 / 255f, 66 / 255f);
        gl.glPopMatrix();

        // cars, beside the two long buildings
        gl.glPushMatrix();
        gl.glTranslatef(-10.95f, 0.18f, 2.5f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(185, 0, 1, 0);
        drawCar(gl, glut, 232 / 255f, 241 / 255f, 95 / 255f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-10.95f, 0.18f, 1.8f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(175, 0, 1, 0);
        drawCar(gl, glut, 171 / 255f, 220 / 255f, 90 / 255f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-10.95f, 0.18f, -0.1f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(165, 0, 1, 0);
        drawCar(gl, glut, 1f, 0f, 0f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-10.95f, 0.18f, -1f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(180, 0, 1, 0);
        drawCar(gl, glut, 13 / 255f, 69 / 255f, 129 / 255f);
        gl.glPopMatrix();
        
        //cars, behind thee building that are behind the long buildings
        gl.glPushMatrix();
        gl.glTranslatef(-10.5f, 0.18f, -3.8f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(180, 0, 1, 0);
        drawCar(gl, glut, 0f, 0f, 0f);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(-10.5f, 0.18f, -4.4f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(188, 0, 1, 0);
        drawCar(gl, glut, 0 / 255f, 3 / 255f, 56 / 255f);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(-10.5f, 0.18f, -5.4f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(166, 0, 1, 0);
        drawCar(gl, glut, 94 / 255f, 94 / 255f, 94 / 255f);
        gl.glPopMatrix();
        
        // cars, in front of the school
        gl.glPushMatrix();
        gl.glTranslatef(5.4f, 0.18f, 7f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(190, 0, 1, 0);
        drawCar(gl, glut, 168 / 255f, 168 / 255f, 168 / 255f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(5.4f, 0.18f, 7.7f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(185, 0, 1, 0);
        drawCar(gl, glut, 131 / 255f, 196 / 255f, 215 / 255f);
        gl.glPopMatrix();

        // cars, behind the mousque
        gl.glPushMatrix();
        gl.glTranslatef(3.5f, 0.18f, -1.45f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(280, 0, 1, 0);
        drawCar(gl, glut, 195 / 255f, 140 / 255f, 80 / 255f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(4.2f, 0.18f, -1.45f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(277, 0, 1, 0);
        drawCar(gl, glut, 233 / 255f, 57 / 255f, 95 / 255f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(4.9f, 0.18f, -1.45f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(272, 0, 1, 0);
        drawCar(gl, glut, 73 / 255f, 106 / 255f, 125 / 255f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(5.5f, 0.18f, -1.45f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(260, 0, 1, 0);
        drawCar(gl, glut, 81 / 255f, 202 / 255f, 57 / 255f);
        gl.glPopMatrix();

        // carinside the orange house 
        gl.glPushMatrix();
        gl.glTranslatef(10.3f, 0.18f, -1.45f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(180, 0, 1, 0);
        drawCar(gl, glut, 0f, 0f, 0f);
        gl.glPopMatrix();
        
        // cars in front of the goal (right)
        gl.glPushMatrix();
        gl.glTranslatef(0.7f, 0.18f, 7.7f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(90, 0, 1, 0);
        drawCar(gl, glut, 170 / 255f, 114 / 255f, 129 / 255f);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(1.95f, 0.18f, 7.7f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(-90, 0, 1, 0);
        drawCar(gl, glut, 178 / 255f, 68 / 255f, 0f);
        gl.glPopMatrix();
        
        // cars in the  park  (back)
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.18f, -3f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(95, 0, 1, 0);
        drawCar(gl, glut, 170 / 255f, 114 / 255f, 129 / 255f);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 0.18f, -3.1f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(85, 0, 1, 0);
        drawCar(gl, glut, 67 / 255f, 67 / 255f, 67 / 255f);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(1f, 0.18f, -4.8f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(185, 0, 1, 0);
        drawCar(gl, glut, 61 / 255f, 90 / 255f, 37 / 255f);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(1.1f, 0.18f, -5.5f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(188, 0, 1, 0);
        drawCar(gl, glut, 170 / 255f, 170 / 255f, 170 / 255f);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(1.1f, 0.18f, -6.5f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(175, 0, 1, 0);
        drawCar(gl, glut, 159 / 255f, 39 / 255f, 39 / 255f);
        gl.glPopMatrix();
        
        // cars, behind the back mousque
        
        gl.glPushMatrix();
        gl.glTranslatef(-4.1f, 0.18f, -7.5f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(90, 0, 1, 0);
        drawCar(gl, glut, 193 / 255f, 0 / 255f, 39 / 255f);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(-4.05f, 0.18f, -6.5f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(175, 0, 1, 0);
        drawCar(gl, glut, 211 / 255f, 128 / 255f, 58 / 255f);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(-4.05f, 0.18f, -5.7f);
        gl.glScalef(0.67f, 0.67f, 0.67f);
        gl.glRotatef(170, 0, 1, 0);
        drawCar(gl, glut, 50 / 255f, 116 / 255f, 21 / 255f);
        gl.glPopMatrix();
        
        // clouds
        
        gl.glPushMatrix();
        gl.glTranslatef(0f, 2f, -1f);
        Cloud1( glut,  gl);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 2f, -1f);
        gl.glRotatef(90, 0, 1, 0);
        Cloud1( glut,  gl);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(-2f, 2f, -1.5f);
        gl.glRotatef(-90, 0, 1, 0);
        Cloud1( glut,  gl);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, 0f);
        Cloud2( glut,  gl);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(4f, 1f, -3.5f);
        gl.glRotatef(-90, 0, 1, 0);
        Cloud2( glut,  gl);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(3f, 1f, -3.3f);
        gl.glRotatef(90, 0, 1, 0);
        Cloud2( glut,  gl);
        gl.glPopMatrix();
        
        gl.glPopMatrix();
        
        // Flush all drawing operations to the graphics card
        gl.glDisable(GL_BLEND);
        gl.glDisable(GL_DEPTH_TEST);

        
        gl.glFlush();
        
        
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    // House with wall ---------------------------------------------------------

    public  void oldHouseWithWall(GLUT glut, GL gl, int num, int bs1, int bs2, int bs3, int bs4, int bs5, int bs6, int w1, int w2, int w3){
        gl.glPushMatrix();//BIG PUSH

        //********************STRUCTURE OF THE HOUSE************************
        gl.glPushMatrix();
        gl.glEnable(GL_BLEND);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        gl.glColor3f(bs1/255f, bs2 / 255f, bs3 / 255f);
        gl.glScalef(1.5f, 1, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, .5f, -6.0f);
        gl.glColor3f(bs4 / 255f, bs5 / 255f, bs6 / 255f);
        gl.glScalef(1.5f, 0.08f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.04f, -6.0f);
        gl.glColor3f(bs1/255f, bs2 / 255f, bs3 / 255f);
        gl.glScalef(1.5f, 1f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //AC
        gl.glPushMatrix();
        gl.glTranslatef(-.85f, 1.1f, -6.33f);
        gl.glColor3f(76 / 255f, 71 / 255f, 62 / 255f);
        gl.glScalef(.16f, .36f, .5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-.85f, 1.1f, -5.65f);
        gl.glColor3f(76 / 255f, 71 / 255f, 62 / 255f);
        gl.glScalef(.16f, .36f, .5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //********************END OF THE STRUCTURE************************
        //-------Windows----------
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -.26f, -5.29f);
        gl.glColor3f(132 / 255f, 132 / 255f, 132 / 255f);
        gl.glScalef(.5f, .5f, 0.09f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.4f, 1f, -5.29f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(.5f, .5f, 0.09f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        //brown line
        gl.glPushMatrix();
        gl.glTranslatef(0.4f, .7f, -5.29f);
        gl.glColor3f(147 / 255f, 95 / 255f, 2 / 255f);
        gl.glScalef(.5f, .08f, 0.1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.4f, 1f, -5.29f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(.5f, .5f, 0.09f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.4f, .7f, -5.29f);
        gl.glColor3f(147 / 255f, 95 / 255f, 2 / 255f);
        gl.glScalef(.5f, .08f, 0.1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Left side
        gl.glPushMatrix();
        gl.glTranslatef(-0.74f, 0f, -5.67f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(0.05f, .5f, 0.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.74f, -0.3f, -5.67f);
        gl.glColor3f(147 / 255f, 95 / 255f, 2 / 255f);
        gl.glScalef(0.05f, .08f, 0.47f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.74f, 0f, -6.37f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(0.05f, .5f, 0.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.74f, -0.3f, -6.37f);
        gl.glColor3f(147 / 255f, 95 / 255f, 2 / 255f);
        gl.glScalef(0.05f, .08f, 0.47f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //-------End of Windows----------
        //--------roof-------- 
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.5f, -6.0f);
        gl.glColor3f(bs4 / 255f, bs5 / 255f, bs6 / 255f);
        gl.glScalef(1.5f, 0.08f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.6f, -6.71f);
        gl.glColor3f(bs4 / 255f, bs5 / 255f, bs6 / 255f);
        gl.glScalef(1.5f, .2f, .08f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.71f, 1.6f, -6f);
        gl.glColor3f(bs4 / 255f, bs5 / 255f, bs6 / 255f);
        gl.glScalef(.08f, .2f, 1.35f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.71f, 1.6f, -6f);
        gl.glColor3f(bs4 / 255f, bs5 / 255f, bs6 / 255f);
        gl.glScalef(.08f, .2f, 1.35f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.6f, -5.29f);
        gl.glColor3f(bs4 / 255f, bs5 / 255f, bs6 / 255f);
        gl.glScalef(1.5f, .2f, .08f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //water tank
        gl.glPushMatrix();
        gl.glTranslatef(-.5f, 1.79f, -5.8f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.2f, .4f, .5f);
        gl.glRotatef(90, 0, 1, 0);
        glut.glutSolidCylinder(.5f, 3, 50, 15);
        gl.glPopMatrix();

        // Sati 
        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.8f, -6f);
        gl.glScalef(0.15f, 0.15f, 0.15f);
        satellite(gl, glut, 87, 85, 86, 78, 76, 77, 0, 198, 137);
        gl.glPopMatrix();

        //--------End of roof--------
        // Outside wall
        wall(glut, gl, num, w1, w2, w3);
        //BIG POP
        gl.glPopMatrix();
    }
    
    public void oldHouseWithWall2(GL gl, GLUT glut, int h1, int h2, int h3,int r1, int r2, int r3) {

        gl.glPushMatrix();

        gl.glEnable(GL_BLEND);
        gl.glEnable(GL_DEPTH_TEST);

        // house
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, -2f);
        gl.glScalef(3f, 2f, 2);
        gl.glColor3d(h1 / 255f, h2 / 255f, h3 / 255f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        // roof front
        gl.glPushMatrix();
        gl.glTranslatef(0, 2.2f, -0.2f);
        gl.glScalef(3f, 0.2f, 0.2f);
        gl.glColor3d(r1 / 255f, r2 / 255f, r3 / 255f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        // roof back
        gl.glPushMatrix();
        gl.glTranslatef(0, 2.2f, -3.8f);
        gl.glScalef(3f, 0.2f, 0.2f);
        gl.glColor3d(r1 / 255f, r2 / 255f, r3 / 255f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        // roof right
        gl.glPushMatrix();
        gl.glTranslatef(3, 2.2f, -2f);
        gl.glScalef(0.1f, 0.2f, 2f);
        gl.glColor3d(r1 / 255f, r2 / 255f, r3 / 255f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        // roof left
        gl.glPushMatrix();
        gl.glTranslatef(-3, 2.2f, -2f);
        gl.glScalef(0.1f, 0.2f, 2f);
        gl.glColor3d(r1 / 255f, r2 / 255f, r3 / 255f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        // begin of the windows
        gl.glPushMatrix();
        gl.glTranslatef(-2, 1f, 0.005f);
        gl.glScalef(0.5f, 0.5f, 0f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0, 1f, 0.005f);
        gl.glScalef(0.5f, 0.5f, 0f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2, 1f, 0.005f);
        gl.glScalef(0.5f, 0.5f, 0f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2, 1f, -4.01f);
        gl.glScalef(0.5f, 0.5f, 0f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0, 1f, -4.01f);
        gl.glScalef(0.5f, 0.5f, 0f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2, 1f, -4.01f);
        gl.glScalef(0.5f, 0.5f, 0f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(3, 1f, -1f);
        gl.glScalef(0.01f, 0.5f, 0.5f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(3, 1f, -3f);
        gl.glScalef(0.01f, 0.5f, 0.5f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        // 3 air-condi.
        gl.glPushMatrix();
        gl.glTranslatef(-3.2f, 1f, -1f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        gl.glColor3d(127 / 255f, 127 / 255f, 127 / 255f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-3.2f, 1f, -2f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        gl.glColor3d(127 / 255f, 127 / 255f, 127 / 255f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-3.2f, 1f, -3f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        gl.glColor3d(127 / 255f, 127 / 255f, 127 / 255f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(.4f, 2.5f, -2f);
        gl.glColor3f(209 / 255f, 209 / 255f, 209 / 255f);
        gl.glScalef(.5f, .5f, .8f);
        gl.glRotatef(90, 0, 1, 0);
        glut.glutSolidCylinder(.5f, 3, 50, 15);
        gl.glPopMatrix();

        // door
        gl.glPushMatrix();
        gl.glTranslatef(0, -1.3f, 0.005f);
        gl.glScalef(0.7f, 0.7f, 0f);
        gl.glColor3d(0f, 0f, 0f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0, -1.5f, 2.26f);
        gl.glScalef(0.7f, 0.5f, 0f);
        gl.glColor3d(0f, 0f, 0f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-6, -1.48f, 2.26f);
        gl.glScalef(1f, 0.52f, 0f);
        gl.glColor3d(91 / 255f, 91 / 255f, 91 / 255f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        // Sattilite
        gl.glPushMatrix();
        gl.glTranslatef(0, 3f, -.2f);
        gl.glScalef(0.5f, 0.5f, 0.4f);
        satellite(gl, glut, 87, 85, 86, 78, 76, 77, 0 , 198, 137);
        gl.glPopMatrix();

        
        wall(glut, gl, 3, h1, h2, h3);

        gl.glDisable(GL_BLEND);
        gl.glDisable(GL_DEPTH_TEST);

        gl.glPopMatrix();
    }
    
    public void wall ( GLUT glut, GL gl, int num, int w1, int w2, int w3){
        
        if( num == 1 ){
            
        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, -0.12f, -5.85f);
        gl.glColor3f(254 / 255f, 210 / 255f, 145 / 255f);
        gl.glScalef(.16f, .7f, 2.46f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.47f, -0.12f, -5.85f);
        gl.glColor3f(254 / 255f, 210 / 255f, 145 / 255f);
        gl.glScalef(.16f, .7f, 2.46f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // front
        gl.glPushMatrix();
        gl.glTranslatef(-0.39f, -0.12f, -4.7f);
        gl.glColor3f(254 / 255f, 210 / 255f, 145 / 255f);
        gl.glScalef(3.55f, .7f, 0.16f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // back
        gl.glPushMatrix();
        gl.glTranslatef(-0.39f, -0.12f, -7f);
        gl.glColor3f(254 / 255f, 210 / 255f, 145 / 255f);
        gl.glScalef(3.55f, .7f, 0.16f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // door
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -.24f, -4.61f);
        gl.glColor3f(147 / 255f, 95 / 255f, 2 / 255f);
        gl.glScalef(.5f, .5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.4f, -.2f, -4.61f);
        gl.glColor3f(131 / 255f, 131 / 255f, 131 / 255f);
        gl.glScalef(1f, .58f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glDisable(GL_BLEND);
        gl.glDisable(GL_DEPTH_TEST);
        
        } else if (num == 2){
            
        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, -0.12f, -5.85f);
        gl.glColor3f(w1 / 255f, w2 / 255f, w3 / 255f);
        gl.glScalef(.16f, .7f, 2.46f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.47f, -0.12f, -5.85f);
        gl.glColor3f(w1 / 255f, w2 / 255f, w3 / 255f);
        gl.glScalef(.16f, .7f, 2.46f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // front
        gl.glPushMatrix();
        gl.glTranslatef(-0.39f, -0.12f, -4.7f);
        gl.glColor3f(w1 / 255f, w2 / 255f, w3 / 255f);
        gl.glScalef(3.55f, .7f, 0.16f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // back
        gl.glPushMatrix();
        gl.glTranslatef(-0.39f, -0.12f, -7f);
        gl.glColor3f(w1 / 255f, w2 / 255f, w3 / 255f);
        gl.glScalef(3.55f, .7f, 0.16f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // door
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -.24f, -4.61f);
        gl.glColor3f(98 / 255f, 105 / 255f, 98 / 255f);
        gl.glScalef(.5f, .5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.4f, -.2f, -4.61f);
        gl.glColor3f(131 / 255f, 131 / 255f, 131 / 255f);
        gl.glScalef(1f, .58f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glDisable(GL_BLEND);
        gl.glDisable(GL_DEPTH_TEST);
        
        } else {
            // wall
        gl.glPushMatrix();
        gl.glTranslatef(-2f, -1.41f, 2f);
        gl.glScalef(6.5f, 0.57f, 0.25f);
        gl.glColor3d(226 / 255f, 174 / 255f, 93 / 255f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2f, -1.41f, -5.3f);
        gl.glScalef(6.5f, 0.57f, 0.25f);
        gl.glColor3d(226 / 255f, 174 / 255f, 93 / 255f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-8.25f, -1.41f, -1.7f);
        gl.glScalef(0.25f, 0.57f, 3.8f);
        gl.glColor3d(226 / 255f, 174 / 255f, 93 / 255f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(4.25f, -1.41f, -1.7f);
        gl.glScalef(0.25f, 0.57f, 3.8f);
        gl.glColor3d(226 / 255f, 174 / 255f, 93 / 255f);
        glut.glutSolidCube(2F);
        gl.glPopMatrix();
        }
    }
    
    //Building with balcony ----------------------------------------------------

    public static void oldHouseWithBalcony(GLUT glut, GL gl, int el1, int el2, int el3, int s1, int s2, int s3) {

        gl.glPushMatrix();

        gl.glEnable(GL_BLEND);
        gl.glEnable(GL_DEPTH_TEST);

        // edge line
        gl.glPushMatrix();
        gl.glTranslatef(-1.4f, 0.0f, -5.22f);
        gl.glColor3f(el1 / 255f, el2 / 255f, el3 / 255f);
        gl.glScalef(.2f, 4.95f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.4f, 0.0f, -5.22f);
        gl.glColor3f(el1 / 255f, el2 / 255f, el3 / 255f);
        gl.glScalef(.2f, 4.95f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // structure
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        gl.glColor3f(s1 / 255f, s2 / 255f, s3 / 255f);
        gl.glScalef(3f, 5, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //balconies
        gl.glPushMatrix();
        balcony(gl, glut);
        gl.glPopMatrix();

        gl.glTranslatef(0.0f, 0.0f, 0.0f);

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -2f, 0f);
        balcony(gl, glut);
        gl.glPopMatrix();

        //door
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -2.22f, -5.22f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, .5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.25f, -2.22f, -5.21f);
        gl.glColor3f(160 / 255f, 160 / 255f, 160 / 255f);
        gl.glScalef(.5f, .5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //roof
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 2.55f, -6.0f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(3f, .2f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //AC
        gl.glPushMatrix();
        gl.glTranslatef(-1.5f, 1.6f, -6.0f);
        gl.glColor3f(81 / 255f, 81 / 255f, 81 / 255f);
        gl.glScalef(.5f, .6f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.5f, -0.2f, -6.0f);
        gl.glColor3f(81 / 255f, 81 / 255f, 81 / 255f);
        gl.glScalef(.5f, .6f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, 1.6f, -6.0f);
        gl.glColor3f(81 / 255f, 81 / 255f, 81 / 255f);
        gl.glScalef(.5f, .6f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.2f, -6.0f);
        gl.glColor3f(81 / 255f, 81 / 255f, 81 / 255f);
        gl.glScalef(.5f, .6f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glDisable(GL_BLEND);
        gl.glDisable(GL_DEPTH_TEST);

        gl.glPopMatrix();

    }

    public static void balcony(GL gl, GLUT glut) {
        //balcony
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.5f, -5.22f);
        gl.glColor3f(206 / 255f, 206 / 255f, 206 / 255f);
        gl.glScalef(1.9f, 1.5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.45f, 1.5f, -5.19f);
        gl.glColor3f(160 / 255f, 160 / 255f, 160 / 255f);
        gl.glScalef(1f, 1.5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.8f, -5.0f);
        gl.glColor3f(66 / 255f, 66 / 255f, 65 / 255f);
        gl.glScalef(1.9f, .1f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.5f, -4.3f);
        gl.glColor3f(66 / 255f, 66 / 255f, 65 / 255f);
        gl.glScalef(1.8f, .1f, .1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(.9f, 1.5f, -5.0f);
        gl.glColor3f(66 / 255f, 66 / 255f, 65 / 255f);
        gl.glScalef(.1f, .1f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-.9f, 1.5f, -5.0f);
        gl.glColor3f(66 / 255f, 66 / 255f, 65 / 255f);
        gl.glScalef(.1f, .1f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-.9f, 1.2f, -4.6f);
        gl.glColor3f(66 / 255f, 66 / 255f, 65 / 255f);
        gl.glScalef(.02f, .7f, .05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-.9f, 1.2f, -4.9f);
        gl.glColor3f(66 / 255f, 66 / 255f, 65 / 255f);
        gl.glScalef(.02f, .7f, .05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(.9f, 1.2f, -4.3f);
        gl.glColor3f(66 / 255f, 66 / 255f, 65 / 255f);
        gl.glScalef(.1f, .7f, .1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(.9f, 1.2f, -4.6f);
        gl.glColor3f(66 / 255f, 66 / 255f, 65 / 255f);
        gl.glScalef(.02f, .7f, .05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(.9f, 1.2f, -4.9f);
        gl.glColor3f(66 / 255f, 66 / 255f, 65 / 255f);
        gl.glScalef(.02f, .7f, .05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        float i = .9f;
        while (i > -.9) {
            gl.glPushMatrix();
            gl.glTranslatef(i, 1.2f, -4.3f);
            gl.glColor3f(66 / 255f, 66 / 255f, 65 / 255f);
            gl.glScalef(.02f, .7f, .1f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();
            i -= .3;
        }

        gl.glPushMatrix();
        gl.glTranslatef(-.9f, 1.2f, -4.3f);
        gl.glColor3f(66 / 255f, 66 / 255f, 65 / 255f);
        gl.glScalef(.1f, .7f, .1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

    }

    // Masjed ------------------------------------------------------------------
    
    public void MasjedOld(GLUT glut, GL gl) {

        gl.glPushMatrix();

        gl.glTranslatef(2f, 0.0f, 0f);
        //gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        //------------------------------------all

        gl.glPushMatrix();
        //gl.glTranslatef(0f, 1.2f, -2.0f);
        //gl.glRotatef(90, 1, 0, 0);
        gl.glScalef(0.5f, 0.9f, 0.7f);
        buldingOld(glut, gl);
        gl.glPopMatrix();
        //----------------------------------------
        gl.glPushMatrix();
        gl.glTranslatef(0.15f, 0f, -0.2f);
        //gl.glRotatef(90, 1, 0, 0);
        gl.glScalef(0.6f, 0.6f, 0.6f);
        Qubba(glut, gl);
        gl.glPopMatrix();
        //----------------------------------------
        gl.glPushMatrix();
        gl.glTranslatef(0.2f, 0f, -0.5f);
        //gl.glRotatef(90, 1, 0, 0);
        gl.glScalef(0.6f, 0.6f, 0.6f);
        MethanaOld(glut, gl);
        gl.glPopMatrix();
        //----------------------------------------

        gl.glPopMatrix();//end all ---------------------------------

    }

    public void buldingOld(GLUT glut, GL gl) {

        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, -1.0f, 0.0f);
        //gl.glRotatef(40, 0, 1, 0);

        //bulding
        gl.glPushMatrix();

        gl.glEnable(GL_DEPTH_TEST);
        gl.glTranslatef(0f, 0.0f, -2f);
        //gl.glRotatef(angle, x, y, z);
        gl.glScalef(2.2f, 1.03f, 1.5f);
        gl.glColor3f(241 / 255f, 239 / 255f, 240 / 255f);

        glut.glutSolidCube(2);//thing

        gl.glPopMatrix();//end 1

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, 0f);
        //gl.glRotatef(-60, 0, 1, 1);
        //gl.glScalef(0.3f, 0.3f, 0f);
        // start all windows infront

        //window1
        gl.glPushMatrix();
        gl.glTranslatef(1.5f, 0.5f, -0.4f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.5f, 0.3f, 0f);
        window(glut, gl);
        gl.glPopMatrix();
        //---------------------------

        //window3
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 0.5f, -0.4f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.5f, 0.3f, 0f);
        window(glut, gl);
        gl.glPopMatrix();
        //---------------------------

        //window5
        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 0.5f, -0.4f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.5f, 0.3f, 0f);
        window(glut, gl);
        gl.glPopMatrix();
        //---------------------------

        //window7
        gl.glPushMatrix();
        gl.glTranslatef(-1.5f, 0.5f, -0.4f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.5f, 0.3f, 0f);
        window(glut, gl);
        gl.glPopMatrix();
        //---------------------------

        gl.glPopMatrix();
        // end windows infront -----------------------------------------

        //entrance1
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -0.5f, -0.4f);
        //gl.glRotatef(angle, x, y, z);
        gl.glScalef(1.3f, 1f, 0.3f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        gl.glColor3f(0.643f, 0.647f, 0.662f);

        glut.glutSolidCube(1);//thing

        gl.glPopMatrix();//----end 2
        //--------------------------------------------------------

        //door
        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, -0.3f, -0.24f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.6f, 0.4f, 0.1f);
        door(glut, gl);
        gl.glPopMatrix();

        //----------------------------------------------
        gl.glPopMatrix();//end all
    }

    public void MethanaOld(GLUT glut, GL gl) {

        gl.glPushMatrix();//start all
        gl.glTranslatef(-1.3f, 2.4f, -1.8f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.2f, 0.2f, 0.2f);

        gl.glPushMatrix();
        gl.glTranslatef(0f, 2f, -2.0f);
        //gl.glRotatef(90, 1, 0, 0);

        //cylinder
        gl.glPushMatrix();
        gl.glTranslatef(-2f, 0f, -2.0f);
        gl.glRotatef(90, 1, 0, 0);
        //gl.glScalef(1, 1.7f, 1);
        gl.glColor3f(0.643f, 0.647f, 0.662f);

        glut.glutSolidCylinder(0.05, 0.3, 20, 20);//thing
        gl.glPopMatrix();//----end 1

        //cylinder
        gl.glPushMatrix();
        gl.glTranslatef(-2f, -0.3f, -2.0f);
        gl.glRotatef(90, 1, 0, 0);
        //gl.glScalef(1, 1.7f, 1);
        gl.glColor3f(0.643f, 0.647f, 0.662f);

        glut.glutSolidCylinder(0.1, 5, 20, 20);//thing
        gl.glPopMatrix();//----end 2

        //sphere
        gl.glPushMatrix();
        gl.glTranslatef(-2f, -2f, -2f);
        //gl.glScalef(1f, 1f, 1f);
        gl.glColor3f(1f, 0.741f, 0.2f);
        //gl.glRotatef(angle, x, y, z);

        glut.glutSolidSphere(1.4f, 20, 20);//thing

        gl.glPopMatrix();//end 3

        gl.glPopMatrix();

        //-------------------------------------------------------
        gl.glPushMatrix();//start all
        gl.glTranslatef(-2f, 0f, -2f);
        //gl.glRotatef(angle, 0, 0, 0);
        //gl.glScalef(0.2f, 0.2f, 0.2f);
        //cylinder
        gl.glPushMatrix();
        gl.glTranslatef(0f, -1f, -2.0f);
        gl.glRotatef(90, 1, 0, 0);
        //gl.glScalef(1, 1.7f, 1);
        gl.glColor3f(0.643f, 0.647f, 0.662f);

        glut.glutSolidCylinder(1, 2, 10, 5);//thing

        gl.glPopMatrix();//----end 1

        //-------------------------------------------------------
        //cylinder
        gl.glPushMatrix();
        gl.glTranslatef(0f, -3.5f, -2.0f);
        gl.glRotatef(90, 1, 0, 0);
        //gl.glScalef(1, 1.7f, 1);
        gl.glColor3f(0.643f, 0.647f, 0.662f);

        glut.glutSolidTorus(0.5, 2, 3, 20);//thing

        gl.glPopMatrix();//----end 2

        //-------------------------------------------------------
        //cylinder
        gl.glPushMatrix();
        gl.glTranslatef(0f, -4.0f, -2.0f);
        gl.glRotatef(90, 1, 0, 0);
        //gl.glScalef(1, 1.7f, 1);
        gl.glColor3f(0.643f, 0.647f, 0.662f);

        glut.glutWireCylinder(1.3f, 3, 8, 0);//thing Wire object

        gl.glPopMatrix();//----end 3

        //-------------------------------------------------------
        //-------------------------------------------------------
        //cylinder
        gl.glPushMatrix();
        gl.glTranslatef(0f, -7.0f, -2.0f);
        gl.glRotatef(90, 1, 0, 0);
        //gl.glScalef(1, 1.7f, 1);
        gl.glColor3f(0.643f, 0.647f, 0.662f);

        glut.glutSolidCylinder(1.7, 5, 10, 5);//thing

        gl.glPopMatrix();//----end 5

        gl.glPopMatrix();//end cylinder

        gl.glPopMatrix();//end all

    }

    public void window(GLUT glut, GL gl) {

        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, 0f, 0f);
        //gl.glRotatef(-120, 0, 1, 0);
        //gl.glScalef(0.2f, 0.2f, 0.2f);
        //----------------------------------------------------------

        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, 0f, 0f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(1f, 1f, 0f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        //gl.glColor3f(0.643f, 0.647f, 0.662f);
        gl.glColor3f(2 / 255f, 56 / 255f, 80 / 255f);
        glut.glutSolidSphere(0.6, 20, 10);//object
        gl.glPopMatrix();

        //-------------------------------------
        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, -0.8f, 0f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(1f, 1.1f, 0f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        //gl.glColor3f(0.643f, 0.647f, 0.662f);
        gl.glColor3f(2 / 255f, 56 / 255f, 80 / 255f);
        glut.glutSolidCube(1.2f);//object
        gl.glPopMatrix();

        //---------------------------------------
        //------------------------------------------------------
        gl.glPopMatrix();
    }

    public void door(GLUT glut, GL gl) {

        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, 0f, 0f);
        //gl.glRotatef(-120, 0, 1, 0);
        //gl.glScalef(0.2f, 0.2f, 0.2f);
        //----------------------------------------------------------

        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, 0f, 0f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(1f, 1f, 0f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        //gl.glColor3f(0.643f, 0.647f, 0.662f);
        gl.glColor3f(2 / 255f, 56 / 255f, 80 / 255f);
        glut.glutSolidSphere(0.6, 20, 10);//object
        gl.glPopMatrix();

        //-------------------------------------
        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, -1.0f, 0f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(1f, 1.3f, 0f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        //gl.glColor3f(0.643f, 0.647f, 0.662f);
        gl.glColor3f(2 / 255f, 56 / 255f, 80 / 255f);
        glut.glutSolidCube(1.2f);//object
        gl.glPopMatrix();

        //---------------------------------------
        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, 0.2f, 0f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.5f, 0.5f, 0f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        //gl.glColor3f(0.643f, 0.647f, 0.662f);
        gl.glColor3f(2 / 255f, 56 / 255f, 80 / 255f);
        glut.glutSolidOctahedron();//object
        gl.glPopMatrix();

        //---------------------------------------
        //------------------------------------------------------
        gl.glPopMatrix();

    }

    public void Qubba(GLUT glut, GL gl) {

        gl.glPushMatrix();//start all
        gl.glTranslatef(0.5f, 0.4f, 0f);

        gl.glPushMatrix();
        gl.glTranslatef(-0.75f, -0.25f, -2f);
        //gl.glScalef(0.4f, 0.4f, 0.4f);
        gl.glColor3f(1f, 0.741f, 0.2f);
        gl.glRotatef(-90, 1, 0, 0);

        glut.glutSolidSphere(1.1f, 20, 20);//thing

        gl.glPopMatrix();//end 3

        //-------------------------------------------------------
        gl.glPopMatrix();//end all

    }

    // Park --------------------------------------------------------------------
    
    public static void park(GL gl, GLUT glut) {

        gl.glPushMatrix();

        gl.glScalef(0.5f, 0.5f, 0.5f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glTranslatef(1, 3, 2);

        // FIRST GOAL (LEFY SIDE)
        gl.glPushMatrix();

        gl.glTranslatef(10, 0, 6);

        // first pillar
        gl.glPushMatrix();
        gl.glRotatef(95, 1, 0, 0);
        glut.glutSolidCylinder(0.15, 3, 5, 5);
        gl.glPopMatrix();

        // second pillar
        gl.glTranslatef(0, 0, 5);
        gl.glPushMatrix();
        gl.glRotatef(95, 1, 0, 0);
        glut.glutSolidCylinder(0.15, 3, 5, 5);
        gl.glPopMatrix();

        // horizontal pillar
        gl.glTranslatef(0, -0.2f, -5f);
        gl.glPushMatrix();
        //gl.glRotatef(95, 1, 0, 0);
        glut.glutSolidCylinder(0.15, 5, 5, 5);
        gl.glPopMatrix();

        gl.glPopMatrix(); // END OF FIRST GOAL

        // SECOND GOAL (RIGHT SIDE)  
        gl.glPushMatrix();

        gl.glTranslatef(19, 0, 6);

        // first pillar
        gl.glPushMatrix();
        gl.glRotatef(95, 1, 0, 0);
        glut.glutSolidCylinder(0.15, 3, 5, 5);
        gl.glPopMatrix();

        // second pillar
        gl.glTranslatef(0, 0, 5);
        gl.glPushMatrix();
        gl.glRotatef(95, 1, 0, 0);
        glut.glutSolidCylinder(0.15, 3, 5, 5);
        gl.glPopMatrix();

        // horizontal pillar
        gl.glTranslatef(0, -0.2f, -5f);
        gl.glPushMatrix();
        //gl.glRotatef(95, 1, 0, 0);
        glut.glutSolidCylinder(0.15, 5, 5, 5);
        gl.glPopMatrix();

        gl.glPopMatrix(); // END OF SECOND GOAL 

    }

    public static void rock(GL gl, GLUT glut) {

        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.5f, 0f);
        gl.glColor3f(102 / 255f, 96 / 255f, 96 / 255f);
        gl.glScalef(0.1f, 0.1f, 0.1f);
        glut.glutSolidDodecahedron();
        gl.glPopMatrix();
        gl.glPopMatrix();
    }

    // Old houses --------------------------------------------------------------
    
    public static void oldHouse(GLUT glut, GL gl, int bs1, int bs2, int bs3, int bs4, int bs5, int bs6) {
        gl.glPushMatrix();//BIG PUSH

        gl.glEnable(GL.GL_BLEND);
        gl.glEnable(GL.GL_DEPTH_TEST);

        //Structure 
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        gl.glColor3f(bs1 / 255f, bs2 / 255f, bs3 / 255f);
        gl.glScalef(2.3f, 1, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, .5f, -6.0f);
        gl.glColor3f(bs4 / 255f, bs5 / 255f, bs6 / 255f);
        gl.glScalef(2.31f, 0.08f, 1.51f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.04f, -6.0f);
        gl.glColor3f(bs1 / 255f, bs2 / 255f, bs3 / 255f);
        gl.glScalef(2.31f, 1f, 1.51f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.51f, -6.0f);
        gl.glColor3f(bs4 / 255f, bs5 / 255f, bs6 / 255f);
        gl.glScalef(2.31f, 0.08f, 1.52f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.4f, 2.05f, -6.0f);
        gl.glColor3f(bs1 / 255f, bs2 / 255f, bs3 / 255f);
        gl.glScalef(1.5f, 1f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.4f, 2.52f, -6.0f);
        gl.glColor3f(bs4 / 255f, bs5 / 255f, bs6 / 255f);
        gl.glScalef(1.51f, 0.08f, 1.51f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //AC
        gl.glPushMatrix();
        gl.glTranslatef(-1.23f, 1.1f, -6.2f);
        gl.glColor3f(76 / 255f, 71 / 255f, 62 / 255f);
        gl.glScalef(.2f, .4f, .5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 1.99f, -6.2f);
        gl.glColor3f(76 / 255f, 71 / 255f, 62 / 255f);
        gl.glScalef(.2f, .4f, .5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.6f, 1.1f, -6.8f);
        gl.glColor3f(76 / 255f, 71 / 255f, 62 / 255f);
        gl.glScalef(.5f, .4f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.6f, 2f, -6.8f);
        gl.glColor3f(76 / 255f, 71 / 255f, 62 / 255f);
        gl.glScalef(.5f, .4f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.6f, 0f, -6.8f);
        gl.glColor3f(76 / 255f, 71 / 255f, 62 / 255f);
        gl.glScalef(.5f, .4f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();


        //-------Windows----------
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 1f, -5.2f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(.7f, .5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 1f, -5.2f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(.7f, .5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 1f, -6.76f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(.7f, .5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 0f, -6.76f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(.7f, .5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.4f, 2f, -5.2f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(.7f, .5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.16f, 2f, -5.95f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(0f, .5f, 0.7f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.16f, 1f, -5.95f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(0f, .5f, 0.7f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.16f, 0f, -5.95f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(0f, .5f, 0.7f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.16f, 0f, -5.95f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(0f, .5f, 0.7f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // DOOR
        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.17f, -5.2f);
        gl.glColor3f(102 / 255f, 58 / 255f, 28 / 255f);
        gl.glScalef(.39f, .6f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();


        //water tank
        gl.glPushMatrix();
        gl.glTranslatef(.5f, 2.7f, -6f);
        gl.glColor3f(209 / 255f, 209 / 255f, 209 / 255f);
        gl.glScalef(.2f, .4f, .5f);
        gl.glRotatef(90, 0, 1, 0);
        glut.glutSolidCylinder(.5f, 3, 50, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 2.9f, -6f);
        gl.glScalef(0.15f, 0.15f, 0.15f);
        gl.glRotatef(180, 0, 1, 0);
        satellite(gl, glut, 87, 85, 86, 78, 76, 77, 0 , 198, 137);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.2f, 1.8f, -6.2f);
        gl.glScalef(0.15f, 0.15f, 0.15f);
        gl.glRotatef(-90, 0, 1, 0);
        satellite(gl, glut, 87, 85, 86, 78, 76, 77, 0 , 198, 137);
        gl.glPopMatrix();

        gl.glEnable(GL.GL_BLEND);
        gl.glEnable(GL.GL_DEPTH_TEST);

        //BIG POP
        gl.glPopMatrix();
    }

    public static void oldHouse2(GLUT glut, GL gl, int bs1, int bs2, int bs3, int bs4, int bs5, int bs6) {
        gl.glPushMatrix();//BIG PUSH

        gl.glEnable(GL.GL_BLEND);
        gl.glEnable(GL.GL_DEPTH_TEST);

        //Structure
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        gl.glColor3f(bs1 / 255f, bs2 / 255f, bs3 / 255f);
        gl.glScalef(2.3f, 1, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, .5f, -6.0f);
        gl.glColor3f(bs4 / 255f, bs5 / 255f, bs6 / 255f);
        gl.glScalef(2.31f, 0.08f, 1.51f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.04f, -6.0f);
        gl.glColor3f(bs1 / 255f, bs2 / 255f, bs3 / 255f);
        gl.glScalef(2.31f, 1f, 1.51f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.51f, -6.0f);
        gl.glColor3f(bs4 / 255f, bs5 / 255f, bs6 / 255f);
        gl.glScalef(2.31f, 0.08f, 1.52f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //AC
        gl.glPushMatrix();
        gl.glTranslatef(-1.23f, 1.1f, -6.2f);
        gl.glColor3f(76 / 255f, 71 / 255f, 62 / 255f);
        gl.glScalef(.2f, .4f, .5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.6f, 1.1f, -6.8f);
        gl.glColor3f(76 / 255f, 71 / 255f, 62 / 255f);
        gl.glScalef(.5f, .4f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.6f, 0f, -6.8f);
        gl.glColor3f(76 / 255f, 71 / 255f, 62 / 255f);
        gl.glScalef(.5f, .4f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        
        //-------Windows----------
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 1f, -5.2f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(.7f, .5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 1f, -5.2f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(.7f, .5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 1f, -6.76f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(.7f, .5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 0f, -6.76f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(.7f, .5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.16f, 1f, -5.95f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(0f, .5f, 0.7f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.16f, 0f, -5.95f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(0f, .5f, 0.7f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.16f, 0f, -5.95f);
        gl.glColor3f(226 / 255f, 225 / 255f, 224 / 255f);
        gl.glScalef(0f, .5f, 0.7f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // DOOR
        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.17f, -5.2f);
        gl.glColor3f(110 / 255f, 104 / 255f, 106 / 255f);
        gl.glScalef(.39f, .6f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //water tank
        gl.glPushMatrix();
        gl.glTranslatef(.4f, 1.7f, -5.5f);
        gl.glColor3f(209 / 255f, 209 / 255f, 209 / 255f);
        gl.glScalef(.2f, .4f, .5f);
        gl.glRotatef(90, 0, 1, 0);
        glut.glutSolidCylinder(.5f, 3, 50, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.7f, -6.2f);
        gl.glColor3f(209 / 255f, 209 / 255f, 209 / 255f);
        gl.glScalef(.2f, .4f, .5f);
        gl.glRotatef(90, 0, 1, 0);
        glut.glutSolidCylinder(.5f, 3, 50, 15);
        gl.glPopMatrix();

        gl.glEnable(GL.GL_BLEND);
        gl.glEnable(GL.GL_DEPTH_TEST);

        //BIG POP
        gl.glPopMatrix();
    }

    
    // bin ---------------------------------------------------------------------

    public void old_bin(GL gl, GLUT glut) {

        gl.glPushMatrix();

        //left side
        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 0.0f, -4.0f);
        gl.glColor3f(30 / 253f, 85 / 253f, 31 / 253f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(1, 1, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //right side
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 0.0f, -4.0f);
        gl.glColor3f(30 / 253f, 85 / 253f, 31 / 253f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(1, 1, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //bottom side
        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.475f, -4.0f);
        gl.glColor3f(30 / 253f, 85 / 253f, 31 / 253f);
        gl.glRotatef(90, 1, 0, 0);
        gl.glScalef(1, 1, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Trash inside ----
        gl.glPushMatrix();
        gl.glTranslatef(0.24f, 0.85f, -3.69f);
        gl.glColor3f(236 / 255f, 57 / 255f, 42 / 255f);
        gl.glScalef(0.2f, 0.2f, 0.2f);
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(.5f, 2f, 30, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.85f, -4.4f);
        gl.glColor3f(252 / 255f, 193 / 255f, 16 / 255f);
        gl.glScalef(0.23f, 0.23f, 0.23f);
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(.5f, 2f, 30, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.5f, -3.66f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.1f, 0.1f, 0.1f);
        glut.glutSolidDodecahedron();
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.06f, 0.58f, -4f);
        gl.glColor3f(214 / 255f, 204 / 255f, 191 / 255f);
        gl.glScalef(0.13f, 0.13f, 0.13f);
        glut.glutSolidDodecahedron();
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.03f, 0.5f, -3.97f);
        gl.glColor3f(0f, 0f, 0f);
        gl.glScalef(0.13f, 0.13f, 0.13f);
        glut.glutSolidDodecahedron();
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.2f, 0.55f, -3.97f);
        gl.glColor3f(0f, 0f, 0f);
        gl.glScalef(0.17f, 0.17f, 0.17f);
        glut.glutSolidDodecahedron();
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.24f, 0.52f, -3.66f);
        gl.glColor3f(48 / 255f, 150 / 255f, 95 / 255f);
        gl.glScalef(0.1f, 0.1f, 0.1f);
        glut.glutSolidDodecahedron();
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.27f, 0.54f, -3.99f);
        gl.glColor3f(67 / 255f, 87 / 255f, 114 / 255f);
        gl.glScalef(0.1f, 0.1f, 0.1f);
        glut.glutSolidDodecahedron();
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.29f, 0.54f, -4.4f);
        gl.glColor3f(20 / 255f, 183 / 255f, 236 / 255f);
        gl.glScalef(0.22f, 0.4f, 0.22f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.77f, -0.5f, -3.7f);
        gl.glColor3f(0f, 0f, 0f);
        gl.glScalef(0.17f, 0.17f, 0.17f);
        glut.glutSolidDodecahedron();
        gl.glPopMatrix();

        // ------ 
        //front side
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.0f, -3.49f);
        gl.glColor3f(30 / 253f, 85 / 253f, 31 / 253f);
        gl.glScalef(1, 1, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //back side
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -4.49f);
        gl.glColor3f(30 / 253f, 85 / 253f, 31 / 253f);
        gl.glScalef(1, 1, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //top side
        gl.glPushMatrix();
        gl.glTranslatef(-0.18f, 0.92f, -4.0f);
        gl.glColor3f(30 / 253f, 85 / 253f, 31 / 253f);
        gl.glRotatef(90, 1, 0, 0);
        gl.glRotatef(50, 0, 1, 0);
        gl.glScalef(1, 1, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glColor3f(15 / 253f, 40 / 253f, 16 / 253f);

        //joint 1
        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 0.5f, -4.33f);
        glut.glutSolidTorus(0.025, 0.07, 10, 25);
        gl.glPopMatrix();

        //joint 2
        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 0.5f, -3.65f);
        glut.glutSolidTorus(0.025, 0.07, 10, 25);
        gl.glPopMatrix();

        gl.glColor3f(0, 0, 0);

        //wheel 1
        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, -0.60f, -3.65f);
        glut.glutSolidTorus(0.060, 0.07, 10, 25);
        gl.glPopMatrix();

        //wheel 2
        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, -0.59f, -4.33f);
        glut.glutSolidTorus(0.060, 0.07, 10, 25);
        gl.glPopMatrix();

        // wheel 3
        gl.glPushMatrix();
        gl.glTranslatef(0.35f, -0.60f, -3.65f);
        glut.glutSolidTorus(0.060, 0.07, 10, 25);
        gl.glPopMatrix();

        // wheel 4
        gl.glPushMatrix();
        gl.glTranslatef(0.35f, -0.60f, -4.33f);
        glut.glutSolidTorus(0.060, 0.07, 10, 25);
        gl.glPopMatrix();

        gl.glColor3f(15 / 253f, 40 / 253f, 16 / 253f);

        //boarder 1
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.5f, -3.5f);
        gl.glScalef(1, 0.05f, 0.10f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //boarder 2
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.5f, -4.5f);
        gl.glScalef(1, 0.05f, 0.10f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //boarder 3
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 0.5f, -4f);
        gl.glScalef(0.10f, 0.05f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();
    }
    
    // car ---------------------------------------------------------------------

    public static void drawCar(GL gl, GLUT glut, float r, float g, float b) {
        /* OBJECT MODULE*/
 /* top of cube*/
        //************************FRONT BODY****************************************
        gl.glColor3f(r, g, b);
        float[][] fb_top = {{0.2f, 0.4f, 0.6f},
        {0.6f, 0.5f, 0.6f},
        {0.6f, 0.5f, 0.2f},
        {0.2f, 0.4f, 0.2f}};
        drawQuads(gl, fb_top);
        /* bottom of cube*/
        float[][] fb_bottom = {{0.2f, 0.2f, 0.6f},
        {0.6f, 0.2f, 0.6f},
        {0.6f, 0.2f, 0.2f},
        {0.2f, 0.2f, 0.2f}};
        drawQuads(gl, fb_bottom);
        /* front of cube*/
        float[][] fb_front = {{0.2f, 0.2f, 0.6f},
        {0.2f, 0.4f, 0.6f},
        {0.2f, 0.4f, 0.2f},
        {0.2f, 0.2f, 0.2f}};
        drawQuads(gl, fb_front);
        /* back of cube.*/
        float[][] fb_back = {{0.6f, 0.2f, 0.6f},
        {0.6f, 0.5f, 0.6f},
        {0.6f, 0.5f, 0.2f},
        {0.6f, 0.2f, 0.2f}};
        drawQuads(gl, fb_back);
        /* left of cube*/
        float[][] fb_left = {{0.2f, 0.2f, 0.6f},
        {0.6f, 0.2f, 0.6f},
        {0.6f, 0.5f, 0.6f},
        {0.2f, 0.4f, 0.6f}};
        drawQuads(gl, fb_left);
        /* Right of cube */
        float[][] fb_right = {{0.2f, 0.2f, 0.2f},
        {0.6f, 0.2f, 0.2f},
        {0.6f, 0.5f, 0.2f},
        {0.2f, 0.4f, 0.2f}};
        drawQuads(gl, fb_right);
//****************************************************************************
        //top cover
        float[][] fb_top_cover = {{0.7f, 0.65f, 0.6f},
        {0.7f, 0.65f, 0.2f},
        {1.7f, 0.65f, 0.2f},
        {1.7f, 0.65f, 0.6f}};
        drawQuads(gl, fb_top_cover);
//***************************back guard******************************
        gl.glColor3f(r, g, b);
        float[][] bk = {{1.8f, 0.5f, 0.6f},
        {1.8f, 0.5f, 0.2f},
        {2.1f, 0.4f, 0.2f},
        {2.1f, 0.4f, 0.6f}};
        drawQuads(gl, bk);
        /* bottom of cube*/
        float[][] bk_bottom = {{2.1f, 0.2f, 0.6f},
        {2.1f, 0.2f, 0.2f},
        {1.8f, 0.2f, 0.6f},
        {1.8f, 0.2f, 0.6f}};
        drawQuads(gl, bk_bottom);
        /* back of cube.*/
        float[][] bk_back = {{2.1f, 0.4f, 0.6f},
        {2.1f, 0.4f, 0.2f},
        {2.1f, 0.2f, 0.2f},
        {2.1f, 0.2f, 0.6f}};
        drawQuads(gl, bk_back);
        /* left of cube*/
        float[][] bk_left = {{1.8f, 0.2f, 0.2f},
        {1.8f, 0.5f, 0.2f},
        {2.1f, 0.4f, 0.2f},
        {2.1f, 0.2f, 0.2f}};
        drawQuads(gl, bk_left);
        /* Right of cube */
        float[][] bk_right = {{1.8f, 0.2f, 0.6f},
        {1.8f, 0.5f, 0.6f},
        {2.1f, 0.4f, 0.6f},
        {2.1f, 0.2f, 0.6f}};
        drawQuads(gl, bk_right);
//******************MIDDLE BODY************************************
        float[][] mb = {{0.6f, 0.5f, 0.6f},
        {0.6f, 0.2f, 0.6f},
        {1.8f, 0.2f, 0.6f},
        {1.8f, 0.5f, 0.6f}};
        drawQuads(gl, mb);
        /* bottom of cube*/
        float[][] mb_bottom = {{0.6f, 0.2f, 0.6f},
        {0.6f, 0.2f, 0.2f},
        {1.8f, 0.2f, 0.2f},
        {1.8f, 0.2f, 0.6f}};
        drawQuads(gl, mb_bottom);
        /* back of cube.*/
        float[][] mb_back = {{0.6f, 0.5f, 0.2f},
        {0.6f, 0.2f, 0.2f},
        {1.8f, 0.2f, 0.2f},
        {1.8f, 0.5f, 0.2f}};
        drawQuads(gl, mb_back);
//*********************ENTER WINDOW**********************************
        gl.glColor3f(0.3f, 0.3f, 0.3f);
        //quad front window
        float[][] front_window = {{0.77f, 0.63f, 0.2f},
        {0.75f, 0.5f, 0.2f},
        {1.2f, 0.5f, 0.2f},
        {1.22f, 0.63f, 0.2f}};
        drawQuads(gl, front_window);
        //quad back window
        float[][] back_window = {{1.27f, 0.63f, .2f},
        {1.25f, 0.5f, 0.2f},
        {1.65f, 0.5f, 0.2f},
        {1.67f, 0.63f, 0.2f}};
        drawQuads(gl, back_window);
        gl.glColor3f(r, g, b);
        //first separation
        float[][] first_seperation = {{0.7f, 0.65f, 0.2f},
        {0.7f, 0.5f, .2f},
        {0.75f, 0.5f, 0.2f},
        {0.77f, 0.65f, 0.2f}};
        drawQuads(gl, first_seperation);
        //second separation
        float[][] second_seperation = {{1.2f, 0.65f, 0.2f},
        {1.2f, 0.5f, .2f},
        {1.25f, 0.5f, 0.2f},
        {1.27f, 0.65f, 0.2f}};
        drawQuads(gl, second_seperation);
        //3d separation
        float[][] third_seperation = {{1.65f, 0.65f, 0.2f},
        {1.65f, 0.5f, .2f},
        {1.7f, 0.5f, 0.2f},
        {1.7f, 0.65f, 0.2f}};
        drawQuads(gl, third_seperation);
        //line strip
        float[][] line_strip = {{0.75f, 0.65f, 0.2f},
        {0.75f, 0.63f, 0.2f},
        {1.7f, 0.63f, 0.2f},
        {1.7f, 0.65f, 0.2f}};
        drawQuads(gl, line_strip);
        //line strip
        float[][] line_strip2 = {{0.75f, 0.65f, 0.6f},
        {0.75f, 0.63f, 0.6f},
        {1.7f, 0.63f, 0.6f},
        {1.7f, 0.65f, 0.6f}};
        drawQuads(gl, line_strip2);
        gl.glColor3f(0.3f, 0.3f, 0.3f);
        //quad front window
        float[][] front_window2 = {{0.77f, 0.63f, 0.6f},
        {0.75f, 0.5f, 0.6f},
        {1.2f, 0.5f, 0.6f},
        {1.22f, 0.63f, 0.6f}};
        drawQuads(gl, front_window2);
        //quad back window
        float[][] back_window2 = {{1.27f, 0.63f, .6f},
        {1.25f, 0.5f, 0.6f},
        {1.65f, 0.5f, 0.6f},
        {1.67f, 0.63f, 0.6f}};
        drawQuads(gl, back_window2);
        gl.glColor3f(r, g, b);
        //first separation
        float[][] first_seperation2 = {{0.7f, 0.65f, 0.6f},
        {0.7f, 0.5f, .6f},
        {1.75f, 0.5f, 0.6f},
        {0.77f, 0.65f, 0.6f}};
        drawQuads(gl, first_seperation2);
        //second separation
        float[][] second_seperation2 = {{1.2f, 0.65f, 0.6f},
        {1.2f, 0.5f, .6f},
        {1.25f, 0.5f, 0.6f},
        {1.27f, 0.65f, 0.6f}};
        drawQuads(gl, second_seperation2);
        float[][] third_seperation2 = {{1.65f, 0.65f, 0.6f},
        {1.65f, 0.5f, .6f},
        {1.7f, 0.5f, 0.6f},
        {1.7f, 0.65f, 0.6f}};
        drawQuads(gl, third_seperation2);
        /* top of cube*/
        gl.glColor3f(0.3f, 0.3f, 0.3f);
        //quad front window
        float[][] top_front_window = {{0.6f, 0.5f, 0.6f},
        {0.6f, 0.5f, 0.2f},
        {0.7f, 0.65f, 0.2f},
        {0.7f, 0.65f, 0.6f}};
        drawQuads(gl, top_front_window);
        //quad back window
        float[][] top_back_window = {{1.7f, 0.65f, .6f},
        {1.7f, 0.65f, 0.2f},
        {1.8f, 0.5f, 0.2f},
        {1.8f, 0.5f, 0.6f}};
        drawQuads(gl, top_back_window);
        /* start drawing the cube.*/
 /* top of cube*/
        gl.glColor3f(0.3f, 0.3f, 0.3f);
        //tri front window
        float[][] tri_front_window = {{0.6f, 0.5f, 0.6f},
        {0.7f, 0.65f, 0.6f},
        {0.7f, 0.5f, 0.6f},
        {0.6f, 0.5f, 0.2f},
        {0.7f, 0.65f, 0.2f},
        {0.7f, 0.5f, 0.2f}};
        drawTriangles(gl, tri_front_window);
        //tri back window
        float[][] tri_back_window = {{1.7f, 0.65f, 0.2f},
        {1.8f, 0.5f, 0.2f},
        {1.7f, 0.5f, 0.2f},
        {1.7f, 0.65f, 0.6f},
        {1.8f, 0.5f, 0.6f},
        {1.7f, 0.5f, 0.6f}};
        drawTriangles(gl, tri_back_window);
        float angle = 0;
        gl.glColor3f(0.7f, 0.7f, 0.7f);
        gl.glPushMatrix();
        float[][] tires = {{0.6f, 0.2f, 0.62f},
        {0.6f, 0.2f, 0.18f},
        {1.7f, 0.2f, 0.18f},
        {1.7f, 0.2f, 0.62f}};
        drawLineStrip(gl, tires, angle);
        gl.glPopMatrix();
        // tires
        gl.glTranslatef(0.6f, 0.2f, 0.6f);
        gl.glColor3f(0, 0, 0);
        glut.glutSolidTorus(0.025, 0.07, 10, 25);
        gl.glTranslatef(0, 0, -0.4f);
        glut.glutSolidTorus(0.025, 0.07, 10, 25);
        gl.glTranslatef(1.1f, 0, 0);
        glut.glutSolidTorus(0.025, 0.07, 10, 25);
        gl.glTranslatef(0, 0, 0.4f);
        glut.glutSolidTorus(0.025, 0.07, 10, 25);
    }

    public static void drawQuads(GL gl, float[][] array) {
        gl.glBegin(gl.GL_QUADS);
        for (int i = 0; i < array.length; i++) {
            gl.glVertex3f(array[i][0], array[i][1], array[i][2]);
        }
        gl.glEnd();
    }

    public static void drawTriangles(GL gl, float[][] array) {
        gl.glBegin(gl.GL_TRIANGLES);
        for (int i = 0; i < array.length; i++) {
            gl.glVertex3f(array[i][0], array[i][1], array[i][2]);
        }
        gl.glEnd();
    }

    public static void drawLineStrip(GL gl, float[][] array, float angle) {
        for (int i = 0; i < array.length; i++) {
            gl.glBegin(gl.GL_LINE_STRIP);
            for (float theta = 0; theta < 360; theta = theta + 20) {
                gl.glVertex3f(array[i][0], array[i][1], array[i][2]);
                gl.glVertex3f(array[i][0] + (float) (0.08f * (Math.cos(((theta + angle) * 3.14f) / 180))), (float) (array[i][1] + (0.08f * (Math.sin(((theta + angle) * 3.14f) / 180)))), array[i][2]);
            }
            gl.glEnd();
        }
    }

    // Tall Building -----------------------------------------------------------
    
    public void TallBuilding(GLUT glut, GL gl, int num, int bs1, int bs2, int bs3) {

        gl.glPushMatrix();

        gl.glEnable(GL_DEPTH_TEST);
        
        // Building Structure
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -15f);
        gl.glColor3f(bs1 / 255f, bs2 / 255f, bs3 / 255f);
        gl.glScalef(4, 4.5f, 2);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // roof
        gl.glPushMatrix();
        gl.glTranslatef(0f, 2.25f, -15f);
        gl.glColor3f(166 / 255f, 130 / 255f, 106 / 255f);
        gl.glScalef(4, 0.01f, 2);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Back 
        gl.glPushMatrix();
        gl.glTranslatef(0f, 2.32f, -15.97f);
        gl.glColor3f(bs1 / 255f, bs2 / 255f, bs2 / 255f);
        gl.glScalef(4, 0.15f, 0.09f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Left 
        gl.glPushMatrix();
        gl.glTranslatef(-1.95f, 2.32f, -15.03f);
        gl.glColor3f(bs1 / 255f, bs2 / 255f, bs2 / 255f);
        gl.glScalef(0.09f, 0.15f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Right 
        gl.glPushMatrix();
        gl.glTranslatef(1.95f, 2.32f, -15.03f);
        gl.glColor3f(bs1 / 255f, bs2 / 255f, bs2 / 255f);
        gl.glScalef(0.09f, 0.15f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //water tanks and satellites
        roofStuff(glut, gl, num);

        // Front 
        gl.glPushMatrix();
        gl.glTranslatef(0f, 2.32f, -14.03f);
        gl.glColor3f(bs1 / 255f, bs2 / 255f, bs2 / 255f);
        gl.glScalef(4, 0.15f, 0.09f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // AC
        // Right side
        gl.glPushMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 1.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, 1.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 0.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, 0.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, -0.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, -0.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, -1.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, -1.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 1.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, 1.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 0.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, 0.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, -0.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, -0.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, -1.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, -1.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Left side
        gl.glPushMatrix();

        gl.glTranslatef(4.25f, 0, 0);

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 1.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, 1.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 0.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, 0.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, -0.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, -0.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, -1.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, -1.5f, -15.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 1.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, 1.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 0.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, 0.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, -0.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, -0.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, -1.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, -1.5f, -14.5f);
        gl.glColor3f(106 / 255f, 97 / 255f, 90 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        shops(glut,gl, num);

        // windows
        // FRONT
        // Right , above
        gl.glPushMatrix();

        gl.glPushMatrix();

        gl.glTranslatef(1.2f, 1.5f, 0);

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -14f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(0.57f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.15f, 0f, -13.99f);
        gl.glColor3f(203 / 255f, 203 / 255f, 203 / 255f);
        gl.glScalef(0.25f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Right , below
        gl.glPushMatrix();

        gl.glTranslatef(1.2f, 0.2f, 0);

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -14f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(0.57f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.15f, 0f, -13.99f);
        gl.glColor3f(203 / 255f, 203 / 255f, 203 / 255f);
        gl.glScalef(0.25f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Left , below
        gl.glPushMatrix();

        gl.glTranslatef(-1.2f, 0.2f, 0);

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -14f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(0.57f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.15f, 0f, -13.99f);
        gl.glColor3f(203 / 255f, 203 / 255f, 203 / 255f);
        gl.glScalef(0.25f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Left , above
        gl.glPushMatrix();

        gl.glTranslatef(-1.2f, 1.5f, 0);

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -14f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(0.57f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.15f, 0f, -13.99f);
        gl.glColor3f(203 / 255f, 203 / 255f, 203 / 255f);
        gl.glScalef(0.25f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Middle, above
        gl.glPushMatrix();

        gl.glTranslatef(0f, 1.5f, 0);

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -14f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(0.57f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.15f, 0f, -13.99f);
        gl.glColor3f(203 / 255f, 203 / 255f, 203 / 255f);
        gl.glScalef(0.25f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Middle, below
        gl.glPushMatrix();

        gl.glTranslatef(0f, 0.2f, 0);

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -14f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(0.57f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.15f, 0f, -13.99f);
        gl.glColor3f(203 / 255f, 203 / 255f, 203 / 255f);
        gl.glScalef(0.25f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPopMatrix();

        // BACK
        // Right , above
        gl.glPushMatrix();

        gl.glTranslatef(0f, 0f, -2f);

        gl.glPushMatrix();

        gl.glTranslatef(1.2f, 1.5f, 0);

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -14f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(0.57f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.15f, 0f, -13.99f);
        gl.glColor3f(203 / 255f, 203 / 255f, 203 / 255f);
        gl.glScalef(0.25f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Right , below
        gl.glPushMatrix();

        gl.glTranslatef(1.2f, 0.2f, 0);

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -14f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(0.57f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.15f, 0f, -13.99f);
        gl.glColor3f(203 / 255f, 203 / 255f, 203 / 255f);
        gl.glScalef(0.25f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Left , below
        gl.glPushMatrix();

        gl.glTranslatef(-1.2f, 0.2f, 0);

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -14f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(0.57f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.15f, 0f, -13.99f);
        gl.glColor3f(203 / 255f, 203 / 255f, 203 / 255f);
        gl.glScalef(0.25f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Left , above
        gl.glPushMatrix();

        gl.glTranslatef(-1.2f, 1.5f, 0);

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -14f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(0.57f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.15f, 0f, -13.99f);
        gl.glColor3f(203 / 255f, 203 / 255f, 203 / 255f);
        gl.glScalef(0.25f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Middle, above
        gl.glPushMatrix();

        gl.glTranslatef(0f, 1.5f, 0);

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -14f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(0.57f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.15f, 0f, -13.99f);
        gl.glColor3f(203 / 255f, 203 / 255f, 203 / 255f);
        gl.glScalef(0.25f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Middle, below
        gl.glPushMatrix();

        gl.glTranslatef(0f, 0.2f, 0);

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -14f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(0.57f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.15f, 0f, -13.99f);
        gl.glColor3f(203 / 255f, 203 / 255f, 203 / 255f);
        gl.glScalef(0.25f, 0.57f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.6f, 0.04f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 0f, -14f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.04f, 0.62f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPopMatrix();

        // Door
        gl.glPushMatrix();

        gl.glTranslatef(0f, -1.8f, -2);

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -14f);
        gl.glColor3f(0f, 0f, 0f);
        gl.glScalef(1.5f, 0.9f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.36f, -0.05f, -14.01f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(.69f, 0.8f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.36f, -0.05f, -14.01f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(.69f, 0.8f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();
        gl.glDisable(GL_DEPTH_TEST);

        gl.glPopMatrix();

    }
    
    public void shops (GLUT glut, GL gl, int num){
        
        if(num == 1){
            
            // Qirtasiah
        gl.glPushMatrix();

        gl.glPushMatrix();

        gl.glTranslatef(-0.7f, 0f, 0f);

        // Qirtasiah's Door
        gl.glPushMatrix();
        gl.glTranslatef(0f, -1.74f, -14f);
        gl.glColor3f(0 / 255f, 150 / 255f, 199 / 255f);
        gl.glScalef(1.5f, 1.03f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        // Door's glass
        gl.glPushMatrix();
        gl.glTranslatef(0.37f, -1.74f, -13.96f);
        gl.glColor3f(176 / 255f, 176 / 255f, 176 / 255f);
        gl.glScalef(0.7f, 0.98f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.37f, -1.74f, -13.96f);
        gl.glColor3f(175 / 255f, 169 / 255f, 158 / 255f);
        gl.glScalef(0.7f, 0.98f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPopMatrix();

        // SHOP LOHAH
        gl.glPushMatrix();

        gl.glTranslatef(-0.7f, 0f, 0f);

        // Shop name
        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.97f, -14f);
        gl.glColor3f(236 / 255f, 144 / 255f, 0f);
        gl.glScalef(2f, 0.5f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1f, -1.23f, -13.96f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1f, 1f, 1f);
        Qurtasiah.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(2f, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(2f, 0.5f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 0.5f);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPopMatrix();

        // Asha'ab 
        gl.glPushMatrix();

        gl.glTranslatef(1.1f, 0f, 0f);

        // Qirtasiah's Door
        gl.glPushMatrix();
        gl.glTranslatef(0f, -1.74f, -14f);
        gl.glColor3f(129 / 255f, 49 / 255f, 171 / 255f);
        gl.glScalef(0.9f, 1.03f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Door's glass
        gl.glPushMatrix();
        gl.glTranslatef(0f, -1.74f, -13.96f);
        gl.glColor3f(171 / 255f, 171 / 255f, 171 / 255f);
        gl.glScalef(0.83f, 0.98f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPushMatrix();

        // SHOP LOHAH
        gl.glPushMatrix();

        gl.glTranslatef(1.1f, 0f, 0f);

        // Shop name
        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.97f, -14f);
        gl.glColor3f(1f, 0f, 0f);
        gl.glScalef(1.4f, 0.5f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.29f, -1.23f, -13.96f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1f, 1f, 1f);
        Mathanah.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0.6f, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(2f, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(2f, 0.5f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0.6f, 0.5f);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPushMatrix();

        gl.glTranslatef(1.55f, 0f, 0f);

        // That small box's tube
        gl.glPushMatrix();
        gl.glTranslatef(-1.2f, -2.15f, -14f);
        gl.glColor3f(185 / 255f, 185 / 255f, 185 / 255f);
        gl.glScalef(0.07f, 0.25f, 0.07f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // That small box
        gl.glPushMatrix();
        gl.glTranslatef(-1.2f, -1.73f, -14f);
        gl.glColor3f(237 / 255f, 236 / 255f, 204 / 255f);
        gl.glScalef(0.4f, 0.6f, 0.1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // That small box's stikers
        gl.glPushMatrix();
        gl.glTranslatef(-1.2f, -1.61f, -13.96f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.18f, 0.1f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.2f, -1.8f, -13.96f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(0.2f, 0.21f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();
        
        } else if (num == 2){
            
             // Begala's Door
        gl.glPushMatrix();
        gl.glTranslatef(0f, -1.74f, -14f);
        gl.glColor3f(132 / 255f, 166 / 255f, 123 / 255f);
        gl.glScalef(1.12f, 1.03f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Door's glass
        gl.glPushMatrix();
        gl.glTranslatef(0.27f, -1.74f, -13.96f);
        gl.glColor3f(175 / 255f, 169 / 255f, 158 / 255f);
        gl.glScalef(0.5f, 0.98f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.27f, -1.74f, -13.96f);
        gl.glColor3f(175 / 255f, 169 / 255f, 158 / 255f);
        gl.glScalef(0.5f, 0.98f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // ???? ???????
        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.97f, -14f);
        gl.glColor3f(0f, 12 / 255f, 1f);
        gl.glScalef(2f, 0.5f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1f, -1.23f, -13.96f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1f, 1f, 1f);
        flowers.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(2f, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(2f, 0.5f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 0.5f);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glPopMatrix();

        // That small box's tube
        gl.glPushMatrix();
        gl.glTranslatef(-1.2f, -2.15f, -14f);
        gl.glColor3f(185 / 255f, 185 / 255f, 185 / 255f);
        gl.glScalef(0.07f, 0.25f, 0.07f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // That small box
        gl.glPushMatrix();
        gl.glTranslatef(-1.2f, -1.73f, -14f);
        gl.glColor3f(237 / 255f, 236 / 255f, 204 / 255f);
        gl.glScalef(0.4f, 0.6f, 0.1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // That small box's stikers
        gl.glPushMatrix();
        gl.glTranslatef(-1.2f, -1.61f, -13.96f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.18f, 0.1f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.2f, -1.8f, -13.96f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(0.2f, 0.21f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Street Name ????
        gl.glPushMatrix();
        gl.glTranslatef(1.4f, -1.5f, -13.96f);
        gl.glColor3f(20 / 255f, 106 / 255f, 16 / 255f);
        gl.glScalef(0.5f, 0.21f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        
        } else {
            
            // Begala's Door
        gl.glPushMatrix();
        gl.glTranslatef(0f, -1.74f, -14f);
        gl.glColor3f(132 / 255f, 166 / 255f, 123 / 255f);
        gl.glScalef(1.12f, 1.03f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Door's glass
        gl.glPushMatrix();
        gl.glTranslatef(0.27f, -1.74f, -14f);
        gl.glColor3f(175 / 255f, 169 / 255f, 158 / 255f);
        gl.glScalef(0.5f, 0.98f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.27f, -1.74f, -14f);
        gl.glColor3f(175 / 255f, 169 / 255f, 158 / 255f);
        gl.glScalef(0.5f, 0.98f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // ???? ???????
        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.97f, -14f);
        gl.glColor3f(0f, 12 / 255f, 1f);
        gl.glScalef(2f, 0.5f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1f, -1.23f, -13.96f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1f, 1f, 1f);
        Beglah.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(2f, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(2f, 0.5f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 0.5f);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glPopMatrix();

        // That small box's tube
        gl.glPushMatrix();
        gl.glTranslatef(-1.2f, -2.15f, -14f);
        gl.glColor3f(185 / 255f, 185 / 255f, 185 / 255f);
        gl.glScalef(0.07f, 0.25f, 0.07f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // That small box
        gl.glPushMatrix();
        gl.glTranslatef(-1.2f, -1.73f, -14f);
        gl.glColor3f(237 / 255f, 236 / 255f, 204 / 255f);
        gl.glScalef(0.4f, 0.6f, 0.1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // That small box's stikers
        gl.glPushMatrix();
        gl.glTranslatef(-1.2f, -1.61f, -13.96f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(0.18f, 0.1f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.2f, -1.8f, -13.96f);
        gl.glColor3f(179 / 255f, 179 / 255f, 179 / 255f);
        gl.glScalef(0.2f, 0.21f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Street Name ????
        gl.glPushMatrix();
        gl.glTranslatef(1.4f, -1.5f, -13.96f);
        gl.glColor3f(20 / 255f, 106 / 255f, 16 / 255f);
        gl.glScalef(0.53f, 0.24f, 0.02f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        
        }
        
    }
    
    public void roofStuff (GLUT glut, GL gl, int num){
        
        if (num == 1){
            
            //water tanks
        gl.glPushMatrix();
        gl.glTranslatef(-0.7f, 2.5f, -14.5f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.2f, .4f, .5f);
        gl.glRotatef(90, 0, 1, 0);
        glut.glutSolidCylinder(.5f, 2f, 30, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 2.5f, -15.3f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.2f, .4f, .5f);
        gl.glRotatef(40, 0, 1, 0);
        glut.glutSolidCylinder(.5f, 2f, 30, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 2.5f, -15.3f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.2f, .4f, .5f);
        gl.glRotatef(20, 0, 1, 0);
        glut.glutSolidCylinder(.5f, 2f, 30, 15);
        gl.glPopMatrix();

        // satellite
        gl.glPushMatrix();
        gl.glTranslatef(1f, 2.5f, -14.9f);
        gl.glScalef(0.15f, 0.15f, 0.15f);
        gl.glRotatef(320, 0, 1, 0);
        satellite(gl, glut, 87, 85, 86, 78, 76, 77, 0 , 102, 137);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.7f, 2.5f, -14.9f);
        gl.glScalef(0.15f, 0.15f, 0.15f);
        gl.glRotatef(340, 0, 1, 0);
        satellite(gl, glut, 87, 85, 86, 78, 76, 77, 0 , 102, 137);
        gl.glPopMatrix();
            
        } else if (num == 2){
            
            //water tanks
        gl.glPushMatrix();
        gl.glTranslatef(-0.7f, 2.5f, -14.5f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.2f, .4f, .5f);
        gl.glRotatef(90, 0, 1, 0);
        glut.glutSolidCylinder(.5f, 2f, 30, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 2.5f, -15.3f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.2f, .4f, .5f);
        gl.glRotatef(40, 0, 1, 0);
        glut.glutSolidCylinder(.5f, 2f, 30, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 2.5f, -15.3f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.2f, .4f, .5f);
        gl.glRotatef(20, 0, 1, 0);
        glut.glutSolidCylinder(.5f, 2f, 30, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.7f, 2.5f, -14.9f);
        gl.glScalef(0.15f, 0.15f, 0.15f);
        gl.glRotatef(340, 0, 1, 0);
        satellite(gl, glut, 87, 85, 86, 78, 76, 77, 0 , 198, 137);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1f, 2.5f, -15.5f);
        gl.glScalef(0.15f, 0.15f, 0.15f);
        gl.glRotatef(250, 0, 1, 0);
        satellite(gl, glut, 87, 85, 86, 78, 76, 77, 0 , 158, 137);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1f, 2.5f, -15.5f);
        gl.glScalef(0.15f, 0.15f, 0.15f);
        gl.glRotatef(150, 0, 1, 0);
        satellite(gl, glut, 87, 85, 86, 78, 76, 77, 0 , 158, 137);
        gl.glPopMatrix();
        
        } else{
            
            //water tanks
        gl.glPushMatrix();
        gl.glTranslatef(-0.7f, 2.3f, -14.5f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.2f, .4f, .5f);
        gl.glRotatef(90, 0, 1, 0);
        glut.glutSolidCylinder(.5f, 2f, 30, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 2.3f, -15.3f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.2f, .4f, .5f);
        gl.glRotatef(40, 0, 1, 0);
        glut.glutSolidCylinder(.52f, 2f, 10, 5);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 2.3f, -15.1f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.2f, .4f, .5f);
        gl.glRotatef(180, 0, 1, 0);
        glut.glutSolidCylinder(.52f, 1f, 10, 5);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.5f, 2.3f, -14.5f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.2f, .4f, .5f);
        gl.glRotatef(180, 0, 1, 0);
        glut.glutSolidCylinder(.52f, 1f, 10, 5);
        gl.glPopMatrix();

        // sat 
        gl.glPushMatrix();
        gl.glTranslatef(1f, 2.5f, -14.9f);
        gl.glScalef(0.15f, 0.15f, 0.15f);
        gl.glRotatef(320, 0, 1, 0);
        satellite(gl, glut, 87, 85, 86, 78, 76, 77, 0 , 102, 137);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1f, 2.5f, -14.9f);
        gl.glScalef(0.15f, 0.15f, 0.15f);
        gl.glRotatef(340, 0, 1, 0);
        satellite(gl, glut, 87, 85, 86, 78, 76, 77, 0 , 158, 137);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.6f, 2.5f, -15.3f);
        gl.glScalef(0.15f, 0.15f, 0.15f);
        gl.glRotatef(395, 0, 1, 0);
        satellite(gl, glut, 87, 85, 86, 78, 76, 77, 0 , 198, 137);
        gl.glPopMatrix();
        
        }
    }
    
    // school ------------------------------------------------------------------
    
    public static void oldSchool(GLUT glut, GL gl) {

        gl.glPushMatrix();

        // BUILDING 1 ----------------------------------------------------------
        gl.glPushMatrix();
        gl.glEnable(GL_BLEND);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glTranslatef(0f, 0.4f, -15f);
        gl.glColor3f(197 / 255f, 155 / 255f, 107 / 255f);
        gl.glScalef(4, 3f, 2);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 2f, -15f);
        gl.glColor3f(189 / 255f, 146 / 255f, 98 / 255f);
        gl.glScalef(4, 0f, 2);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // ROOF ----------------------------------------------------------------
        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.95f, -15.9f);
        gl.glColor3f(197 / 255f, 155 / 255f, 107 / 255f);
        gl.glScalef(4, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.9f, 1.95f, -15f);
        gl.glColor3f(197 / 255f, 155 / 255f, 107 / 255f);
        gl.glScalef(0.2f, 0.2f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.9f, 1.95f, -15f);
        gl.glColor3f(197 / 255f, 155 / 255f, 107 / 255f);
        gl.glScalef(0.2f, 0.2f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.95f, -14.11f);
        gl.glColor3f(197 / 255f, 155 / 255f, 107 / 255f);
        gl.glScalef(4, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // WINDOWS -------------------------------------------------------------
        // FRONT
        // left below
        gl.glPushMatrix();

        gl.glTranslatef(-3f, 0f, 0f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // left middle
        gl.glPushMatrix();

        gl.glTranslatef(-3f, 1f, 0f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // left above
        gl.glPushMatrix();

        gl.glTranslatef(-3f, 2f, 0f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // middle above
        gl.glPushMatrix();

        gl.glTranslatef(-2f, 2f, 0f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // middle above
        gl.glPushMatrix();

        gl.glTranslatef(-1f, 2f, 0f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // right above
        gl.glPushMatrix();

        gl.glTranslatef(0f, 2f, 0f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // right middle  
        gl.glPushMatrix();

        gl.glTranslatef(0f, 1f, 0f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // right below
        gl.glPushMatrix();

        gl.glTranslatef(0f, 0f, 0f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // FRONT BUILDING ------------------------------------------------------
        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.1f, -13.79f);
        gl.glColor3f(197 / 255f, 155 / 255f, 107 / 255f);
        gl.glScalef(2, 2f, 0.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // WINDOWS CONTINUE  ... -----------------------------------------------
        // ABOVE THE DOOR
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -13.53f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(1.3f, 0.5f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -13.5f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(1.2f, 0.4f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // LEFT SIDE
        //right below
        gl.glPushMatrix();

        gl.glTranslatef(-3.51f, 0f, -0.48f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.001f, 0.7f, 0.53f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        //right middle
        gl.glPushMatrix();

        gl.glTranslatef(-3.51f, 1f, -0.48f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.001f, 0.7f, 0.53f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        //right above
        gl.glPushMatrix();

        gl.glTranslatef(-3.51f, 2f, -0.48f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.001f, 0.7f, 0.53f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        //left above
        gl.glPushMatrix();

        gl.glTranslatef(-3.51f, 2f, -1.55f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.001f, 0.7f, 0.53f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        //left middle
        gl.glPushMatrix();

        gl.glTranslatef(-3.51f, 1f, -1.55f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.001f, 0.7f, 0.53f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        //left middle
        gl.glPushMatrix();

        gl.glTranslatef(-3.51f, 0f, -1.55f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.001f, 0.7f, 0.53f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // RIGHT SIDE
        //right below
        gl.glPushMatrix();

        gl.glTranslatef(0.5f, 0f, -0.48f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.001f, 0.7f, 0.53f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        //right middle
        gl.glPushMatrix();

        gl.glTranslatef(0.5f, 1f, -0.48f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.001f, 0.7f, 0.53f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        //right above
        gl.glPushMatrix();

        gl.glTranslatef(0.5f, 2f, -0.48f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.001f, 0.7f, 0.53f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        //left above
        gl.glPushMatrix();

        gl.glTranslatef(0.5f, 2f, -1.55f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.001f, 0.7f, 0.53f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        //left middle
        gl.glPushMatrix();

        gl.glTranslatef(0.5f, 1f, -1.55f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.001f, 0.7f, 0.53f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        //left middle
        gl.glPushMatrix();

        gl.glTranslatef(0.5f, 0f, -1.55f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.001f, 0.7f, 0.53f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.01f, 0.25f, 0.48f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // BACK (WINDOW CONTINUE...) 
        // first row
        gl.glPushMatrix();

        // Right 
        gl.glPushMatrix();

        gl.glTranslatef(-3f, 2f, -2f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Middle 
        gl.glPushMatrix();

        gl.glTranslatef(-1.5f, 2f, -2f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // left 
        gl.glPushMatrix();

        gl.glTranslatef(0f, 2f, -2f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPopMatrix();

        // second row
        gl.glPushMatrix();

        gl.glTranslatef(0f, -1f, 0f);

        // Right 
        gl.glPushMatrix();

        gl.glTranslatef(-3f, 2f, -2f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Middle 
        gl.glPushMatrix();

        gl.glTranslatef(-1.5f, 2f, -2f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // left 
        gl.glPushMatrix();

        gl.glTranslatef(-0f, 2f, -2f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPopMatrix();

        // third row
        gl.glPushMatrix();

        gl.glTranslatef(0f, -2f, 0f);

        // Right 
        gl.glPushMatrix();

        gl.glTranslatef(-3f, 2f, -2f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Middle 
        gl.glPushMatrix();

        gl.glTranslatef(-1.5f, 2f, -2f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // left 
        gl.glPushMatrix();

        gl.glTranslatef(-0f, 2f, -2f);

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.6f, -14f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(0.53f, 0.7f, 0.001f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.44f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.5f, -0.76f, -14f);
        gl.glColor3f(131 / 255f, 112 / 255f, 106 / 255f);
        gl.glScalef(0.48f, 0.25f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPopMatrix();

        // DOOR ----------------------------------------------------------------
        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.66f, -13.54f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(1.16f, 1.16f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.27f, -0.71f, -13.53f);
        gl.glColor3f(164 / 255f, 179 / 255f, 172 / 255f);
        gl.glScalef(0.5f, 1.03f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.27f, -0.71f, -13.53f);
        gl.glColor3f(164 / 255f, 179 / 255f, 172 / 255f);
        gl.glScalef(0.5f, 1.03f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //AC -------------------------------------------------------------------
        // lEFT SIDE
        // above
        gl.glPushMatrix();

        gl.glTranslatef(0f, -0.1f, 0.5f);

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        //middle
        gl.glPushMatrix();

        gl.glTranslatef(0f, -1.1f, 0.5f);

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        //middle
        gl.glPushMatrix();

        gl.glTranslatef(0f, -2.1f, 0.5f);

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // RIGHT SIDE
        // above
        gl.glPushMatrix();

        gl.glTranslatef(4.2f, -0.1f, 0.5f);

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        //middle
        gl.glPushMatrix();

        gl.glTranslatef(4.2f, -1.1f, 0.5f);

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        //middle
        gl.glPushMatrix();

        gl.glTranslatef(4.2f, -2.1f, 0.5f);

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.2f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.01f, 0.2f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // BACK
        // first layer
        gl.glPushMatrix();

        gl.glTranslatef(1.3f, 0f, -0.7f);

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // second layer
        gl.glPushMatrix();

        gl.glTranslatef(1.3f, -1f, -0.7f);

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // second layer
        gl.glPushMatrix();

        gl.glTranslatef(1.3f, -2f, -0.7f);

        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 1.5f, -15.5f);
        gl.glColor3f(121 / 255f, 95 / 255f, 65 / 255f);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // OUSIDE WALL ---------------------------------------------------------
        gl.glPushMatrix();
        gl.glTranslatef(-2.65f, -0.6f, -14.5f);
        gl.glColor3f(201 / 255f, 157 / 255f, 110 / 255f);
        gl.glScalef(0.2f, 1f, 4.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.65f, -0.6f, -14.5f);
        gl.glColor3f(201 / 255f, 157 / 255f, 110 / 255f);
        gl.glScalef(0.2f, 1f, 4.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.6f, -12.2f);
        gl.glColor3f(201 / 255f, 157 / 255f, 110 / 255f);
        gl.glScalef(5.5f, 1f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.6f, -16.65f);
        gl.glColor3f(201 / 255f, 157 / 255f, 110 / 255f);
        gl.glScalef(5.5f, 1f, 0.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // front wall, brown
        gl.glPushMatrix();
        gl.glTranslatef(-.9f, -0.65f, -12.1f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(0.4f, 0.75f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(.9f, -0.65f, -12.1f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(0.4f, 0.75f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.9f, -0.65f, -12.1f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(1.5f, 0.75f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.9f, -0.65f, -12.1f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(1.5f, 0.75f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // left and right, brown
        gl.glPushMatrix();
        gl.glTranslatef(-2.75f, -0.65f, -12.95f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(0.01f, 0.75f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.75f, -0.65f, -12.95f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(0.01f, 0.75f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.75f, -0.65f, -14f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(0.01f, 0.75f, 0.4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.75f, -0.65f, -14f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(0.01f, 0.75f, 0.4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.75f, -0.65f, -15.05f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(0.01f, 0.75f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.75f, -0.65f, -15.05f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(0.01f, 0.75f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.75f, -0.65f, -16.3f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(0.01f, 0.75f, 0.66f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.75f, -0.65f, -16.3f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(0.01f, 0.75f, 0.66f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // back brown
        gl.glPushMatrix();

        gl.glTranslatef(0f, 0f, -4.65f);

        gl.glPushMatrix();
        gl.glTranslatef(-.9f, -0.65f, -12.1f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(0.4f, 0.75f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(.9f, -0.65f, -12.1f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(0.4f, 0.75f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.65f, -12.1f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(1.18f, 0.75f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.9f, -0.65f, -12.1f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(1.5f, 0.75f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.9f, -0.65f, -12.1f);
        gl.glColor3f(162 / 255f, 111 / 255f, 68 / 255f);
        gl.glScalef(1.5f, 0.75f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // DOOR ----------------------------------------------------------------
        gl.glPushMatrix();

        gl.glTranslatef(0f, 0f, 1.44f);

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.66f, -13.54f);
        gl.glColor3f(70 / 255f, 92 / 255f, 79 / 255f);
        gl.glScalef(1.16f, 0.9f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.27f, -0.71f, -13.53f);
        gl.glColor3f(164 / 255f, 179 / 255f, 172 / 255f);
        gl.glScalef(0.5f, 0.8f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.27f, -0.71f, -13.53f);
        gl.glColor3f(164 / 255f, 179 / 255f, 172 / 255f);
        gl.glScalef(0.5f, 0.8f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // school name 
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.1f, -12.12f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glScalef(1.18f, 0.55f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.53f, -0.15f, -12.09f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        school.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(1.0811f, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(1.08f, 0.55f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 0.55f);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glPopMatrix();

        // Flush all drawing operations to the graphics card
        gl.glEnable(GL_BLEND);
        gl.glEnable(GL_DEPTH_TEST);

        gl.glPopMatrix();

        gl.glFlush();

    }
    
    // Unfinished Building -----------------------------------------------------

    public void UnfinishedBuilding(GLUT glut, GL gl) {

        gl.glPushMatrix();

        gl.glEnable(GL_BLEND);
        gl.glEnable(GL_DEPTH_TEST);

        // BUILDING STRUCTURE ...
        // Below 
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -15f);
        gl.glColor3f(144 / 255f, 87 / 255f, 78 / 255f);
        gl.glScalef(4, 2.5f, 3);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Above 
        gl.glPushMatrix();
        gl.glTranslatef(0f, 2.75f, -14.5f);
        gl.glColor3f(144 / 255f, 87 / 255f, 78 / 255f);
        gl.glScalef(4, 3f, 4);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // GRAY LAYERS
        // First
        gl.glPushMatrix();
        gl.glTranslatef(0f, -1.1f, -15f);
        gl.glColor3f(143 / 255f, 151 / 255f, 154 / 255f);
        gl.glScalef(4.01f, 0.3f, 3.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Second
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.1f, -15f);
        gl.glColor3f(143 / 255f, 151 / 255f, 154 / 255f);
        gl.glScalef(4.01f, 0.3f, 3.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Third
        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.4f, -14.5f);
        gl.glColor3f(143 / 255f, 151 / 255f, 154 / 255f);
        gl.glScalef(4.01f, 0.3f, 4.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Fourth
        gl.glPushMatrix();
        gl.glTranslatef(0f, 2.9f, -14.5f);
        gl.glColor3f(143 / 255f, 151 / 255f, 154 / 255f);
        gl.glScalef(4.01f, 0.3f, 4.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Fourth
        gl.glPushMatrix();
        gl.glTranslatef(0f, 2.9f, -14.5f);
        gl.glColor3f(143 / 255f, 151 / 255f, 154 / 255f);
        gl.glScalef(4.01f, 0.3f, 4.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Fifth
        gl.glPushMatrix();
        gl.glTranslatef(0f, 4.11f, -14.5f);
        gl.glColor3f(143 / 255f, 151 / 255f, 154 / 255f);
        gl.glScalef(4.01f, 0.3f, 4.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Sixth 
        gl.glPushMatrix();
        gl.glTranslatef(0f, 5.5f, -14.5f);
        gl.glColor3f(143 / 255f, 151 / 255f, 154 / 255f);
        gl.glScalef(4, 0.1f, 4);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // COLUMS (GRAY TOO)
        // left side, behind
        gl.glPushMatrix();
        gl.glTranslatef(-1.87f, 4.8f, -16.38f);
        gl.glColor3f(143 / 255f, 151 / 255f, 154 / 255f);
        gl.glScalef(0.25f, 1.4f, 0.25f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // left side, front
        gl.glPushMatrix();
        gl.glTranslatef(-1.87f, 4.8f, -12.63f);
        gl.glColor3f(143 / 255f, 151 / 255f, 154 / 255f);
        gl.glScalef(0.25f, 1.4f, 0.25f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // righy side, behind
        gl.glPushMatrix();
        gl.glTranslatef(1.87f, 4.8f, -16.38f);
        gl.glColor3f(143 / 255f, 151 / 255f, 154 / 255f);
        gl.glScalef(0.25f, 1.4f, 0.25f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // left side, front
        gl.glPushMatrix();
        gl.glTranslatef(1.87f, 4.8f, -12.63f);
        gl.glColor3f(143 / 255f, 151 / 255f, 154 / 255f);
        gl.glScalef(0.25f, 1.4f, 0.25f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // WINDOWS
        // Above. Front 
        gl.glPushMatrix();

        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glTranslatef(-0.3f, 0f, 0f);
        gl.glPushMatrix();
        gl.glTranslatef(1.6f, 3.5f, -12.5f);

        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Above. Back 
        gl.glPushMatrix();

        gl.glTranslatef(-0.3f, 0f, -4f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(1.6f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Above. Front 
        gl.glPushMatrix();

        gl.glTranslatef(-0.3f, -1.3f, 0f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(1.6f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Above. Back 
        gl.glPushMatrix();

        gl.glTranslatef(-0.3f, -1.3f, -4f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(1.6f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Below, Back
        gl.glPushMatrix();

        gl.glTranslatef(-0.3f, -2.7f, -4f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(1.6f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Below, Back
        gl.glPushMatrix();

        gl.glTranslatef(-0.3f, -3.96f, -4f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(1.6f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Above, right 
        gl.glPushMatrix();

        gl.glTranslatef(-0.3f, -1.3f, 0f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -13.1f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -15.9f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Above, right 
        gl.glPushMatrix();

        gl.glTranslatef(-0.3f, 0f, 0f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -13.1f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -15.9f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Below, right 
        gl.glPushMatrix();

        gl.glTranslatef(-0.3f, -2.7f, 0f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -15.9f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Below, right 
        gl.glPushMatrix();

        gl.glTranslatef(-0.3f, -3.96f, 0f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -15.9f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Above, left 
        gl.glPushMatrix();

        gl.glTranslatef(-4.3f, 0f, 0f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -13.1f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -15.9f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Above, left 
        gl.glPushMatrix();

        gl.glTranslatef(-4.3f, -1.3f, 0f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -13.1f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -15.9f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Below, Front
        gl.glPushMatrix();

        gl.glTranslatef(-0.3f, -2.7f, -1f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(1.6f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Below, Back
        gl.glPushMatrix();

        gl.glTranslatef(-0.3f, -3.96f, -1f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(1.6f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.3f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1f, 3.5f, -12.5f);
        gl.glScalef(0.45f, 0.7f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Below, right 
        gl.glPushMatrix();

        gl.glTranslatef(-4.3f, -2.7f, 0f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -15.9f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Below, right 
        gl.glPushMatrix();

        gl.glTranslatef(-4.3f, -3.96f, 0f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -15.9f);
        gl.glScalef(0.01f, 0.7f, 0.45f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        //ACs
        // Left
        gl.glPushMatrix();
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();

        gl.glTranslatef(-4.3f, 0f, -.8f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -13.1f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPushMatrix();

        gl.glTranslatef(-4.3f, -1.3f, -.8f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -13.1f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPushMatrix();

        gl.glTranslatef(-4.3f, -2.7f, -.8f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -13.1f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPushMatrix();

        gl.glTranslatef(-4.3f, -3.9f, -.8f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -13.1f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPopMatrix();

        // Right
        gl.glPushMatrix();

        gl.glTranslatef(4f, 0f, 0f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();

        gl.glTranslatef(-4.3f, 0f, -.8f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -13.1f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPushMatrix();

        gl.glTranslatef(-4.3f, -1.3f, -.8f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -13.1f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPushMatrix();

        gl.glTranslatef(-4.3f, -2.7f, -.8f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -13.1f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPushMatrix();

        gl.glTranslatef(-4.3f, -3.9f, -.8f);

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -13.1f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.3f, 3.5f, -14.5f);
        gl.glScalef(0.01f, .4f, .4f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPopMatrix();

        // Back
        // First Layer
        gl.glPushMatrix();

        gl.glTranslatef(-0.3f, 0f, -4f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(1f, 3.5f, -12.5f);
        gl.glScalef(0.4f, 0.4f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 3.5f, -12.5f);
        gl.glScalef(0.4f, 0.4f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Second Layer
        gl.glPushMatrix();

        gl.glTranslatef(-0.3f, -1.3f, -4f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(1f, 3.5f, -12.5f);
        gl.glScalef(0.4f, 0.4f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 3.5f, -12.5f);
        gl.glScalef(0.4f, 0.4f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Second Layer
        gl.glPushMatrix();

        gl.glTranslatef(-0.3f, -2.7f, -4f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(1f, 3.5f, -12.5f);
        gl.glScalef(0.4f, 0.4f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 3.5f, -12.5f);
        gl.glScalef(0.4f, 0.4f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Third Layer
        gl.glPushMatrix();

        gl.glTranslatef(-0.3f, -4f, -4f);
        gl.glColor3f(47 / 255f, 44 / 255f, 39 / 255f);

        gl.glPushMatrix();
        gl.glTranslatef(1f, 3.5f, -12.5f);
        gl.glScalef(0.4f, 0.4f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.3f, 3.5f, -12.5f);
        gl.glScalef(0.4f, 0.4f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glDisable(GL_BLEND);
        gl.glDisable(GL_DEPTH_TEST);

        gl.glPopMatrix();
    }
    
    // clouds ------------------------------------------------------------------
    
    public void Cloud1(GLUT glut, GL gl){
        
        gl.glPushMatrix();
        
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        
        gl.glPushMatrix();
        gl.glTranslatef(0f, 7f, 6f);
        gl.glColor4f(1, 1, 1, 0.2f);
        gl.glScalef(1.5f, 1.5f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.9f, 7f, 6f);
        gl.glColor4f(1, 1, 1, 0.4f);
        gl.glScalef(1f, 1f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(-0.2f, 7.37f, 4.7f);
        gl.glColor4f(1, 1, 1, 0.7f);
        gl.glScalef(1.3f, 1.3f, 1.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(-0.2f, 7.37f, 7f);
        gl.glColor4f(1, 1, 1, 0.6f);
        gl.glScalef(0.6f, 0.6f, 0.6f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        
         gl.glPushMatrix();
        gl.glTranslatef(-0.22f, 7.37f, 7f);
        gl.glColor4f(1, 1, 1, 0.1f);
        gl.glScalef(2f, 2f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(-0.2f, 8.37f, 8f);
        gl.glColor4f(1, 1, 1, 0.3f);
        gl.glScalef(.5f, .5f, .5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(-0.2f, 7.7f, 8.5f);
        gl.glColor4f(1, 1, 1, 0.25f);
        gl.glScalef(.8f, .8f, .8f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        
        gl.glDisable(GL_BLEND);
        gl.glPopMatrix();
    }
    
    public void Cloud2(GLUT glut, GL gl){
        
        gl.glPushMatrix();
        
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        
        gl.glPushMatrix();
        gl.glTranslatef(0f, 7f, 6f);
        gl.glColor4f(1, 1, 1, 0.1f);
        gl.glScalef(1f, 1f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.1f, 7.5f, 7.3f);
        gl.glColor4f(1, 1, 1, 0.4f);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        
        gl.glDisable(GL_BLEND);
        gl.glPopMatrix();
    }
    
    // satellite ---------------------------------------------------------------

    public static void satellite(GL gl, GLUT glut, int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9){

        gl.glPushMatrix();
        
        //stick ?
        gl.glPushMatrix();
        gl.glTranslatef(0, 0f, -2f);
        gl.glColor3f(87 / 255f, 85 / 255f, 86 / 255f);
        gl.glScalef(1f, 0.7f, .5f);
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(.2f, 3, 50, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0, -2f, -2f);
        gl.glScalef(0.5f, 0.4f, 0.5f);
        gl.glColor3f(78 / 255f, 76 / 255f, 77 / 255f);
        glut.glutSolidCube(2f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0, 0.8f, -1f);
        gl.glRotatef(-50, 1, 0, 0);
        gl.glColor3f(0f, 0f, 0f);
        gl.glScalef(-0.05f, -0.05f, 0.03f);
        glut.glutSolidCylinder(1.4f, 3, 50, 15);
        gl.glPopMatrix();

        // dish
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, -2f);
        gl.glRotatef(-50, 1, 0, 0);
        gl.glColor3f(198 / 255f, 198 / 255f, 198 / 255f);
        gl.glScalef(1.3f, 1.3f, 0.03f);
        glut.glutSolidCylinder(1.4f, 3, 50, 15);
        gl.glPopMatrix();

        // begin of the columns inside dish
        gl.glPushMatrix();
        gl.glTranslatef(0, -0.2f, -2f);
        gl.glColor3f(137 / 255f, 137 / 255f, 137 / 255f);
        gl.glScalef(0.5f, 0.5f, .5f);
        gl.glRotatef(-45, 1, 0, 0);
        glut.glutSolidCylinder(0.1f, 3, 50, 15);
        gl.glPopMatrix();

        gl.glPopMatrix();
    }

    // -------------------------------------------------------------------------
    /*
    Draws a cuboi using five quads, for top, front, back, and bottom.
    
    Parameters:
    GL gl : drawing object
    float width: x measurment
    float height: y measurment
    float depth: z measurment
     */

    public void keyTyped(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void keyPressed(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void keyReleased(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
