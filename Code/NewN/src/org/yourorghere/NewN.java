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
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Font;

/**
 * NewN.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel)
 * <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class NewN implements GLEventListener, KeyListener {

    // Text
    TextRenderer text;
    public static Texture tex;
    public static float counter = 0f;
    static float angle = 0.0f;

    // Animating components:
    public float blackCarCounter = 0;
    public float blackCarCounter2 = 0;
    public float blackCarRotate = 0;
    public float blackCarRotate2 = 0;
    public float blackCarRotate3 = 0;
    public float blackCarRotate4 = 0;
    public float blackCarRotate5 = 0;
    public float blackCarRotate6 = 0;
    public float blackCarRotate7 = 0;
    public boolean done1 = false;
    public boolean done2 = false;
    public boolean roundAbout = false;
    public boolean beforeRoundAbout = true;
    public boolean afterRoundAbout = false;
    
    // actual vector representing the camera's location
    static float cameraX = 0f, cameraY = 4f, cameraZ = 5;
    // Where the camera is looking
    static float locationX = 0, locationY = 2, locationZ = -5;

    public static void main(String[] args) {
        Frame frame = new Frame("New Neighberhood");
        GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new NewN());
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
                    locationX = locationX - 0.5f;
                }
                if (event.getKeyCode() == KeyEvent.VK_DOWN) {
                    System.out.println("KEY EVENT DOWN.");
                    locationY = locationY - 0.5f;
                }
                if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
                    System.out.println("KEY EVENT RIGHT.");
                    locationX = locationX + 0.5f;
                }
                if (event.getKeyCode() == KeyEvent.VK_UP) {
                    System.out.println("KEY EVENT UP.");
                    locationY = locationY + 0.5f;
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
        canvas.addKeyListener(listener);
        // End of Detecting Pressed Keys ----------------------------------------------------

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
            tex = TextureIO.newTexture(new File("pic_bin.gif"), true);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());
        text = new TextRenderer(new Font("Bauhaus 93",Font.PLAIN, 20));

        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(146 / 255f, 229 / 255f, 1f, 0.5f);
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

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        GLUT glut = new GLUT();
        GLU glu = new GLU();
        
        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        // Reset the current matrix to the "identity"
        gl.glLoadIdentity();

        // Enabling depth test
        gl.glEnable(GL.GL_BLEND);
        gl.glEnable(GL.GL_DEPTH_TEST);
        
        // Anti aliasing code:
        gl.glEnable(GL.GL_LINE_SMOOTH);
        gl.glEnable(GL.GL_POLYGON_SMOOTH);
        gl.glHint(GL.GL_POLYGON_SMOOTH_HINT, GL.GL_NICEST);
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        // Set the camera ------------------------------------------------------
        glu.gluLookAt(cameraX, cameraY, cameraZ, //  position of your camera
                locationX, locationY, locationZ, // where the camera is looking
                0.0f, 1.0f, 0.0f); // tilt

        // Lighting ------------------------------------------------------------
        setUpLighting(gl);

        // Text ----------------------------------------------------------------
        text.beginRendering(drawable.getWidth(), drawable.getHeight());
        // Optionally set the color
        text.setColor(0f, 0f, 0f, 1f); //RGBA
        text.draw("New Neighberhood", 30, 30);
        // ... more draw commands, color changes, etc.
        text.endRendering();
        // Drawing -------------------------------------------------------------
        
        // Move the "drawing cursor" around
        gl.glTranslatef(0f, 0f, -6.0f);
        gl.glRotated(angle, 0, 1, 0);
        angle += 0.5;
        // This is used to scale the whole neighberhood up and down.
        gl.glScaled(0.5f, 0.5f, 0.5f);

        // Clouds --------------------------------------------------------------
        gl.glPushMatrix();
        // First could
        gl.glPushMatrix();
        gl.glTranslatef(0f, 2f, 5f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(1f, 1f, 1f);
        cloud1(glut, gl);
        gl.glPopMatrix();
        // Second cloud
        gl.glPushMatrix();
        gl.glTranslatef(-5f, 3f, 5f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(1f, 1f, 1f);
        cloud2(glut, gl);
        gl.glPopMatrix();
        // Third cloud
        gl.glPushMatrix();
        gl.glTranslatef(0f, 3f, -5f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(1.5f, 1.5f, 1.5f);
        cloud2(glut, gl);
        gl.glPopMatrix();
        // Fourth cloud
        gl.glPushMatrix();
        gl.glTranslatef(-8f, 2f, 0f);
        gl.glScalef(1f, 1f, 1f);
        cloud1(glut, gl);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(5f, 2f, 0f);
        gl.glScalef(1f, 1f, 1f);
        cloud2(glut, gl);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glRotatef(90, 0, 1, 0);
        gl.glTranslatef(10f, 2f, 10f);
        gl.glScalef(1f, 1f, 1f);
        cloud1(glut, gl);
        gl.glPopMatrix();
        
        
        gl.glPopMatrix();

        // Floor ---------------------------------------------------------------
        // Main floor
        gl.glColor3f(139 / 255f, 139 / 255f, 139 / 255f); // Light Grey
        gl.glPushMatrix();
        gl.glTranslatef(5, 0, 0);
        gl.glScalef(35, 0.5f, 20);
        glut.glutSolidCube(1); // Drawing the main floor
        gl.glPopMatrix();

        // Lift objects to be drawn above the floor
        //(otherwise they would disappear inside it)
        gl.glTranslatef(0f, 1.8f, 0f); //to be changed

        // Market --------------------------------------------------------------
        marketArea(gl, glut);

        // Modern Houses -------------------------------------------------------
        leftHousesArea(gl, glut);

        // Park ----------------------------------------------------------------
        // Park Ground
        gl.glPushMatrix();
        gl.glTranslatef(-3f, -1.5f, 6f);
        gl.glColor3f(155 / 255f, 221 / 255f, 98 / 255f); // park green
        gl.glScalef(6f, 0.01f, 7.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Park Objects
        gl.glPushMatrix();
        //gl.glTranslatef(0, 0.5f, 0);
        gl.glTranslatef(-5.5f, -1.2f, 3f);
        gl.glScalef(0.4f, 0.4f, 0.4f);
        park(gl, glut);
        gl.glPopMatrix();

        parkStreet(gl, glut);

        // Back houses ---------------------------------------------------------
        backHousesArea(gl, glut);

        // School --------------------------------------------------------------
        schoolArea(gl, glut);

        // Masjed --------------------------------------------------------------
        gl.glPushMatrix();
        gl.glTranslatef(8f, 0.5f, -10f);
        gl.glRotatef(-90, 0, 1, 0);
        gl.glScalef(2f, 2f, 2f);
        MasjedNew(glut, gl);
        gl.glPopMatrix();

        // Masjed streets
        gl.glPushMatrix();
        gl.glTranslatef(11.7f, 0f, 0f);
        masjidStreet(gl, glut);
        gl.glPopMatrix();

        // Masjid Parking
        gl.glPushMatrix();
        gl.glTranslatef(4.5f, 0f, 3.5f);
        gl.glRotatef(-90, 0, 1, 0);
        masjidParking(gl, glut);
        gl.glPopMatrix();
        gl.glPopMatrix();

        // Play Ground ---------------------------------------------------------
        gl.glPushMatrix();
        gl.glTranslatef(2f, -0.5f, 6f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        Stadium(gl, glut);
        gl.glPopMatrix();
        gl.glPopMatrix();

        // Roundabout ----------------------------------------------------------
        gl.glPushMatrix();
        // big yellow cirlce
        gl.glColor3f(239 / 255f, 156 / 255f, 13 / 255f); //yellow
        gl.glTranslatef(4.4f, -1.5f, -0.5f);
        gl.glRotatef(90, 1, 0, 0);
        gl.glScalef(0.7f, 1, 0.7f);
        glut.glutSolidCylinder(2, 0.1, 20, 10);
        // big grey circle
        gl.glTranslatef(0f, 0f, -0.01f);
        gl.glColor3f(139 / 255f, 139 / 255f, 139 / 255f); // Light Grey
        glut.glutSolidCylinder(1.9, 0.01, 20, 10);
        // small yellow circle:
        gl.glTranslatef(0f, 0f, -0.03f);
        gl.glColor3f(239 / 255f, 156 / 255f, 13 / 255f); //yellow
        glut.glutSolidCylinder(0.7, 0.01, 20, 10);
        gl.glPopMatrix();

        // fixing the roundabout exits:
        // left exit
        gl.glPushMatrix();
        gl.glTranslatef(-1f, -1.54f, -0.5f);
        gl.glColor3f(139 / 255f, 139 / 255f, 139 / 255f); // Light Grey
        gl.glScalef(10, 0.1f, 1);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // front exit
        gl.glPushMatrix();
        gl.glTranslatef(4.5f, -1.54f, 5f);
        gl.glColor3f(139 / 255f, 139 / 255f, 139 / 255f); // Light Grey
        gl.glScalef(1, 0.1f, 10);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // yellow lines:
        gl.glPushMatrix();
        gl.glTranslated(12, 0, -2.1);
        // Street in front of houses
        gl.glPushMatrix();
        // left side of the street
        gl.glPushMatrix();
        gl.glColor3f(239 / 255f, 156 / 255f, 13 / 255f);
        gl.glTranslatef(-8f, -1.5f, 4.5f);
        gl.glScalef(0.05f, 0.05f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // right side of the street
        gl.glPushMatrix();
        gl.glColor3f(239 / 255f, 156 / 255f, 13 / 255f);
        gl.glTranslatef(-7f, -1.5f, 4.4f);
        gl.glScalef(0.05f, 0.05f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // Street lines:
        gl.glPushMatrix();
        gl.glColor3f(1f, 1.1f, 1f);
        gl.glTranslatef(-7.4f, -1.5f, 5.5f);
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // end of street lines
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Clinic --------------------------------------------------------------
        gl.glPushMatrix();
        gl.glTranslated(0, 0, -5);
        clinicArea(gl, glut);
        gl.glPopMatrix();

        // Back right houses ---------------------------------------------------
        gl.glPushMatrix();
        gl.glTranslated(22, 0, 5);
        gl.glRotatef(-90, 0, 1, 0);
        backRightHousesArea(gl, glut);
        gl.glPopMatrix();

        // Flush all drawing operations to the graphics card
        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    // -------------------------------------------------------------------------
    // Animating ---------------------------------------------------------------
    public void moveBlackCar(GL gl, GLUT glut) {

        gl.glPushMatrix();

        gl.glTranslatef(2f, 0f, 6f);
        if (!done1) {
            gl.glTranslatef(0, 0, blackCarCounter);
        }
        gl.glPushMatrix();
        gl.glRotated(90, 0, 1, 0);
        gl.glScalef(0.65f, 0.65f, 0.65f);
        if (done1 && !done2) {
            gl.glTranslatef(11.6f, 0f, 0f); //z //y // x
            gl.glTranslatef(blackCarRotate2, 0f, blackCarRotate3); //z //y // x
            gl.glPushMatrix();
            gl.glRotatef(blackCarRotate, 0, 1, 0);
            drawCar(gl, glut, 0f, 0f, 0f);
            blackCarRotate2 += 0.02;
            blackCarRotate3 += 0.01;
            gl.glPopMatrix();
        }
        if (!done1) {
            drawCar(gl, glut, 0f, 0f, 0f);
        }
        if (done2 && beforeRoundAbout) {
            gl.glTranslatef(13.5f, 0f, 1f); //z //y // x
            gl.glRotated(-90, 0, 1, 0);
            gl.glTranslatef(blackCarCounter2, 0f, 0f); //x //y // z (Animation translate
            drawCar(gl, glut, 0f, 0f, 0f);
            blackCarCounter2 += 0.05;
            if (blackCarCounter2 > 13) {
                beforeRoundAbout = false;
                roundAbout = true;
            }
        }

        if (roundAbout) {
            gl.glTranslatef(13.5f, 0f, 14f); //z //y // x
            gl.glRotated(-90, 0, 1, 0);
//            gl.glTranslatef(blackCarRotate5, 0f, 0); //x //y // z (Animation translate

            if (blackCarRotate4 > -90 ){
                gl.glTranslatef(blackCarRotate5, 0f, 0); //x //y // z (Animation translate
                gl.glPushMatrix();
                System.out.println("*********************************");
                if (blackCarRotate4 < -50)
                {gl.glTranslatef(0, 0f, blackCarRotate6);
                blackCarRotate6 += 0.05;
                }
                gl.glRotatef(blackCarRotate4, 0, 1, 0);
                drawCar(gl, glut, 0f, 0f, 0f);
                gl.glPopMatrix();
                blackCarRotate4 -= 0.5;
            }
            else
            {
                gl.glRotatef(-90, 0, 1, 0);
                gl.glTranslatef(4f, 0f, -5.1f);
                drawCar(gl, glut, 0f, 0f, 0f);
            }
            blackCarRotate5 += 0.03;
            
            // all while increasing along x axis
            // Start rotating the car right
            // Then left

            // if x > something
            // change switches
        }
        if (!roundAbout && afterRoundAbout) {
            // all while increasing along x axis
            // Start rotating the car right
            // Then left
        }
        gl.glPopMatrix();

        if (blackCarCounter > -7.6) {
            blackCarCounter -= 0.05;
            System.out.println("Increasing counter");
        } else {
            done1 = true;
            if (blackCarRotate > -100) {
                blackCarRotate -= 1;
                System.out.println("Increasing rotate");
            } else {
                System.out.println("DONE WITH ROTATE");
                done2 = true;
            }
        }

        gl.glPopMatrix();
    }

    // Encapsulating Methods ---------------------------------------------------
    /*
    These methods were written because the main display() was getting too long,
    so I broke down the main *parts* of the neghberhood to these methods.
    */
    
    public void leftHousesArea(GL gl, GLUT glut) {
        gl.glPushMatrix();
        gl.glTranslatef(-6f, -1f, 8.5f);
        gl.glScalef(0.7f, 0.7f, 0.7f);
        // rotation matrix v
        gl.glPushMatrix();
        gl.glRotated(90, 0, 1, 0);
        modernHouse(gl, glut);
        gl.glTranslatef(4f, 0f, 0f);
        modernHouse(gl, glut);
        gl.glTranslatef(4f, 0f, 0f);
        modernHouse(gl, glut);
        gl.glPopMatrix();
        // end of rotation matrix ^
        gl.glPopMatrix();

        // solar panel on top of first house
        gl.glPushMatrix();
        gl.glTranslatef(-10.5f, 1.3f, 10f);
        gl.glRotated(45, 0, 1, 0);
        gl.glScalef(0.6f, 0.6f, 0.6f);
        solarPortrait(glut, gl);
        gl.glPopMatrix();

        // Plants
        gl.glPushMatrix();
        gl.glColor3f(58 / 255f, 150 / 255f, 99 / 255f);

        // plants ninfront of the first house
        gl.glTranslatef(-9.3f, -1.5f, 9f);
        gl.glPushMatrix();
        gl.glRotatef(40, 0, 1, 0);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidIcosahedron();
        gl.glPopMatrix();

        // plants infront of the second house
        gl.glTranslatef(-0f, -0f, -3f);
        gl.glPushMatrix();
        gl.glRotatef(40, 0, 1, 0);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidIcosahedron();
        gl.glPopMatrix();
        gl.glTranslatef(0.3f, -0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.2f, 0.2f, 0.2f);
        glut.glutSolidIcosahedron();
        gl.glPopMatrix();

        // plants infront of the third house
        gl.glTranslatef(-0f, -0f, -3f);
        gl.glPushMatrix();
        gl.glRotatef(40, 0, 0, 1);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidOctahedron();
        gl.glPopMatrix();

        gl.glTranslatef(-0.2f, -0f, 0f);
        gl.glPushMatrix();
        gl.glRotatef(40, 1, 0, 0);
        gl.glScalef(0.2f, 0.2f, 0.2f);
        glut.glutSolidOctahedron();
        gl.glPopMatrix();

        gl.glPopMatrix(); // pops plants

        // Street in front of houses
        gl.glPushMatrix();
        // left side of the street
        gl.glPushMatrix();
        gl.glColor3f(239 / 255f, 156 / 255f, 13 / 255f);
        gl.glTranslatef(-8f, -1.5f, 4.5f);
        gl.glScalef(0.05f, 0.05f, 11f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // right side of the street
        gl.glPushMatrix();
        gl.glColor3f(239 / 255f, 156 / 255f, 13 / 255f);
        gl.glTranslatef(-7f, -1.5f, 5f);
        gl.glScalef(0.05f, 0.05f, 10f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // Street lines:
        gl.glPushMatrix();
        gl.glColor3f(1f, 1f, 1f);
        gl.glTranslatef(-7.4f, -1.5f, 9.5f);
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // end of street lines

        // Cars in front of the houses
        gl.glPushMatrix();
        gl.glTranslatef(-9.5f, -1.6f, 5f);
        // scale matrix v
        gl.glPushMatrix();
        gl.glScalef(0.65f, 0.65f, 0.65f);
        drawCar(gl, glut, 1, 0, 0);
        gl.glPopMatrix();
        // second purple car
        gl.glTranslatef(0f, 0f, -3f);
        gl.glPushMatrix();
        gl.glScalef(0.65f, 0.65f, 0.65f);
        drawCar(gl, glut, 128 / 255f, 0 / 255f, 128 / 255f);
        gl.glPopMatrix();
        moveBlackCar(gl, glut);
//        // third car on the street
//        gl.glTranslatef(1.5f, 0f, 6f);
//        gl.glPushMatrix();
//        gl.glRotated(90, 0, 1, 0);
//        gl.glScalef(0.65f, 0.65f, 0.65f);
//        drawCar(gl, glut, 0f, 0f, 0f);
//        gl.glPopMatrix();
        // end of scale matrix ^
        gl.glPopMatrix();

        // small park
        gl.glPushMatrix();
        gl.glTranslatef(-10f, -1.5f, 0.5f);
        gl.glColor3f(155 / 255f, 221 / 255f, 98 / 255f); // park green
        gl.glScalef(3f, 0.01f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();
    }

    public void backRightHousesArea(GL gl, GLUT glut) {
        gl.glPushMatrix();
        gl.glTranslatef(-6f, -1f, 8.5f);
        gl.glScalef(0.7f, 0.7f, 0.7f);
        // rotation matrix v
        gl.glPushMatrix();
        gl.glRotated(90, 0, 1, 0);
        modernHouse(gl, glut);
        gl.glTranslatef(4f, 0f, 0f);
        modernHouse(gl, glut);
        gl.glTranslatef(4f, 0f, 0f);
        modernHouse(gl, glut);
        gl.glPopMatrix();
        // end of rotation matrix ^
        gl.glPopMatrix();

        // solar panel on top of first house
        gl.glPushMatrix();
        gl.glTranslatef(-10.5f, 1.3f, 4f);
        gl.glRotated(45, 0, 1, 0);
        gl.glScalef(0.6f, 0.6f, 0.6f);
        solarPortrait(glut, gl);
        gl.glPopMatrix();

        // Plants
        gl.glPushMatrix();
        gl.glColor3f(58 / 255f, 150 / 255f, 99 / 255f);

        // plants infront of the first house
        gl.glTranslatef(-9.3f, -1.5f, 9f);
        gl.glPushMatrix();
        gl.glRotatef(40, 0, 1, 0);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidIcosahedron();
        gl.glPopMatrix();

        // plants infront of the second house
        gl.glTranslatef(-0f, -0f, -3f);
        gl.glPushMatrix();
        gl.glRotatef(40, 0, 1, 0);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidIcosahedron();
        gl.glPopMatrix();
        gl.glTranslatef(0.3f, -0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.2f, 0.2f, 0.2f);
        glut.glutSolidIcosahedron();
        gl.glPopMatrix();

        // plants infront of the third house
        gl.glTranslatef(-0f, -0f, -3f);
        gl.glPushMatrix();
        gl.glRotatef(40, 0, 0, 1);
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidOctahedron();
        gl.glPopMatrix();

        gl.glTranslatef(-0.2f, -0f, 0f);
        gl.glPushMatrix();
        gl.glRotatef(40, 1, 0, 0);
        gl.glScalef(0.2f, 0.2f, 0.2f);
        glut.glutSolidOctahedron();
        gl.glPopMatrix();

        gl.glPopMatrix(); // pops plants

        // Street in front of houses
        gl.glPushMatrix();
        // left side of the street
        gl.glPushMatrix();
        gl.glColor3f(239 / 255f, 156 / 255f, 13 / 255f);
        gl.glTranslatef(-6f, -1.5f, 4.5f); // was -8
        gl.glScalef(0.05f, 0.05f, 10f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // right side of the street
        gl.glPushMatrix();
        gl.glColor3f(239 / 255f, 156 / 255f, 13 / 255f);
        gl.glTranslatef(-5f, -1.5f, 4.5f);
        gl.glScalef(0.05f, 0.05f, 10f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // Street lines:
        gl.glPushMatrix();
        gl.glColor3f(1f, 1f, 1f);
        gl.glTranslatef(-5.6f, -1.5f, 9.5f);
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // end of street lines

        // Cars in front of the houses
        gl.glPushMatrix();
        gl.glTranslatef(-9.5f, -1.6f, 4.8f);
        // scale matrix v
        gl.glPushMatrix();
        gl.glScalef(0.65f, 0.65f, 0.65f);
        drawCar(gl, glut, 1, 0, 0);
        gl.glPopMatrix();
        // second purple car
        gl.glTranslatef(0f, 0f, -3f);
        gl.glPushMatrix();
        gl.glScalef(0.65f, 0.65f, 0.65f);
        drawCar(gl, glut, 128 / 255f, 0 / 255f, 128 / 255f);
        gl.glPopMatrix();
        // third car on the street
        gl.glTranslatef(4f, 0f, 6f);
        gl.glPushMatrix();
        gl.glRotated(90, 0, 1, 0);
        gl.glScalef(0.65f, 0.65f, 0.65f);
        drawCar(gl, glut, 255 / 255f, 0 / 255f, 128 / 255f);
        gl.glPopMatrix();
        // end of scale matrix ^
        gl.glPopMatrix();

        // small park
        gl.glPushMatrix();
        gl.glTranslatef(-8f, -1.5f, 6f);
        gl.glColor3f(155 / 255f, 221 / 255f, 98 / 255f); // park green
        gl.glScalef(3f, 0.01f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-8f, -1.5f, 3f);
        gl.glColor3f(155 / 255f, 221 / 255f, 98 / 255f); // park green
        gl.glScalef(3f, 0.01f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-8f, -1.5f, 9f);
        gl.glColor3f(155 / 255f, 221 / 255f, 98 / 255f); // park green
        gl.glScalef(3f, 0.01f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();
    }

    public void backHousesArea(GL gl, GLUT glut) {
        gl.glPushMatrix();
        // House 1 from left
        gl.glTranslatef(-3f, -1.5f, -5f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        ModernHouse2(gl, glut);
        gl.glPopMatrix();
        // House 2 from left
        gl.glTranslatef(2f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        ModernHouse2(gl, glut);
        gl.glPopMatrix();
        // House 2 from left
        gl.glTranslatef(2f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        ModernHouse2(gl, glut);
        gl.glPopMatrix();
        // House 2 from left
        gl.glTranslatef(2f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        ModernHouse2(gl, glut);
        gl.glPopMatrix();
        // House 2 from left
        gl.glTranslatef(2f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        ModernHouse2(gl, glut);
        gl.glPopMatrix();

        // bins
        gl.glTranslatef(-8.5f, 0.5f, 0);
        gl.glPushMatrix();
        gl.glScalef(0.8f, 0.8f, 0.8f);
        new_bin(gl, glut, 0 / 255f, 153 / 255f, 76 / 255f);
        gl.glPopMatrix();

        gl.glTranslatef(2f, 0f, 0);
        gl.glPushMatrix();
        gl.glScalef(0.8f, 0.8f, 0.8f);
        new_bin(gl, glut, 0 / 255f, 153 / 255f, 76 / 255f);
        gl.glPopMatrix();

        gl.glTranslatef(2f, 0f, 0);
        gl.glPushMatrix();
        gl.glScalef(0.8f, 0.8f, 0.8f);
        new_bin(gl, glut, 0 / 255f, 153 / 255f, 76 / 255f);
        gl.glPopMatrix();

        gl.glTranslatef(2f, 0f, 0);
        gl.glPushMatrix();
        gl.glScalef(0.8f, 0.8f, 0.8f);
        new_bin(gl, glut, 0 / 255f, 153 / 255f, 76 / 255f);
        gl.glPopMatrix();

        gl.glTranslatef(2f, 0f, 0);
        gl.glPushMatrix();
        gl.glScalef(0.8f, 0.8f, 0.8f);
        new_bin(gl, glut, 0 / 255f, 153 / 255f, 76 / 255f);
        gl.glPopMatrix();

        // front yards
        gl.glPushMatrix();
        gl.glTranslatef(-7.8f, -0.5f, 1f);
        gl.glColor3f(155 / 255f, 221 / 255f, 98 / 255f); // park green

        gl.glPushMatrix();
        gl.glScalef(1f, 0.01f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(2f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(1f, 0.01f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(2f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(1f, 0.01f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(2f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(1f, 0.01f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(2f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(1f, 0.01f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // solar panels on top of houses
        // second house from left
        gl.glPushMatrix();
        gl.glTranslatef(-8f, 2.8f, -2f);
        gl.glRotated(45, 0, 1, 0);
        gl.glScalef(0.6f, 0.6f, 0.6f);
        solarPortrait(glut, gl);
        gl.glPopMatrix();

        // fourth house from left
        gl.glPushMatrix();
        gl.glTranslatef(-2.5f, 2.8f, -2f);
        gl.glRotated(-45, 0, 1, 0);
        gl.glScalef(0.6f, 0.6f, 0.6f);
        solarPortrait(glut, gl);
        gl.glPopMatrix();

        // cars in front of the houses
        gl.glTranslatef(-3.2f, -0.3f, 3f);
        // scale matrix v
        gl.glPushMatrix();
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(0.65f, 0.65f, 0.65f);
        drawCar(gl, glut, 1, 0, 0);
        gl.glPopMatrix();

        gl.glPopMatrix();
    }

    public void marketArea(GL gl, GLUT glut) {

        gl.glPushMatrix();
        gl.glTranslatef(-9f, -0.1f, 0f);
        gl.glScalef(0.7f, 0.7f, 0.7f);
        market(gl, glut);
        gl.glPopMatrix();

        // Market Parking
        gl.glPushMatrix();

        // first back line:
        gl.glPushMatrix();
        gl.glColor3f(1f, 1f, 1f);
        gl.glTranslatef(-9.5f, -1.5f, -3f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(0.05f, 0.02f, 4.1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Parking lines:
        gl.glPushMatrix();
        gl.glColor3f(1f, 1f, 1f);

        gl.glTranslatef(-11.5f, -1.5f, -2.5f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix(); // pop parking lines

        // market trees:
        gl.glTranslatef(-5.7f, -1, -6);
        gl.glPushMatrix();
        gl.glScalef(0.5f, 0.5f, 0.5f);
        tree(gl, glut);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0, -1);
        gl.glPushMatrix();
        gl.glScalef(0.7f, 0.7f, 0.7f);
        tree2(gl, glut);
        gl.glPopMatrix();
        // bushes
        gl.glTranslatef(-0.5f, -0.5f, 1.2f);
        gl.glPushMatrix();
        gl.glRotatef(40, 0, 1, 0);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        glut.glutSolidIcosahedron();
        gl.glPopMatrix();

        // turbins
        gl.glTranslatef(1.5f, 5f, 1.2f);
        fan(gl, glut);

        gl.glPopMatrix();
    }

    public void parkStreet(GL gl, GLUT glut) {

        // Park Lamps:
        gl.glPushMatrix();
        gl.glTranslatef(-5f, 0.5f, 1f);
        gl.glPushMatrix();
        gl.glScalef(0.5f, 0.5f, 0.5f);
        lamp(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(2.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.5f, 0.5f, 0.5f);
        lamp(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(2.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.5f, 0.5f, 0.5f);
        lamp(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(2.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.5f, 0.5f, 0.5f);
        lamp(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(4f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.5f, 0.5f, 0.5f);
        lamp(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(2.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.5f, 0.5f, 0.5f);
        lamp(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(2.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.5f, 0.5f, 0.5f);
        lamp(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(2.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.5f, 0.5f, 0.5f);
        lamp(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(2.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.5f, 0.5f, 0.5f);
        lamp(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(2.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.5f, 0.5f, 0.5f);
        lamp(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(2.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.5f, 0.5f, 0.5f);
        lamp(gl, glut);
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Street behind the park
        gl.glPushMatrix();
        gl.glTranslatef(-7f, 0f, -8f);
        gl.glRotatef(90, 0, 1, 0);
        // left side of the street
        gl.glPushMatrix();
        gl.glColor3f(239 / 255f, 156 / 255f, 13 / 255f);
        gl.glTranslatef(-8f, -1.5f, 5f);
        gl.glScalef(0.05f, 0.05f, 10f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // yellow bit in front of the supermarket
        gl.glPushMatrix();
        gl.glColor3f(239 / 255f, 156 / 255f, 13 / 255f);
        gl.glTranslatef(-7f, -1.5f, -3.25f);
        gl.glScalef(0.05f, 0.05f, 4.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // right side of the street
        gl.glPushMatrix();
        gl.glColor3f(239 / 255f, 156 / 255f, 13 / 255f);
        gl.glTranslatef(-7f, -1.5f, 5f);
        gl.glScalef(0.05f, 0.05f, 10f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // Street lines:
        gl.glPushMatrix();
        gl.glColor3f(1f, 1f, 1f);
        gl.glTranslatef(-7.4f, -1.5f, 9.5f);
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // end of street lines
        gl.glPopMatrix();
    }

    public void masjidStreet(GL gl, GLUT glut) {
        gl.glPushMatrix();

        // Street behind the park
        gl.glPushMatrix();
        gl.glTranslatef(-7f, 0f, -8f);
        gl.glRotatef(90, 0, 1, 0);

        // left side of the street
        gl.glPushMatrix();
        gl.glColor3f(239 / 255f, 156 / 255f, 13 / 255f);
        gl.glTranslatef(-8f, -1.5f, 5f);
        gl.glScalef(0.05f, 0.05f, 8f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // right side of the street
        gl.glPushMatrix();
        gl.glColor3f(239 / 255f, 156 / 255f, 13 / 255f);
        gl.glTranslatef(-7f, -1.5f, 5f);
        gl.glScalef(0.05f, 0.05f, 8f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Street lines:
        gl.glPushMatrix();
        gl.glColor3f(1f, 1f, 1f);
        gl.glTranslatef(-7.4f, -1.4f, 10.5f);
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, 0f, -1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // end of street lines

        // left exit
        gl.glPushMatrix();
        gl.glTranslatef(-7.5f, -1.53f, 5f);
        gl.glColor3f(139 / 255f, 139 / 255f, 139 / 255f); // Light Grey
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(10, 0.1f, 1);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPopMatrix();
    }

    public void masjidParking(GL gl, GLUT glut) {
        // Market Parking
        gl.glPushMatrix();

        // first back line:
        gl.glPushMatrix();
        gl.glColor3f(1f, 1f, 1f);
        gl.glTranslatef(-9.5f, -1.5f, -3f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(0.05f, 0.02f, 4.1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Parking lines:
        gl.glPushMatrix();
        gl.glColor3f(1f, 1f, 1f);

        gl.glTranslatef(-11.5f, -1.5f, -2.5f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glTranslatef(0.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.05f, 0.02f, 1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix(); // pop parking lines
    }

    public void schoolArea(GL gl, GLUT glut) {

        gl.glPushMatrix();
        gl.glTranslatef(8f, -0.2f, 12f);
        gl.glScalef(0.8f, 0.8f, 0.8f);
        ModernSchool(gl, glut);
        gl.glPopMatrix();

        // Pointy trees in front of play ground --------------------------------
        gl.glPushMatrix();
        gl.glTranslatef(1.2f, -1.3f, 9.5f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix();
        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix(); // end of x axis trees

        // z axis trees:
        gl.glPushMatrix();
        gl.glTranslatef(-11.6f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0f, 0f, -0.9f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0f, 0f, -0.9f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0f, 0f, -0.9f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0f, 0f, -0.9f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0f, 0f, -0.9f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0f, 0f, -0.9f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0f, 0f, -0.9f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix(); // end of tree

        // back x axis trees
        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(3f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix(); // end of tree

        // right z axis trees:
        gl.glTranslatef(0f, 0f, 0.9f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0f, 0f, 0.9f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0f, 0f, 0.9f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0f, 0f, 0.9f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0f, 0f, 0.9f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree2(gl, glut);
        gl.glPopMatrix(); // end of tree

        gl.glTranslatef(0f, 0f, 0.9f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        pointyTree(gl, glut);
        gl.glPopMatrix(); // end of tree
        gl.glPopMatrix(); // end of axis trees

        gl.glPopMatrix();
    }

    public void clinicArea(GL gl, GLUT glut) {
        // Clinic
        gl.glPushMatrix();
        gl.glTranslatef(11f, -0.2f, 24.2f);
        gl.glRotated(-30, 0, 1, 0);
        clinic(gl, glut);
        gl.glPopMatrix();

        // clinic parking
        // clinic park
        gl.glPushMatrix();
        gl.glTranslatef(14.5f, -1.5f, 7f);

        // grren flat area
        gl.glPushMatrix();
        gl.glRotatef(0, 0, 1, 0);
        gl.glScalef(1.2f, 0.01f, 1.2f);
        gl.glColor3f(58 / 255f, 150 / 255f, 99 / 255f); //tree green
        glut.glutSolidDodecahedron();
        gl.glPopMatrix();

        // benches
        gl.glPushMatrix();
        gl.glTranslatef(-8f, 0.3f, 4f);
        gl.glRotatef(-60, 0, 1, 0);
        gl.glScalef(0.7f, 0.7f, 0.7f);
        bench(gl, glut);
        gl.glPopMatrix();

        // turbines behind clinic:
        gl.glPushMatrix();
        gl.glTranslatef(4.5f, 4f, -3.5f);
        //gl.glRotatef(-60, 0, 1, 0);
        //gl.glScalef(0.7f, 0.7f, 0.7f);
        fan(gl, glut);
        gl.glPopMatrix();

        gl.glPopMatrix();// clinic pop
    }

    // Components --------------------------------------------------------------
    /*
    These are the building blocks of the neighberhood, broken down to the
    smallest peices.
    */
    
    // Main Neighberhood Components --------------------------------------------
    // Anfal's work
    public static void park(GL gl, GLUT glut) {
        gl.glPushMatrix();

        // Object
        gl.glPushMatrix();
        tree(gl, glut);
        gl.glPopMatrix();

        // Bench 1
        gl.glPushMatrix();
        gl.glTranslatef(4f, -0.2f, 21f);
        gl.glScalef(1.5f, 1.5f, 1.5f);
        bench(gl, glut);
        gl.glPopMatrix();

        // Bench 2
        gl.glPushMatrix();
        gl.glTranslatef(22f, -0.2f, 4f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(1.5f, 1.5f, 1.5f);
        bench(gl, glut);
        gl.glPopMatrix();

        // Pond
        gl.glPushMatrix();
        gl.glColor3f(74 / 255f, 252 / 255f, 254 / 255f);
        gl.glTranslatef(6f, -0.5f, 7f);
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(2, 0.1, 20, 10);
        gl.glPopMatrix();

        // Pond accesories:
        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.7f, 0f); // change the 0.7 to maybe 0.65
        gl.glColor3f(164 / 255f, 164 / 255f, 164 / 255f);

        // Three rocks on the right
        gl.glTranslatef(9f, 0f, 7f);
        gl.glPushMatrix();
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(0.7, 0.1, 20, 10);
        gl.glPopMatrix();

        gl.glTranslatef(1.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(0.7, 0.1, 20, 10);
        gl.glPopMatrix();

        gl.glTranslatef(1.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(0.7, 0.1, 20, 10);
        gl.glPopMatrix();

        // Three rocks on the left
        gl.glTranslatef(-8.9f, 0f, 0f);
        gl.glPushMatrix();
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(0.7, 0.1, 20, 10);
        gl.glPopMatrix();

        gl.glTranslatef(-1.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(0.7, 0.1, 20, 10);
        gl.glPopMatrix();

        gl.glTranslatef(-1.5f, 0f, 0f);
        gl.glPushMatrix();
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(0.7, 0.1, 20, 10);
        gl.glPopMatrix();

        // Three rocks in the front
        gl.glTranslatef(6f, 0f, 3f);
        gl.glPushMatrix();
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(0.7, 0.1, 20, 10);
        gl.glPopMatrix();

        gl.glTranslatef(0f, 0f, 1.5f);
        gl.glPushMatrix();
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(0.7, 0.1, 20, 10);
        gl.glPopMatrix();

        gl.glTranslatef(0f, 0f, 1.5f);
        gl.glPushMatrix();
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(0.7, 0.1, 20, 10);
        gl.glPopMatrix();

        gl.glPopMatrix();
        // end of pond acceroise

        // Big tree
        gl.glPushMatrix();
        gl.glTranslatef(10f, 0f, 0f);
        gl.glScalef(3f, 2f, 2f);
        tree(gl, glut);
        gl.glPopMatrix();

        // Right small tree
        gl.glPushMatrix();
        gl.glTranslatef(13f, 0f, 0f);
        tree(gl, glut);
        gl.glPopMatrix();

        // Right rock
        gl.glPushMatrix();
        gl.glTranslatef(12.5f, -0.5f, 3f);
        gl.glScalef(1.5f, 1.5f, 1.5f);
        rock(gl, glut);
        gl.glPopMatrix();

        // left rock
        gl.glPushMatrix();
        gl.glTranslatef(1f, -0.5f, 15f);
        gl.glScalef(1.5f, 1.5f, 1.5f);
        rock(gl, glut);
        gl.glPopMatrix();

        // tiny rocks
        gl.glPushMatrix();
        gl.glTranslatef(3f, -0.5f, 15f);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        rock(gl, glut);
        gl.glPopMatrix();

        // tiny plants
        gl.glPushMatrix();
        gl.glColor3f(58 / 255f, 150 / 255f, 99 / 255f);

        gl.glTranslatef(10f, -0.5f, 15f);
        gl.glPushMatrix();
        gl.glRotatef(40, 0, 1, 0);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        glut.glutSolidDodecahedron();
        gl.glPopMatrix();

        gl.glTranslatef(1f, 0f, -0.5f);
        gl.glPushMatrix();
        gl.glScalef(0.3f, 0.3f, 0.3f);
        glut.glutSolidDodecahedron();
        gl.glPopMatrix();

        gl.glPopMatrix(); // end of plants

        gl.glPopMatrix();

    }

    // Razan's work
    public static void market(GL gl, GLUT glut) {
        gl.glPushMatrix();

        ///-------------------------BACK ----------------------------------------
        //blue board 
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, .5f, -9.5f);
        gl.glColor3f(124 / 255f, 125 / 255f, 120 / 255f);
        gl.glScalef(6f, 4.2f, 5);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //column one 
        gl.glPushMatrix();
        gl.glTranslatef(3.5f, -1.0f, -12f);
        gl.glColor3f(124 / 255f, 125 / 255f, 120 / 255f);
        gl.glScalef(1, 1.5f, 1);
        glut.glutSolidCube(1);

        gl.glTranslatef(0.0f, 0.6f, 0.0f);
        gl.glColor3f(55 / 255f, 67 / 255f, 144 / 255f);
        gl.glScalef(1, 0.2f, 1);
        glut.glutSolidCube(1);

        gl.glTranslatef(0.0f, 5f, 0.0f);
        gl.glColor3f(124 / 255f, 125 / 255f, 120 / 255f);
        gl.glScalef(1, 9f, 1);
        glut.glutSolidCube(1);

        gl.glTranslatef(0.0f, .5f, 0.0f);
        gl.glColor3f(55 / 255f, 67 / 255f, 144 / 255f);
        gl.glScalef(1, .1f, 1);
        glut.glutSolidCube(1);

        gl.glTranslatef(0.0f, 1f, 0.0f);
        gl.glColor3f(124 / 255f, 125 / 255f, 120 / 255f);
        gl.glScalef(1, 1f, 1);
        glut.glutSolidCube(1);
        gl.glTranslatef(0.0f, 0.0f, 0.0f);
        gl.glPopMatrix();

        //column 2
        gl.glPushMatrix();
        gl.glTranslatef(-3.5f, -1.0f, -12f);
        gl.glColor3f(124 / 255f, 125 / 255f, 120 / 255f);
        gl.glScalef(1, 1.5f, 1);
        glut.glutSolidCube(1);
        //the blue line 
        gl.glTranslatef(0.0f, 0.6f, 0.0f);
        gl.glColor3f(55 / 255f, 67 / 255f, 144 / 255f);
        gl.glScalef(1, 0.2f, 1);
        glut.glutSolidCube(1);

        gl.glTranslatef(0.0f, 5f, 0.0f);
        gl.glColor3f(124 / 255f, 125 / 255f, 120 / 255f);
        gl.glScalef(1, 9f, 1);
        glut.glutSolidCube(1);
        //the blue line 
        gl.glTranslatef(0.0f, .5f, 0.0f);
        gl.glColor3f(55 / 255f, 67 / 255f, 144 / 255f);
        gl.glScalef(1, .1f, 1);
        glut.glutSolidCube(1);

        gl.glTranslatef(0.0f, 1f, 0.0f);
        gl.glColor3f(124 / 255f, 125 / 255f, 120 / 255f);
        gl.glScalef(1, 1f, 1);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //doors
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -.2f, -6.5f);
        gl.glColor3f(21 / 255f, 23 / 255f, 18 / 255f);
        gl.glScalef(6, .2f, 1);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -.2f, -6.5f);
        gl.glColor3f(21 / 255f, 23 / 255f, 18 / 255f);
        gl.glScalef(.2f, 2.8f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(1.0f, -.2f, -6.5f);
        gl.glColor3f(21 / 255f, 23 / 255f, 18 / 255f);
        gl.glScalef(.2f, 2.8f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2.0f, -.2f, -6.5f);
        gl.glColor3f(21 / 255f, 23 / 255f, 18 / 255f);
        gl.glScalef(.2f, 2.8f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(3.0f, -.2f, -6.5f);
        gl.glColor3f(21 / 255f, 23 / 255f, 18 / 255f);
        gl.glScalef(.2f, 2.8f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.0f, -.2f, -6.5f);
        gl.glColor3f(21 / 255f, 23 / 255f, 18 / 255f);
        gl.glScalef(.2f, 2.8f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2.0f, -.2f, -6.5f);
        gl.glColor3f(21 / 255f, 23 / 255f, 18 / 255f);
        gl.glScalef(.2f, 2.8f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-3.0f, -.2f, -6.5f);
        gl.glColor3f(21 / 255f, 23 / 255f, 18 / 255f);
        gl.glScalef(.2f, 2.8f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2f, -.9f, -6.5f);
        gl.glColor3f(21 / 255f, 23 / 255f, 18 / 255f);
        gl.glScalef(2, .1f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2f, -1.5f, -6.5f);
        gl.glColor3f(124 / 255f, 125 / 255f, 120 / 255f);
        gl.glScalef(2, .2f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(2f, -.9f, -6.5f);
        gl.glColor3f(21 / 255f, 23 / 255f, 18 / 255f);
        gl.glScalef(2, .1f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-2f, -1.5f, -6.5f);
        gl.glColor3f(124 / 255f, 125 / 255f, 120 / 255f);
        gl.glScalef(2, .2f, .2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //******************************************
        //front of the walmart board 
        //red lines
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 2.7f, -6.5f);
        gl.glColor3f(198 / 255f, 32 / 255f, 32 / 255f);
        gl.glScalef(6, .4f, 1);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0f, -6.5f);
        gl.glColor3f(198 / 255f, 32 / 255f, 32 / 255f);
        gl.glScalef(6, .2f, 1);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //blue board 
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.4f, -6.5f);
        gl.glColor3f(55 / 255f, 67 / 255f, 144 / 255f);
        gl.glScalef(6f, 2.5f, 1);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

//-----------------------------------------------------------------
        //column one 
        gl.glTranslatef(3.5f, -1.0f, -6.0f);
        gl.glColor3f(124 / 255f, 125 / 255f, 120 / 255f);
        gl.glScalef(1, 1.5f, 1);
        glut.glutSolidCube(1);

        gl.glTranslatef(0.0f, 0.6f, 0.0f);
        gl.glColor3f(55 / 255f, 67 / 255f, 144 / 255f);
        gl.glScalef(1, 0.2f, 1);
        glut.glutSolidCube(1);

        gl.glTranslatef(0.0f, 5f, 0.0f);
        gl.glColor3f(124 / 255f, 125 / 255f, 120 / 255f);
        gl.glScalef(1, 9f, 1);
        glut.glutSolidCube(1);

        gl.glTranslatef(0.0f, .5f, 0.0f);
        gl.glColor3f(55 / 255f, 67 / 255f, 144 / 255f);
        gl.glScalef(1, .1f, 1);
        glut.glutSolidCube(1);

        gl.glTranslatef(0.0f, 1f, 0.0f);
        gl.glColor3f(124 / 255f, 125 / 255f, 120 / 255f);
        gl.glScalef(1, 1f, 1);
        glut.glutSolidCube(1);
        gl.glTranslatef(0.0f, 0.0f, 0.0f);
        gl.glPopMatrix();

        //column 2
        gl.glPushMatrix();
        gl.glTranslatef(-3.5f, -1.0f, -6.0f);
        gl.glColor3f(124 / 255f, 125 / 255f, 120 / 255f);
        gl.glScalef(1, 1.5f, 1);
        glut.glutSolidCube(1);
        //the blue line 
        gl.glTranslatef(0.0f, 0.6f, 0.0f);
        gl.glColor3f(55 / 255f, 67 / 255f, 144 / 255f);
        gl.glScalef(1, 0.2f, 1);
        glut.glutSolidCube(1);

        gl.glTranslatef(0.0f, 5f, 0.0f);
        gl.glColor3f(124 / 255f, 125 / 255f, 120 / 255f);
        gl.glScalef(1, 9f, 1);
        glut.glutSolidCube(1);
        //the blue line 
        gl.glTranslatef(0.0f, .5f, 0.0f);
        gl.glColor3f(55 / 255f, 67 / 255f, 144 / 255f);
        gl.glScalef(1, .1f, 1);
        glut.glutSolidCube(1);

        gl.glTranslatef(0.0f, 1f, 0.0f);
        gl.glColor3f(124 / 255f, 125 / 255f, 120 / 255f);
        gl.glScalef(1, 1f, 1);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //cart 
        gl.glPushMatrix();
        gl.glTranslatef(0.2f, .68f, -5.9f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, 1f, 0f);
        glut.glutSolidCylinder(.1f, 3, 50, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.2f, .68f, -5.9f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, 1f, 0f);
        glut.glutSolidCylinder(.1f, 3, 50, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, .8f, -5.9f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, .1f, 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.2f, -5.9f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, .1f, 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-.5f, 1.4f, -5.9f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.1f, 1.3f, 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-.7f, 2f, -5.9f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.4f, .1f, 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(.5f, 1.5f, -5.9f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.1f, .7f, 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(.3f, 1.5f, -5.9f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.02f, .7f, 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(.1f, 1.5f, -5.9f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.02f, .7f, 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-.1f, 1.5f, -5.9f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.02f, .7f, 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.5f, -5.9f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, .02f, 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-.3f, 1.5f, -5.9f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.02f, .7f, 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.8f, -5.9f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, .1f, 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
    }

    // Bayader's work
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

    // Nouf's work
    public void MasjedNew(GLUT glut, GL gl) {

        gl.glPushMatrix();

        gl.glTranslatef(2f, 0.0f, 0f);
        //gl.glRotatef(60, 0, 1, 0);
        gl.glScalef(0.5f, 0.5f, 0.5f);

        Qubba(glut, gl);
        Methana(glut, gl);
        bulding(glut, gl);

        gl.glPopMatrix();

    }

    // Rahaf's work
    public void modernHouses(GL gl, GLUT glut) {
        // ================================================== 1th house ==================================================
        gl.glPushMatrix();

        // house1
        gl.glPushMatrix();
        gl.glTranslatef(0, 2, -6.0f);
        gl.glScalef(1, 1.9f, 1);
        gl.glColor3d(158 / 255f, 139 / 255f, 134 / 255f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //roof
        gl.glPushMatrix();
        gl.glTranslatef(0, 5.9f, -6f);
        //gl.glScalef(1f, 0.08f, 0f);
        gl.glScalef(1f, 0.08f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //back
        gl.glPushMatrix();
        gl.glTranslatef(0, 5.95f, -7.6f);
        gl.glScalef(1f, 0.2f, -0.2f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //right
        gl.glPushMatrix();
        gl.glTranslatef(1.3f, 5.95f, -6f);
        gl.glScalef(0.3f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //left
        gl.glPushMatrix();
        gl.glTranslatef(-1.3f, 5.95f, -6f);
        gl.glScalef(0.3f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //front
        gl.glPushMatrix();
        gl.glTranslatef(0, 5.95f, -6f);
        gl.glScalef(1f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        // windos
        float y = 0f;

        for (int i = 0; i < 5; i++) {
            gl.glPushMatrix();
            gl.glTranslatef(-1f, 4.5f + y, -4);
            gl.glScalef(1.7f, 0.8f, 0);
            gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glTranslatef(1f, 4.5f + y, -4f);
            gl.glScalef(1.7f, 0.8f, 0);
            gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            y -= 1;

        }

        // door
        gl.glPushMatrix();
        gl.glTranslatef(0f, -1.15f, -4f);
        gl.glScalef(1.5f, 1.3f, 0);
        gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        y = 0f;

        for (int i = 0; i < 5; i++) {
            //line on window
            gl.glPushMatrix();
            gl.glTranslatef(-1, 4.15f + y, -4.0f);
            gl.glScalef(1.7f, 0.1f, 0f);
            gl.glColor3d(208 / 255f, 212 / 255f, 223 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glTranslatef(1, 4.15f + y, -4.0f);
            gl.glScalef(1.7f, 0.1f, 0f);
            gl.glColor3d(208 / 255f, 212 / 255f, 223 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            y -= 1f;
        }
        gl.glPopMatrix();

        // ================================================== 2th house ==================================================
        gl.glPushMatrix();

        // house2
        gl.glPushMatrix();
        gl.glTranslatef(5, 2, -6.0f);
        gl.glScalef(1, 1.9f, 1);
        gl.glColor3d(149 / 255f, 152 / 255f, 157 / 255f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //roof
        gl.glPushMatrix();
        gl.glTranslatef(5, 5.9f, -6f);
        //gl.glScalef(1f, 0.08f, 0f);
        gl.glScalef(1f, 0.08f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //back
        gl.glPushMatrix();
        gl.glTranslatef(5, 5.95f, -7.6f);
        gl.glScalef(1f, 0.2f, -0.2f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //right
        gl.glPushMatrix();
        gl.glTranslatef(1.3f + 5f, 5.95f, -6f);
        gl.glScalef(0.3f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //left
        gl.glPushMatrix();
        gl.glTranslatef(-1.3f + 5f, 5.95f, -6f);
        gl.glScalef(0.3f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //front
        gl.glPushMatrix();
        gl.glTranslatef(5f, 5.95f, -6f);
        gl.glScalef(1f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        // windos
        y = 0f;

        for (int i = 0; i < 5; i++) {
            gl.glPushMatrix();
            gl.glTranslatef(-1f + 5f, 4.5f + y, -4);
            gl.glScalef(1.7f, 0.8f, 0);
            gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glTranslatef(1f + 5f, 4.5f + y, -4f);
            gl.glScalef(1.7f, 0.8f, 0);
            gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            y -= 1;

        }

        // door
        gl.glPushMatrix();
        gl.glTranslatef(5f, -1.15f, -4f);
        gl.glScalef(1.5f, 1.3f, 0);
        gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        y = 0f;

        for (int i = 0; i < 5; i++) {
            //line on window
            gl.glPushMatrix();
            gl.glTranslatef(-1 + 5f, 4.15f + y, -4.0f);
            gl.glScalef(1.7f, 0.1f, 0f);
            gl.glColor3d(1f, 1f, 1f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glTranslatef(1 + 5f, 4.15f + y, -4.0f);
            gl.glScalef(1.7f, 0.1f, 0f);
            gl.glColor3d(1f, 1f, 1f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            y -= 1f;
        }
        gl.glPopMatrix();

        // ================================================== 3th house ==================================================
        gl.glPushMatrix();

        // house2
        gl.glPushMatrix();
        gl.glTranslatef(-5, 2, -6.0f);
        gl.glScalef(1, 1.9f, 1);
        gl.glColor3f(123 / 255f, 122 / 255f, 122 / 255f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //roof
        gl.glPushMatrix();
        gl.glTranslatef(-5, 5.9f, -6f);
        //gl.glScalef(1f, 0.08f, 0f);
        gl.glScalef(1f, 0.08f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //back
        gl.glPushMatrix();
        gl.glTranslatef(-5, 5.95f, -7.6f);
        gl.glScalef(1f, 0.2f, -0.2f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //right
        gl.glPushMatrix();
        gl.glTranslatef(1.3f - 5f, 5.95f, -6f);
        gl.glScalef(0.3f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //left
        gl.glPushMatrix();
        gl.glTranslatef(-1.3f - 5f, 5.95f, -6f);
        gl.glScalef(0.3f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //front
        gl.glPushMatrix();
        gl.glTranslatef(-5f, 5.95f, -6f);
        gl.glScalef(1f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        // windos
        y = 0f;

        for (int i = 0; i < 5; i++) {
            gl.glPushMatrix();
            gl.glTranslatef(-1f - 5f, 4.5f + y, -4);
            gl.glScalef(1.7f, 0.8f, 0);
            gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glTranslatef(1f - 5f, 4.5f + y, -4f);
            gl.glScalef(1.7f, 0.8f, 0);
            gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            y -= 1;

        }

        // door
        gl.glPushMatrix();
        gl.glTranslatef(-5f, -1.15f, -4f);
        gl.glScalef(1.5f, 1.3f, 0);
        gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        y = 0f;

        for (int i = 0; i < 5; i++) {
            //line on window
            gl.glPushMatrix();
            gl.glTranslatef(-1 - 5f, 4.15f + y, -4.0f);
            gl.glScalef(1.7f, 0.1f, 0f);
            gl.glColor3d(1f, 1f, 1f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glTranslatef(1 - 5f, 4.15f + y, -4.0f);
            gl.glScalef(1.7f, 0.1f, 0f);
            gl.glColor3d(1f, 1f, 1f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            y -= 1f;
        }
        gl.glPopMatrix();
    }

    // Najwa's work
    public static void clinic(GL gl, GLUT glut) {

        // FIRST BUILDING  (The bigger one) ------------------------------------
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -15f);
        gl.glColor3f(231 / 255f, 233 / 255f, 232 / 255f);
        gl.glScalef(4, 2.2f, 2);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // ROOF 
        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.1f, -15f);
        gl.glColor3f(186 / 255f, 187 / 255f, 192 / 255f);
        gl.glScalef(4, 0.01f, 2);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //Right
        gl.glPushMatrix();
        gl.glTranslatef(1.95f, 1.1f, -15f);
        gl.glColor3f(231 / 255f, 233 / 255f, 232 / 255f);
        gl.glScalef(0.07f, 0.07f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Back
        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.1f, -15.95f);
        gl.glColor3f(231 / 255f, 233 / 255f, 232 / 255f);
        gl.glScalef(4, 0.07f, 0.07f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //Left
        gl.glPushMatrix();
        gl.glTranslatef(-1.95f, 1.1f, -15f);
        gl.glColor3f(231 / 255f, 233 / 255f, 232 / 255f);
        gl.glScalef(0.07f, 0.07f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Front
        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.1f, -14.04f);
        gl.glColor3f(231 / 255f, 233 / 255f, 232 / 255f);
        gl.glScalef(4, 0.07f, 0.07f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // WiINDOWS OF THE FIRST BULDING
        // Front, Below
        gl.glPushMatrix();
        gl.glTranslatef(-0.76f, 0f, -14f);
        gl.glRotatef(0, 1, 1, 0);
        gl.glColor3f(142 / 255f, 189 / 255f, 227 / 255f);
        gl.glScalef(2.47f, 0.34f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Front, Above
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.6f, -14f);
        gl.glRotatef(0, 1, 1, 0);
        gl.glColor3f(142 / 255f, 189 / 255f, 227 / 255f);
        gl.glScalef(4f, 0.34f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

//        // Back, Above
//        gl.glPushMatrix();
//        gl.glTranslatef(0f, 0.6f, -16f); 
//        gl.glRotatef(0, 1, 1, 0);
//        gl.glColor3f(0f, 1f, 0f);
//        gl.glScalef(4f, 0.34f, 0.01f);
//        glut.glutSolidCube(1);
//        gl.glPopMatrix();
//        
//        // Back, Middle
//        gl.glPushMatrix();
//        gl.glTranslatef(0f, 0f, -16f); 
//        gl.glRotatef(0, 1, 1, 0);
//        gl.glColor3f(0f, 1f, 0f);
//        gl.glScalef(4f, 0.34f, 0.01f);
//        glut.glutSolidCube(1);
//        gl.glPopMatrix();
//        
//        // Back, Below
//        gl.glPushMatrix();
//        gl.glTranslatef(0f, -0.6f, -16f); 
//        gl.glRotatef(0, 1, 1, 0);
//        gl.glColor3f(0f, 1f, 0f);
//        gl.glScalef(4f, 0.34f, 0.01f);
//        glut.glutSolidCube(1);
//        gl.glPopMatrix();
//        // Right Side, Above
//        gl.glPushMatrix();
//        gl.glTranslatef(2f, 0.6f, -14.99f); 
//        gl.glRotatef(90, 0, 1, 0);
//        gl.glColor3f(0f, 1f, 0f);
//        gl.glScalef(2f, 0.35f, 0.01f);
//        glut.glutSolidCube(1);
//        gl.glPopMatrix();
//        // Left Side, Above
//        gl.glPushMatrix();
//        gl.glTranslatef(-2f, 0.6f, -14.99f); 
//        gl.glRotatef(90, 0, 1, 0);
//        gl.glColor3f(0f, 1f, 0f);
//        gl.glScalef(2f, 0.35f, 0.01f);
//        glut.glutSolidCube(1);
//        gl.glPopMatrix();
//        
//        // Left Side, Middle
//        gl.glPushMatrix();
//        gl.glTranslatef(-2f, 0.0f, -14.99f); 
//        gl.glRotatef(90, 0, 1, 0);
//        gl.glColor3f(0f, 1f, 0f);
//        gl.glScalef(2f, 0.35f, 0.01f);
//        glut.glutSolidCube(1);
//        gl.glPopMatrix();
//        
//        // Left Side, Below
//        gl.glPushMatrix();
//        gl.glTranslatef(-2f, -0.6f, -14.99f); 
//        gl.glRotatef(90, 0, 1, 0);
//        gl.glColor3f(0f, 1f, 0f);
//        gl.glScalef(2f, 0.35f, 0.01f);
//        glut.glutSolidCube(1);
//        gl.glPopMatrix();
        // GATE 
        // Door
        gl.glPushMatrix();
        gl.glTranslatef(-0.7f, -0.85f, -14f);
        gl.glRotatef(0, 1, 1, 0);
        gl.glColor3f(139 / 255f, 184 / 255f, 221 / 255f);
        gl.glScalef(1f, 0.5f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Column 1
        gl.glPushMatrix();
        gl.glTranslatef(-1.3f, -0.93f, -13.65f);
        gl.glRotatef(0, 1, 1, 0);
        gl.glColor3f(188 / 255f, 189 / 255f, 191 / 255f);
        gl.glScalef(0.09f, 0.69f, 0.14f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Column 2
        gl.glPushMatrix();
        gl.glTranslatef(-0.1f, -0.96f, -13.65f);
        gl.glRotatef(0, 1, 1, 0);
        gl.glColor3f(188 / 255f, 189 / 255f, 191 / 255f);
        gl.glScalef(0.09f, 0.69f, 0.14f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Flat Triangle
        gl.glPushMatrix();
        gl.glTranslatef(-0.7f, -0.6f, -14f);
        gl.glColor3f(210 / 255f, 211 / 255f, 213 / 255f);
        gl.glScalef(1.3f, 0.05f, 0.8f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Not flat friangle 
        gl.glPushMatrix();
        gl.glTranslatef(-0.7f, -0.27f, -13.65f);
        gl.glColor3f(210 / 255f, 211 / 255f, 213 / 255f);
        gl.glScalef(1.3f, 0.62f, 0.1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Cross
        gl.glPushMatrix();
        gl.glTranslatef(-0.7f, -0.27f, -13.56f);
        gl.glColor3f(238 / 255f, 71 / 255f, 62 / 255f);
        gl.glScalef(0.1f, 0.4f, 0.07f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Cross
        gl.glPushMatrix();
        gl.glTranslatef(-0.7f, -0.27f, -13.56f);
        gl.glColor3f(238 / 255f, 71 / 255f, 62 / 255f);
        gl.glScalef(0.4f, 0.1f, 0.07f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // SECOND BUILDING (The smaller one)------------------------------------
        gl.glPushMatrix();
        gl.glTranslatef(1.25f, -0.39f, -13f);
        gl.glRotatef(0, 1, 1, 0);
        gl.glColor3f(231 / 255f, 233 / 255f, 232 / 255f);
        gl.glScalef(1.5f, 1.5f, 2.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // ROOF
        gl.glPushMatrix();
        gl.glTranslatef(1.25f, 0.37f, -13f);
        gl.glRotatef(0, 1, 1, 0);
        gl.glColor3f(186 / 255f, 187 / 255f, 192 / 255f);
        gl.glScalef(1.5f, 0.01f, 2.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Left
        gl.glPushMatrix();
        gl.glTranslatef(1.96f, 0.4f, -13f);
        gl.glRotatef(0, 1, 1, 0);
        gl.glColor3f(231 / 255f, 233 / 255f, 232 / 255f);
        gl.glScalef(0.07f, 0.07f, 2.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Right
        gl.glPushMatrix();
        gl.glTranslatef(0.54f, 0.4f, -13f);
        gl.glRotatef(0, 1, 1, 0);
        gl.glColor3f(231 / 255f, 233 / 255f, 232 / 255f);
        gl.glScalef(0.07f, 0.07f, 2.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Front
        gl.glPushMatrix();
        gl.glTranslatef(1.25f, 0.4f, -11.78f);
        gl.glRotatef(0, 1, 1, 0);
        gl.glColor3f(231 / 255f, 233 / 255f, 232 / 255f);
        gl.glScalef(1.5f, 0.07f, 0.07f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // WiINDOWS OF THE SECOND BUILDING
        // Front, Below
        gl.glPushMatrix();
        gl.glTranslatef(1.25f, -0.7f, -11.74f);
        gl.glRotatef(0, 1, 1, 0);
        gl.glColor3f(142 / 255f, 189 / 255f, 227 / 255f);
        gl.glScalef(1.5f, 0.35f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Front, Above
        gl.glPushMatrix();
        gl.glTranslatef(1.25f, -0.1f, -11.74f);
        gl.glRotatef(0, 1, 1, 0);
        gl.glColor3f(142 / 255f, 189 / 255f, 227 / 255f);
        gl.glScalef(1.5f, 0.35f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Left Side, Below
        gl.glPushMatrix();
        gl.glTranslatef(0.49f, -0.1f, -13f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glColor3f(142 / 255f, 189 / 255f, 227 / 255f);
        gl.glScalef(2.5f, 0.35f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Left Side, Above
        gl.glPushMatrix();
        gl.glTranslatef(0.49f, -0.7f, -13f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glColor3f(142 / 255f, 189 / 255f, 227 / 255f);
        gl.glScalef(2.5f, 0.35f, 0.01f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

//        // Right Side, Below
//        gl.glPushMatrix();
//        gl.glTranslatef(2f, -0.7f, -13.88f); 
//        gl.glRotatef(90, 0, 1, 0);
//        gl.glColor3f(0f, 1f, 0f);
//        gl.glScalef(4.25f, 0.35f, 0.01f);
//        glut.glutSolidCube(1);
//        gl.glPopMatrix();
//        
//        // Right Side, Above
//        gl.glPushMatrix();
//        gl.glTranslatef(2f, -0.1f, -13.88f); 
//        gl.glRotatef(90, 0, 1, 0);
//        gl.glColor3f(0f, 1f, 0f);
//        gl.glScalef(4.25f, 0.35f, 0.01f);
//        glut.glutSolidCube(1);
//        gl.glPopMatrix();
        gl.glPopMatrix();

        gl.glFlush();

    }

    // Razan's work
    public static void modernHouse(GL gl, GLUT glut) {
        gl.glPushMatrix();//BIG PUSH

        //bottom structure
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        gl.glColor3f(165 / 255f, 165 / 255f, 165 / 255f);
        gl.glScalef(3f, 1, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //window1
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.1f, -5f);
        gl.glColor3f(182 / 255f, 244 / 255f, 252 / 252f);
        gl.glScalef(2f, .5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(.98f, 0.1f, -5f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.05f, .5f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-.98f, 0.1f, -5f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.05f, .5f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(.4f, 0.1f, -5f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.05f, .5f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-.4f, 0.1f, -5f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.05f, .5f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -.15f, -5f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(2f, .05f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, .35f, -5f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(2f, .05f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        //End of W1

        //door
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -.38f, -5f);
        gl.glColor3f(137 / 255f, 137 / 255f, 137 / 255f);
        gl.glScalef(.3f, .25f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        //end of door

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, .5f, -6.0f);
        gl.glColor3f(51 / 255f, 51 / 255f, 51 / 255f);
        gl.glScalef(3.2f, 0.08f, 2.2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //--------------------
        //upper structer
        gl.glPushMatrix();
        gl.glTranslatef(-.7f, 1.25f, -6.0f);
        gl.glColor3f(165 / 255f, 165 / 255f, 165 / 255f);
        gl.glScalef(1.5f, 1.4f, 1.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //Window 2 
        gl.glPushMatrix();
        gl.glTranslatef(-.7f, 1.25f, -5.2f);
        gl.glColor3f(182 / 255f, 244 / 255f, 252 / 252f);
        gl.glScalef(1f, .8f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-.7f, .85f, -5.2f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, .05f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-.22f, 1.25f, -5.2f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.05f, .8f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.17f, 1.25f, -5.2f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.05f, .8f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-.89f, 1.25f, -5.2f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.05f, .8f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-.5f, 1.25f, -5.2f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(.05f, .8f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-.7f, 1.65f, -5.2f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, .05f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //End of w2 
        gl.glPushMatrix();
        gl.glTranslatef(-.7f, 1.9f, -6.0f);
        gl.glColor3f(51 / 255f, 51 / 255f, 51 / 255f);
        gl.glScalef(1.7f, 0.08f, 1.7f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //white 
        gl.glPushMatrix();
        gl.glTranslatef(.89f, 1.04f, -6.0f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1.2f, 1f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //white 
        gl.glPushMatrix();
        gl.glTranslatef(.89f, 1.55f, -6.0f);
        gl.glColor3f(84 / 255f, 68 / 255f, 41 / 255f);
        gl.glScalef(.5f, .08f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(.89f, 1.06f, -5f);
        gl.glColor3f(84 / 255f, 68 / 255f, 41 / 255f);
        gl.glScalef(.5f, 1.05f, .08f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        //----------------------

        gl.glPopMatrix();//BIG POP
    }

    public void Stadium(GL gl, GLUT glut) {
        gl.glPushMatrix();

        // the brown thing on the left
        gl.glPushMatrix();
        gl.glTranslatef(-4.48f, 0f, 0f);
        gl.glScalef(0.005f, 0.4f, 0.8f);
        gl.glColor3d(142 / 255f, 77 / 255f, 47 / 255f);
        glut.glutSolidCube(2f);
        gl.glPopMatrix();

        // the brown thing on the right
        gl.glPushMatrix();
        gl.glTranslatef(4.48f, 0f, 0f);
        gl.glScalef(0.005f, 0.4f, 0.8f);
        gl.glColor3d(142 / 255f, 77 / 255f, 47 / 255f);
        glut.glutSolidCube(2f);
        gl.glPopMatrix();

        // columns 
        gl.glPushMatrix();
        gl.glTranslatef(-4.5f, -0.25f, 0f);
        gl.glColor3f(171 / 255f, 171 / 255f, 171 / 255f);
        gl.glScalef(.1f, 0.6f, .5f);
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(.2f, 3, 50, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(4.5f, -0.25f, 0f);
        gl.glColor3f(171 / 255f, 171 / 255f, 171 / 255f);
        gl.glScalef(.1f, 0.6f, .5f);
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(.2f, 3, 50, 15);
        gl.glPopMatrix();

        gl.glRotatef(270, 1, 0, 0);

        // begin of ground Stadium
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, -2f);
        gl.glScalef(5f, 2f, 0);
        gl.glColor3d(71 / 255f, 135f / 255f, 181 / 255f);
        glut.glutSolidCube(2f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0, 0, -1.999f);
        gl.glScalef(4.89f, 1.89f, 0);
        gl.glColor3d(249 / 255f, 196 / 255f, 56 / 255f);
        glut.glutSolidCube(2f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(4.3f, 0, -1.998f);
        gl.glScalef(0.7f, 1.3f, 0);
        gl.glColor3d(71 / 255f, 135f / 255f, 181 / 255f);
        glut.glutSolidCube(2f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(4.3f, 0, -1.997f);
        gl.glScalef(0.6f, 1.2f, 0);
        gl.glColor3d(249 / 255f, 196 / 255f, 56 / 255f);
        glut.glutSolidCube(2f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0, 0, -1.99f);
        gl.glColor3d(71 / 255f, 135f / 255f, 181 / 255f);
        gl.glScalef(1.3f, 1f, 0f);
        glut.glutSolidCylinder(1.5f, 3, 50, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0, 0, -1.98f);
        gl.glColor3d(249 / 255f, 196 / 255f, 56 / 255f);
        gl.glScalef(1.3f, 1f, 0f);
        glut.glutSolidCylinder(1.4f, 3, 50, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-4.3f, 0, -1.998f);
        gl.glScalef(0.7f, 1.3f, 0);
        gl.glColor3d(71 / 255f, 135f / 255f, 181 / 255f);
        glut.glutSolidCube(2f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-4.3f, 0, -1.997f);
        gl.glScalef(0.6f, 1.2f, 0);
        gl.glColor3d(249 / 255f, 196 / 255f, 56 / 255f);
        glut.glutSolidCube(2f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0, 0, -1.97f);
        gl.glScalef(0.1f, 4f, 0);
        gl.glColor3d(71 / 255f, 135f / 255f, 181 / 255f);
        glut.glutSolidCube(1f);
        gl.glPopMatrix();
        //end of the ground

        gl.glPopMatrix();

    }

    public void ModernHouses(GL gl, GLUT glut) {
        // ================================================== 1th house ==================================================
        gl.glPushMatrix();

        // house1
        gl.glPushMatrix();
        gl.glTranslatef(0, 2, -6.0f);
        gl.glScalef(1, 1.9f, 1);
        gl.glColor3d(158 / 255f, 139 / 255f, 134 / 255f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //roof
        gl.glPushMatrix();
        gl.glTranslatef(0, 5.9f, -6f);
        //gl.glScalef(1f, 0.08f, 0f);
        gl.glScalef(1f, 0.08f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //back
        gl.glPushMatrix();
        gl.glTranslatef(0, 5.95f, -7.6f);
        gl.glScalef(1f, 0.2f, -0.2f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //right
        gl.glPushMatrix();
        gl.glTranslatef(1.3f, 5.95f, -6f);
        gl.glScalef(0.3f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //left
        gl.glPushMatrix();
        gl.glTranslatef(-1.3f, 5.95f, -6f);
        gl.glScalef(0.3f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //front
        gl.glPushMatrix();
        gl.glTranslatef(0, 5.95f, -6f);
        gl.glScalef(1f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        // windos
        float y = 0f;

        for (int i = 0; i < 5; i++) {
            gl.glPushMatrix();
            gl.glTranslatef(-1f, 4.5f + y, -4);
            gl.glScalef(1.7f, 0.8f, 0);
            gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glTranslatef(1f, 4.5f + y, -4f);
            gl.glScalef(1.7f, 0.8f, 0);
            gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            y -= 1;

        }

        // door
        gl.glPushMatrix();
        gl.glTranslatef(0f, -1.15f, -4f);
        gl.glScalef(1.5f, 1.3f, 0);
        gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        y = 0f;

        for (int i = 0; i < 5; i++) {
            //line on window
            gl.glPushMatrix();
            gl.glTranslatef(-1, 4.15f + y, -4.0f);
            gl.glScalef(1.7f, 0.1f, 0f);
            gl.glColor3d(208 / 255f, 212 / 255f, 223 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glTranslatef(1, 4.15f + y, -4.0f);
            gl.glScalef(1.7f, 0.1f, 0f);
            gl.glColor3d(208 / 255f, 212 / 255f, 223 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            y -= 1f;
        }
        gl.glPopMatrix();

        // ================================================== 2th house ==================================================
        gl.glPushMatrix();

        // house2
        gl.glPushMatrix();
        gl.glTranslatef(5, 2, -6.0f);
        gl.glScalef(1, 1.9f, 1);
        gl.glColor3d(149 / 255f, 152 / 255f, 157 / 255f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //roof
        gl.glPushMatrix();
        gl.glTranslatef(5, 5.9f, -6f);
        //gl.glScalef(1f, 0.08f, 0f);
        gl.glScalef(1f, 0.08f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //back
        gl.glPushMatrix();
        gl.glTranslatef(5, 5.95f, -7.6f);
        gl.glScalef(1f, 0.2f, -0.2f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //right
        gl.glPushMatrix();
        gl.glTranslatef(1.3f + 5f, 5.95f, -6f);
        gl.glScalef(0.3f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //left
        gl.glPushMatrix();
        gl.glTranslatef(-1.3f + 5f, 5.95f, -6f);
        gl.glScalef(0.3f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //front
        gl.glPushMatrix();
        gl.glTranslatef(5f, 5.95f, -6f);
        gl.glScalef(1f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        // windos
        y = 0f;

        for (int i = 0; i < 5; i++) {
            gl.glPushMatrix();
            gl.glTranslatef(-1f + 5f, 4.5f + y, -4);
            gl.glScalef(1.7f, 0.8f, 0);
            gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glTranslatef(1f + 5f, 4.5f + y, -4f);
            gl.glScalef(1.7f, 0.8f, 0);
            gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            y -= 1;

        }

        // door
        gl.glPushMatrix();
        gl.glTranslatef(5f, -1.15f, -4f);
        gl.glScalef(1.5f, 1.3f, 0);
        gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        y = 0f;

        for (int i = 0; i < 5; i++) {
            //line on window
            gl.glPushMatrix();
            gl.glTranslatef(-1 + 5f, 4.15f + y, -4.0f);
            gl.glScalef(1.7f, 0.1f, 0f);
            gl.glColor3d(1f, 1f, 1f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glTranslatef(1 + 5f, 4.15f + y, -4.0f);
            gl.glScalef(1.7f, 0.1f, 0f);
            gl.glColor3d(1f, 1f, 1f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            y -= 1f;
        }
        gl.glPopMatrix();

        // ================================================== 3th house ==================================================
        gl.glPushMatrix();

        // house2
        gl.glPushMatrix();
        gl.glTranslatef(-5, 2, -6.0f);
        gl.glScalef(1, 1.9f, 1);
        gl.glColor3f(123 / 255f, 122 / 255f, 122 / 255f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //roof
        gl.glPushMatrix();
        gl.glTranslatef(-5, 5.9f, -6f);
        //gl.glScalef(1f, 0.08f, 0f);
        gl.glScalef(1f, 0.08f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //back
        gl.glPushMatrix();
        gl.glTranslatef(-5, 5.95f, -7.6f);
        gl.glScalef(1f, 0.2f, -0.2f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //right
        gl.glPushMatrix();
        gl.glTranslatef(1.3f - 5f, 5.95f, -6f);
        gl.glScalef(0.3f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //left
        gl.glPushMatrix();
        gl.glTranslatef(-1.3f - 5f, 5.95f, -6f);
        gl.glScalef(0.3f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //front
        gl.glPushMatrix();
        gl.glTranslatef(-5f, 5.95f, -6f);
        gl.glScalef(1f, 0.2f, -1f);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        // windos
        y = 0f;

        for (int i = 0; i < 5; i++) {
            gl.glPushMatrix();
            gl.glTranslatef(-1f - 5f, 4.5f + y, -4);
            gl.glScalef(1.7f, 0.8f, 0);
            gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glTranslatef(1f - 5f, 4.5f + y, -4f);
            gl.glScalef(1.7f, 0.8f, 0);
            gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            y -= 1;

        }

        // door
        gl.glPushMatrix();
        gl.glTranslatef(-5f, -1.15f, -4f);
        gl.glScalef(1.5f, 1.3f, 0);
        gl.glColor3d(65 / 255f, 85 / 255f, 122 / 255f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        y = 0f;

        for (int i = 0; i < 5; i++) {
            //line on window
            gl.glPushMatrix();
            gl.glTranslatef(-1 - 5f, 4.15f + y, -4.0f);
            gl.glScalef(1.7f, 0.1f, 0f);
            gl.glColor3d(1f, 1f, 1f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glTranslatef(1 - 5f, 4.15f + y, -4.0f);
            gl.glScalef(1.7f, 0.1f, 0f);
            gl.glColor3d(1f, 1f, 1f);
            glut.glutSolidCube(1);
            gl.glPopMatrix();

            y -= 1f;
        }
        gl.glPopMatrix();
    }

    public void ModernHouse2(GL gl, GLUT glut) {
        gl.glPushMatrix();

        // building
        gl.glPushMatrix();
        gl.glTranslatef(0, 2, -6.0f);
        gl.glScalef(1.4f, 1f, 1.4f);
        gl.glColor3d(213 / 255f, 193 / 255f, 184 / 255f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0, 4f, -6f);
        gl.glScalef(13f, -0.5f, 13f);
        gl.glColor3d(77 / 255f, 79 / 255f, 88 / 255f);
        glut.glutSolidCube(0.5f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0, 6.1f, -6.0f);
        gl.glScalef(1.4f, 1f, 1.4f);
        gl.glColor3d(213 / 255f, 193 / 255f, 184 / 255f);
        glut.glutSolidCube(4);
        gl.glPopMatrix();

        //roof
        gl.glPushMatrix();
        gl.glTranslatef(0, 8f, -6.1f);
        gl.glScalef(10f, -0.5f, 10f);
        gl.glColor3d(213 / 255f, 193 / 255f, 184 / 255f);
        glut.glutSolidCube(0.5f);
        gl.glPopMatrix();

        //left
        gl.glPushMatrix();
        gl.glTranslatef(-2.7f, 8f, -6f);
        gl.glScalef(1f, 1f, 10f);
        gl.glColor3d(77 / 255f, 79 / 255f, 88 / 255f);
        glut.glutSolidCube(0.5f);
        gl.glPopMatrix();

        //right
        gl.glPushMatrix();
        gl.glTranslatef(2.7f, 8f, -6f);
        gl.glScalef(1f, 1f, 10f);
        gl.glColor3d(77 / 255f, 79 / 255f, 88 / 255f);
        glut.glutSolidCube(0.5f);
        gl.glPopMatrix();

        //front
        gl.glPushMatrix();
        gl.glTranslatef(0f, 8f, -3.4f);
        gl.glScalef(11f, 1f, 1f);
        gl.glColor3d(77 / 255f, 79 / 255f, 88 / 255f);
        glut.glutSolidCube(0.5f);
        gl.glPopMatrix();

        //back
        gl.glPushMatrix();
        gl.glTranslatef(0f, 8f, -8.5f);
        gl.glScalef(11f, 1f, 1f);
        gl.glColor3d(77 / 255f, 79 / 255f, 88 / 255f);
        glut.glutSolidCube(0.5f);
        gl.glPopMatrix();

        // window 1 
        // white frame
        gl.glPushMatrix();
        gl.glTranslatef(0f, 6f, -3.1f);
        gl.glScalef(2.4f, 0.7f, 0);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(2f);
        gl.glPopMatrix();

        // blue frame
        gl.glPushMatrix();
        gl.glTranslatef(0f, 6f, -3);
        gl.glScalef(2.9f, 0.7f, 0);
        gl.glColor3d(180 / 255f, 238 / 255f, 244 / 255f);
        glut.glutSolidCube(1.5f);
        gl.glPopMatrix();

        // accsseroy
        gl.glPushMatrix();
        gl.glTranslatef(0f, 6f, -2.9f);
        gl.glScalef(0.1f, 0.65f, 0);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(2f);
        gl.glPopMatrix();

        // window 2
        // white frame
        gl.glPushMatrix();
        gl.glTranslatef(-1f, 2f, -3.1f);
        gl.glScalef(2.4f / 2.0f, 0.7f, 0);
        gl.glColor3d(1f, 1f, 1f);
        glut.glutSolidCube(2f);
        gl.glPopMatrix();

        // blue frame
        gl.glPushMatrix();
        gl.glTranslatef(-1f, 2f, -3);
        gl.glScalef(2.6f / 2.0f, 0.7f, 0);
        gl.glColor3d(180 / 255f, 238 / 255f, 244 / 255f);
        glut.glutSolidCube(1.5f);
        gl.glPopMatrix();

        // door
        gl.glPushMatrix();
        gl.glTranslatef(1.3f, 1f, -3.1f);
        gl.glScalef(1.5f, 2.5f, 0);
        gl.glColor3d(77 / 255f, 79 / 255f, 88 / 255f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();
    }

    public static void ModernSchool(GL gl, GLUT glut) {
        gl.glPushMatrix();//big push  

        //-----------------------------------------
        //right building
        gl.glPushMatrix();
        gl.glTranslatef(3f, -0.5f, -6.0f);
        gl.glColor3f(124 / 255f, 87 / 255f, 70 / 255f);
        gl.glScalef(3.5f, 2f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // its gray lines
        gl.glPushMatrix();
        gl.glTranslatef(3f, -0.5f, -4.9f);
        gl.glColor3f(127 / 255f, 120 / 255f, 108 / 255f);
        gl.glScalef(.2f, 2f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // right window
        gl.glPushMatrix();
        gl.glTranslatef(2.1f, -0.5f, -4.9f);
        gl.glColor3f(209 / 255f, 209 / 255f, 209 / 255f);
        gl.glScalef(.7f, .7f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // window shade
        gl.glPushMatrix();
        gl.glTranslatef(2.3f, -0.5f, -4.85f);
        gl.glColor3f(147 / 255f, 147 / 255f, 147 / 255f);
        gl.glScalef(.35f, .7f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // grey line
        gl.glPushMatrix();
        gl.glTranslatef(4.65f, -0.5f, -4.9f);
        gl.glColor3f(127 / 255f, 120 / 255f, 108 / 255f);
        gl.glScalef(.2f, 2f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // window
        gl.glPushMatrix();
        gl.glTranslatef(3.8f, -0.5f, -4.9f);
        gl.glColor3f(209 / 255f, 209 / 255f, 209 / 255f);
        gl.glScalef(.7f, .7f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(4f, -0.5f, -4.85f);
        gl.glColor3f(147 / 255f, 147 / 255f, 147 / 255f);
        gl.glScalef(.35f, .7f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //roof of right building
        gl.glPushMatrix();
        gl.glTranslatef(3f, .5f, -6.0f);
        gl.glColor3f(194 / 255f, 194 / 255f, 185 / 255f);
        gl.glScalef(3.5f, .2f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // more roof
        gl.glPushMatrix();
        gl.glTranslatef(3f, .6f, -6.0f);
        gl.glColor3f(194 / 255f, 194 / 255f, 185 / 255f);
        gl.glScalef(3.8f, .1f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //left building
        gl.glPushMatrix();
        gl.glTranslatef(-3f, -0.5f, -6.0f);
        gl.glColor3f(124 / 255f, 87 / 255f, 70 / 255f);
        gl.glScalef(3.5f, 2f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        //gray lines
        gl.glPushMatrix();
        gl.glTranslatef(-3f, -0.5f, -4.9f);
        gl.glColor3f(127 / 255f, 120 / 255f, 108 / 255f);
        gl.glScalef(.2f, 2f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        //window
        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, -0.5f, -4.9f);
        gl.glColor3f(209 / 255f, 209 / 255f, 209 / 255f);
        gl.glScalef(.7f, .7f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        //window shade
        gl.glPushMatrix();
        gl.glTranslatef(-2.3f, -0.5f, -4.85f);
        gl.glColor3f(147 / 255f, 147 / 255f, 147 / 255f);
        gl.glScalef(.35f, .7f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        //gray lines
        gl.glPushMatrix();
        gl.glTranslatef(-4.65f, -0.5f, -4.9f);
        gl.glColor3f(127 / 255f, 120 / 255f, 108 / 255f);
        gl.glScalef(.2f, 2f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        //window
        gl.glPushMatrix();
        gl.glTranslatef(-3.8f, -0.5f, -4.9f);
        gl.glColor3f(209 / 255f, 209 / 255f, 209 / 255f);
        gl.glScalef(.7f, .7f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-4f, -0.5f, -4.85f);
        gl.glColor3f(147 / 255f, 147 / 255f, 147 / 255f);
        gl.glScalef(.35f, .7f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        //top of left
        gl.glPushMatrix();
        gl.glTranslatef(-3f, .5f, -6.0f);
        gl.glColor3f(194 / 255f, 194 / 255f, 185 / 255f);
        gl.glScalef(3.5f, .2f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-3f, .6f, -6.0f);
        gl.glColor3f(194 / 255f, 194 / 255f, 185 / 255f);
        gl.glScalef(3.8f, .1f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //----------------middle---------------
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0f, -6.0f);
        gl.glColor3f(124 / 255f, 87 / 255f, 70 / 255f);
        gl.glScalef(2.5f, 3f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        //gray lines
        gl.glPushMatrix();
        gl.glTranslatef(1.15f, 0f, -4.9f);
        gl.glColor3f(127 / 255f, 120 / 255f, 108 / 255f);
        gl.glScalef(.2f, 3f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-1.15f, 0f, -4.9f);
        gl.glColor3f(127 / 255f, 120 / 255f, 108 / 255f);
        gl.glScalef(.2f, 3f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //door 
        gl.glPushMatrix();
        gl.glTranslatef(0f, -1f, -4.9f);
        gl.glColor3f(127 / 255f, 120 / 255f, 108 / 255f);
        gl.glScalef(1f, 1f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //watch
        gl.glPushMatrix();
        gl.glTranslatef(0f, .5f, -4.9f);
        gl.glColor3f(194 / 255f, 194 / 255f, 185 / 255f);
        gl.glScalef(1f, 1f, 0.01f);
        glut.glutSolidCylinder(.6f, 3, 50, 15);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, .7f, -4.8f);
        gl.glColor3f(89 / 255f, 84 / 255f, 77 / 255f);
        gl.glScalef(.06f, .5f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(.2f, .5f, -4.8f);
        gl.glColor3f(89 / 255f, 84 / 255f, 77 / 255f);
        gl.glScalef(.5f, .06f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -1f, -4.8f);
        gl.glColor3f(89 / 255f, 84 / 255f, 77 / 255f);
        gl.glScalef(.05f, 1f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -.5f, -4.9f);
        gl.glColor3f(145 / 255f, 137 / 255f, 125 / 255f);
        gl.glScalef(1f, .15f, 0.1f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -1.5f, -4.8f);
        gl.glColor3f(145 / 255f, 137 / 255f, 125 / 255f);
        gl.glScalef(1f, .15f, 0.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -1.4f, -4.9f);
        gl.glColor3f(145 / 255f, 137 / 255f, 125 / 255f);
        gl.glScalef(1f, .15f, 0.3f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        //top of middle
        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.5f, -6f);
        gl.glColor3f(194 / 255f, 194 / 255f, 185 / 255f);
        gl.glScalef(2.5f, .2f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.6f, -5.9f);
        gl.glColor3f(194 / 255f, 194 / 255f, 185 / 255f);
        gl.glScalef(2.8f, .1f, 2f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();// big pop
    }

    // Smaller Neighberhood components: ----------------------------------------
    public static void fan(GL gl, GLUT glut) {
        // turbine:
        gl.glColor3f(174 / 255f, 217 / 255f, 251 / 255f); // rock grey

        gl.glPushMatrix();
        gl.glRotatef(40, 0, 1, 0);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(0.25, 10, 10, 10);
        gl.glPopMatrix();

        // turbine head:
        gl.glTranslatef(0f, 0f, 0.2f);
        gl.glScalef(2f, 2f, 2f);

        gl.glRotatef(counter, 0.0f, 0.0f, 1.0f);
        counter += 3;
        gl.glBegin(GL.GL_POLYGON);
        gl.glVertex2f(0, 0);
        gl.glVertex2f(0, 0.5f);
        gl.glVertex2f(-0.5f, 0.5f);
        gl.glVertex2f(0.0f, 0.0f);
        gl.glVertex2f(-0.5f, 0.0f);
        gl.glVertex2f(-0.5f, -0.5f);
        gl.glVertex2f(0.0f, 0.0f);
        gl.glVertex2f(0, -0.5f);
        gl.glVertex2f(0.5f, -0.5f);
        gl.glVertex2f(0.0f, 0.0f);
        gl.glVertex2f(0.5f, 0);
        gl.glVertex2f(0.5f, 0.5f);
        gl.glEnd();
    }

    public static void streets(GL gl, GLUT glut) {

        // Side of the hospital street:
        gl.glPushMatrix();
        gl.glColor3f(239 / 255f, 156 / 255f, 13 / 255f);

        // first street from left
        gl.glPushMatrix();
        gl.glTranslatef(8f, 0, 2f);
        gl.glScalef(0.1f, 0.1f, 10f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // parallel
        gl.glPushMatrix();
        gl.glTranslatef(9.9f, 0, 10f);
        gl.glScalef(0.1f, 0.1f, 15f);
        glut.glutSolidCube(1);
        gl.glTranslatef(-20f, 0, 0.4f);
        gl.glScalef(1f, 1f, 0.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // corner
//        gl.glPushMatrix();
//        gl.glRotated(-90, 0, 1, 0);
//        gl.glTranslatef(5f, 0, -2.7f);
//        gl.glScalef(0.1f, 0.1f, 10f);
//        glut.glutSolidCube(1);
//        gl.glPopMatrix();
        // second parallel street
//        gl.glPushMatrix();
//        gl.glTranslatef(2.2f, 0, 0f);
//        gl.glScalef(0.1f, 0.1f, 10f);
//        glut.glutSolidCube(1);
//        gl.glPopMatrix();
//        
        gl.glPopMatrix();

    }

    public static void tree(GL gl, GLUT glut) {

        gl.glPushMatrix();

        // Tree trunk
        gl.glPushMatrix();
        gl.glColor3f(166 / 255f, 100 / 255f, 100 / 255f);
        gl.glScalef(0.5f, 1f, 0.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Tree top
        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.5f, 0f);
        gl.glColor3f(58 / 255f, 150 / 255f, 99 / 255f); //tree green
        gl.glScalef(0.5f, 0.8f, 0.5f);
        glut.glutSolidDodecahedron();
        gl.glPopMatrix();

        gl.glPopMatrix();

    }

    public static void tree2(GL gl, GLUT glut) {

        gl.glPushMatrix();

        // Tree trunk
        gl.glPushMatrix();
        gl.glColor3f(166 / 255f, 100 / 255f, 100 / 255f);
        gl.glScalef(0.5f, 1f, 0.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Tree top
        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.6f, 0f);
        gl.glColor3f(58 / 255f, 150 / 255f, 99 / 255f); //tree green
        gl.glScalef(1f, 2f, 1f);
        glut.glutSolidOctahedron();
        gl.glPopMatrix();

        gl.glPopMatrix();

    }

    public static void fatTree(GL gl, GLUT glut) {

        gl.glPushMatrix();

        // Tree trunk
        gl.glPushMatrix();
        gl.glColor3f(166 / 255f, 100 / 255f, 100 / 255f);
        gl.glScalef(0.5f, 1f, 0.5f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // Tree top
        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.5f, 0f);
        gl.glColor3f(58 / 255f, 150 / 255f, 99 / 255f);
        gl.glScalef(2f, 2f, 2f);
        glut.glutSolidCube(1);
        gl.glTranslatef(0f, -1.5f, 0f);
        gl.glPopMatrix();

        gl.glPopMatrix();

    }

    public static void pointyTree(GL gl, GLUT glut) {

        gl.glPushMatrix();

        // Tree base
        gl.glPushMatrix();
        gl.glTranslatef(0, -0.5f, 0);
        gl.glColor3f(166 / 255f, 100 / 255f, 100 / 255f);
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(1, 0.1, 20, 20);

        gl.glPopMatrix();

        // Tree top
        gl.glPushMatrix();
        gl.glColor3f(58 / 255f, 150 / 255f, 99 / 255f);
        gl.glTranslatef(0, -0.5f, 0);
        gl.glScalef(0.8f, 1f, 0.8f);
        gl.glRotatef(-90, 1, 0, 0);
        glut.glutSolidOctahedron();
        gl.glPopMatrix();

        gl.glPopMatrix();

    }

    public static void pointyTree2(GL gl, GLUT glut) {

        gl.glPushMatrix();

        // Tree base
        gl.glPushMatrix();
        gl.glTranslatef(0, -0.5f, 0);
        gl.glColor3f(166 / 255f, 100 / 255f, 100 / 255f);
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(1, 0.1, 20, 20);

        gl.glPopMatrix();

        // Tree top
        gl.glPushMatrix();
        gl.glColor3f(58 / 255f, 150 / 255f, 99 / 255f);
        gl.glTranslatef(0, -0.5f, 0);
        gl.glScalef(0.8f, 1f, 0.8f);
        gl.glRotatef(-90, 1, 0, 0);
        glut.glutSolidIcosahedron();
        //glut.glutSolidDodecahedron();
        //glut.glutSolidSphere(0.8, 10, 10);
        gl.glPopMatrix();

        gl.glPopMatrix();

    }

    public static void rock(GL gl, GLUT glut) {

        gl.glPushMatrix();
        gl.glColor3f(174 / 255f, 217 / 255f, 251 / 255f); // rock grey

        // first layer
        gl.glScalef(2, 0.5f, 2);
        glut.glutSolidCube(0.5f);
        // second layer
        gl.glTranslatef(0, 0.25f, 0);
        glut.glutSolidCube(0.3f);
        // third layer
        gl.glTranslatef(0, 0.25f, 0);
        glut.glutSolidCube(0.2f);

        gl.glPopMatrix();

    }

    public static void windMill(GL gl, GLUT glut) {
        gl.glPushMatrix();
        // base
        gl.glPushMatrix();
        gl.glRotatef(95, 1, 0, 0);
        gl.glColor3f(1, 1, 1);
        glut.glutSolidCylinder(0.2, 9, 10, 10);
        gl.glPopMatrix();

        // Windmill
        gl.glPopMatrix();
    }

    public static void bench(GL gl, GLUT glut) {

        gl.glPushMatrix();

        // Metal 
        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 0.33f, -14.71f);
        gl.glColor3f(163 / 255f, 163 / 255f, 163 / 255f);
        gl.glScalef(0.1f, 0.7f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 0.33f, -14.71f);
        gl.glColor3f(163 / 255f, 163 / 255f, 163 / 255f);
        gl.glScalef(0.1f, 0.7f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // ---------------------------------------------------------------------
        // Metal
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 0f, -14.5f);
        gl.glColor3f(163 / 255f, 163 / 255f, 163 / 255f);
        gl.glScalef(0.1f, 0.05f, 0.56f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 0f, -14.5f);
        gl.glColor3f(163 / 255f, 163 / 255f, 163 / 255f);
        gl.glScalef(0.1f, 0.05f, 0.56f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // ---------------------------------------------------------------------
        // Metal
        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, -0.35f, -14.58f);
        gl.glColor3f(163 / 255f, 163 / 255f, 163 / 255f);
        gl.glScalef(0.13f, 0.01f, 0.25f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.5f, -0.35f, -14.58f);
        gl.glColor3f(163 / 255f, 163 / 255f, 163 / 255f);
        gl.glScalef(0.13f, 0.01f, 0.25f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, -0.2f, -14.58f);
        gl.glColor3f(163 / 255f, 163 / 255f, 163 / 255f);
        gl.glScalef(0.09f, 0.3f, 0.09f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.5f, -0.2f, -14.58f);
        gl.glColor3f(163 / 255f, 163 / 255f, 163 / 255f);
        gl.glScalef(0.09f, 0.3f, 0.09f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // ---------------------------------------------------------------------
        // Wood
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -14.65f);
        gl.glColor3f(167 / 255f, 109 / 255f, 89 / 255f);
        gl.glScalef(1.7f, 0.05f, 0.25f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -14.35f);
        gl.glColor3f(167 / 255f, 109 / 255f, 89 / 255f);
        gl.glScalef(1.7f, 0.05f, 0.25f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // ---------------------------------------------------------------------
        // Wood
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.25f, -14.72f);
        gl.glColor3f(167 / 255f, 109 / 255f, 89 / 255f);
        gl.glScalef(1.7f, 0.25f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.55f, -14.72f);
        gl.glColor3f(167 / 255f, 109 / 255f, 89 / 255f);
        gl.glScalef(1.7f, 0.25f, 0.05f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // ---------------------------------------------------------------------
        gl.glPopMatrix();

    }

    public static void lamp(GL gl, GLUT glut) {
        gl.glPushMatrix();

        // first blue cylinder
        gl.glPushMatrix();
        gl.glRotatef(90, 1, 0, 0);
        gl.glColor3f(67 / 255f, 116 / 255f, 130 / 255f); //dark blue
        glut.glutSolidCylinder(0.2, 4, 5, 20);
        gl.glPopMatrix();

        // second white cylinder
        gl.glTranslatef(0, 1, 0);
        gl.glPushMatrix();
        gl.glRotatef(90, 1, 0, 0);
        gl.glColor3f(1, 1, 1); //white
        glut.glutSolidCylinder(0.1, 1, 5, 20);
        gl.glPopMatrix();

        // lamp
        gl.glTranslatef(0, 0, 0);
        gl.glColor3f(224 / 255f, 198 / 255f, 101 / 255f); //yellow
        glut.glutSolidSphere(0.5, 10, 10);

        gl.glPopMatrix();
    }

    public void solarLandscape(GLUT glut, GL gl) {

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, 0f);
        gl.glRotatef(90, 0, 1, 0);
        //gl.glScalef(0.2f, 0.2f, 0.2f);
        //----------------------- start all

        gl.glPushMatrix();
        //gl.glTranslatef(0f, -0.5f, 0f);
        //gl.glRotatef(-40, 0, 1, 1);
        //gl.glScalef(0.2f, 1f, 1.2f);
        plate(glut, gl);// end 1 ----------------
        gl.glPopMatrix();

        //pipe
        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.5f, 0f);
        gl.glRotatef(90, 1, 0, 0);
        gl.glScalef(0.2f, 1f, 1.2f);
        gl.glColor3f(182 / 255f, 182 / 255f, 182 / 255f);

        glut.glutSolidCylinder(0.06, 0.25, 50, 50);//thing

        gl.glPopMatrix();//----end 2----------------

        //base
        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.81f, 0f);
        gl.glRotatef(90, 1, 0, 0);
        gl.glScalef(0.2f, 1f, 1.2f);
        gl.glColor3f(167 / 255f, 167 / 255f, 167 / 255f);

        glut.glutSolidCylinder(0.15, 0.1, 50, 50);//thing

        gl.glPopMatrix();//----end 3----------------

        //-------------------------------------------------------
        gl.glPopMatrix();//---------------------end all

    }

    public void solarPortrait(GLUT glut, GL gl) {

        gl.glPushMatrix();
        gl.glTranslatef(2f, 0f, 0f);
        gl.glRotatef(-90, 0, 1, 0);
        //gl.glScalef(0.2f, 0.2f, 0.2f);
        //----------------------- start all

        //wide long cube
        gl.glPushMatrix();
        gl.glTranslatef(-.1f, -0.3f, 0.5f);
        gl.glScalef(0.1f, 0.1f, 1.3f);
        gl.glColor3f(182 / 255f, 182 / 255f, 182 / 255f);
        glut.glutSolidCube(1f);//thing
        gl.glPopMatrix();//----end 1----------------

        //2nd wide long cube
        gl.glPushMatrix();
        gl.glTranslatef(-.1f, -0.9f, 0.5f);
        gl.glScalef(0.1f, 0.1f, 1.3f);
        gl.glColor3f(182 / 255f, 182 / 255f, 182 / 255f);
        glut.glutSolidCube(1f);//thing
        gl.glPopMatrix();//----end 1----------------

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.5f, 0f);
        gl.glRotatef(90, 1, 0, 0);
        gl.glScalef(0.03f, 0.5f, 1f);
        plate(glut, gl);// end 1 ----------------
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.5f, 0.6f);
        gl.glRotatef(90, 1, 0, 0);
        gl.glScalef(0.03f, 0.5f, 1f);
        plate(glut, gl);// end 1 ----------------
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.5f, 1.2f);
        gl.glRotatef(90, 1, 0, 0);
        gl.glScalef(0.03f, 0.5f, 1f);
        plate(glut, gl);// end 1 ----------------
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.6f, 0.6f);

        //pipe
        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.5f, 0f);
        gl.glRotatef(90, 1, 0, 0);
        gl.glScalef(0.2f, 1f, 1.2f);
        gl.glColor3f(182 / 255f, 182 / 255f, 182 / 255f);
        glut.glutSolidCylinder(0.06, 0.2, 50, 50);//thing
        gl.glPopMatrix();//----end 2----------------

        //base
        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.71f, 0f);
        gl.glRotatef(90, 1, 0, 0);
        gl.glScalef(0.2f, 1f, 1.2f);
        gl.glColor3f(167 / 255f, 167 / 255f, 167 / 255f);

        glut.glutSolidCylinder(0.25, 0.1, 50, 50);//thing

        gl.glPopMatrix();//----end 3----------------
        gl.glPopMatrix();

        //-------------------------------------------------------
        gl.glPopMatrix();//---------------------end all

    }

    public static void grid(GLUT glut, GL gl) {

        //cylinder
        gl.glPushMatrix();
        gl.glTranslatef(0.1f, 0f, 0.1f);
        gl.glRotatef(-90, 0, 1, 0);
        gl.glScalef(1f, 1f, 1f);
        gl.glColor3f(182 / 255f, 182 / 255f, 182 / 255f);
        //----------------------- start grid

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.5f, 0);
        gl.glRotatef(90, 1, 0, 0);
        //gl.glScalef(1, 1.7f, 1);

        glut.glutWireCylinder(0.1, 1, 2, 8);//thing

        gl.glPopMatrix();//----end 1

        gl.glPushMatrix();
        gl.glTranslatef(0.2f, 0.5f, 0);
        gl.glRotatef(90, 1, 0, 0);
        //gl.glScalef(1, 1.7f, 1);

        glut.glutWireCylinder(0.1, 1, 2, 8);//thing

        gl.glPopMatrix();//----end 2

        //cylinder
        gl.glPushMatrix();
        gl.glTranslatef(0.4f, 0.5f, 0);
        gl.glRotatef(90, 1, 0, 0);
        //gl.glScalef(1, 1.7f, 1);

        glut.glutWireCylinder(0.1, 1, 2, 8);//thing
        gl.glPopMatrix();//----end 3

        gl.glPushMatrix();
        gl.glTranslatef(-0.2f, 0.5f, 0);
        gl.glRotatef(90, 1, 0, 0);
        //gl.glScalef(1, 1.7f, 1);

        glut.glutWireCylinder(0.1, 1, 2, 8);//thing
        gl.glPopMatrix();//----end 4

        gl.glPushMatrix();
        gl.glTranslatef(-0.4f, 0.5f, 0);
        gl.glRotatef(90, 1, 0, 0);
        //gl.glScalef(1, 1.7f, 1);

        glut.glutWireCylinder(0.1, 1, 2, 8);//thing

        gl.glPopMatrix();//----end 5

        //cylinder
        gl.glPushMatrix();
        gl.glTranslatef(-0.6f, 0.5f, 0);
        gl.glRotatef(90, 1, 0, 0);
        //gl.glScalef(1, 1.7f, 1);

        glut.glutWireCylinder(0.1, 1, 2, 8);//thing

        gl.glPopMatrix();//----end 6

        gl.glPopMatrix();//----end Grid

    }

    public static void plate(GLUT glut, GL gl) {

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, 0f);
        //gl.glRotatef(90, 0, 1, 0);
        //gl.glScalef(0.2f, 0.2f, 0.2f);
        //----------------------- start all

        //cube
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, 0f);
        //gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(0.1f, 1f, 1.2f);
        gl.glColor3f(182 / 255f, 182 / 255f, 182 / 255f);

        glut.glutSolidCube(1f);//thing

        gl.glPopMatrix();//----end 1----------------

        //cube
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, 0f);
        //gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(0.2f, 1f, 1.2f);
        gl.glColor3f(0f, 0f, 125f);

        glut.glutSolidCube(0.9f);//thing

        gl.glPopMatrix();//----end 1----------------

        gl.glTranslatef(0f, 0f, 0f);
        //gl.glRotatef(-90, 0, 1, 0);
        //gl.glScalef(1f, 1f, 1.04f);
        grid(glut, gl);//grid end 2 ----------------

        gl.glPopMatrix();//----------------end

    }

    public void cloud1(GLUT glut, GL gl){
        
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
    
    public void cloud2(GLUT glut, GL gl){
        
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

    public void new_bin(GL gl, GLUT glut, float r, float g, float b) {

        gl.glPushMatrix();
        gl.glColor3f(r, g, b);
        float[][] back = {
            {-0.2f, 0.4f, 0},
            {0.2f, 0.4f, 0f},
            {0.2f, -0.4f, 0},
            {-0.2f, -0.4f, 0f}};
        drawQuads(gl, back);

        float[][] left = {
            {-0.2f, 0.4f, 0},
            {-0.2f, 0.2f, 0.4f},
            {-0.2f, -0.4f, 0.4f},
            {-0.2f, -0.4f, 0}};
        drawQuads(gl, left);

        float[][] right = {
            {0.2f, 0.4f, 0},
            {0.2f, 0.2f, 0.4f},
            {0.2f, -0.4f, 0.4f},
            {0.2f, -0.4f, 0}};
        drawQuads(gl, right);

        tex.enable();
        gl.glBegin(GL.GL_QUADS);
        tex.bind();
        gl.glColor3f(1, 1, 1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-0.2f, 0.2f, 0.4f);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(0.2f, 0.2f, 0.4f);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(0.2f, -0.4f, 0.4f);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(-0.2f, -0.4f, 0.4f);
        gl.glEnd();
        tex.disable();

        gl.glColor3f(r, g, b);
        float[][] bottom = {
            {-0.2f, -0.4f, 0},
            {0.2f, -0.4f, 0f},
            {0.2f, -0.4f, 0.4f},
            {-0.2f, -0.4f, 0.4f}};
        drawQuads(gl, bottom);

        float[][] top = {
            {-0.15f, 0.4f, 0.f},
            {0.15f, 0.4f, 0.f},
            {0.15f, 0.22f, 0.35f},
            {-0.15f, 0.22f, 0.35f}};
        drawQuads(gl, top);

        gl.glColor3f(0, 0, 0);

        float[][] left_boarder = {
            {-0.2f, 0.4f, 0.f},
            {-0.19f, 0.4f, 0.f},
            {-0.19f, 0.2f, 0.4f},
            {-0.2f, 0.2f, 0.4f}};
        drawQuads(gl, left_boarder);

        float[][] right_boarder = {
            {0.2f, 0.4f, 0.0f},
            {0.19f, 0.4f, 0.0f},
            {0.19f, 0.2f, 0.4f},
            {0.2f, 0.2f, 0.4f}};
        drawQuads(gl, right_boarder);

        float[][] bottom_boarder = {
            {-0.2f, 0.21f, 0.39f},
            {0.2f, 0.21f, 0.39f},
            {0.2f, 0.2f, 0.4f},
            {-0.2f, 0.2f, 0.4f}};
        drawQuads(gl, bottom_boarder);

        gl.glPopMatrix();

    }

    // Functional Methods: -----------------------------------------------------
    /*
    This method set up the lighting scene in the frame.
    */
    private static void setUpLighting(GL gl) {
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

        float[] positionLight = {-5f, 3f, 0f};  // coordinates of the light
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, positionLight, 0);

        float[] specularLight = {1.0f, 1.0f, 1.0f, 1.0f}; // intensety of the shine
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specularLight, 0);

        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPOT_DIRECTION, specularLight, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPOT_EXPONENT, specularLight, 0);
    }

    // Helper methods: ---------------------------------------------------------
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

    public static void fanHead(GL gl, GLUT glut) {

    }

    // Masjed supporting objects:
    public void Qubba(GLUT glut, GL gl) {

        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, 0.5f, 0f);
        //gl.glRotatef(angle, 0, 0, 0);

        //cylinder
        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.2f, -2.0f);
        gl.glRotatef(90, 1, 0, 0);
        //gl.glScalef(1, 1.7f, 1);
        gl.glColor3f(0.643f, 0.647f, 0.662f);

        glut.glutSolidCylinder(0.015, 0.2, 20, 20);//thing
        gl.glPopMatrix();//----end 1
        //sphere
        gl.glPushMatrix();
        gl.glTranslatef(0f, -0.2f, -2f);
        gl.glScalef(1f, 1f, 1f);
        gl.glColor3f(1f, 0.741f, 0.2f);
        //gl.glRotatef(angle, x, y, z);

        glut.glutSolidSphere(1.2f, 20, 20);//thing

        gl.glPopMatrix();//end 1

        //-------------------------------------------------------
        //cylinder
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.0f, -2.0f);
        gl.glRotatef(90, 1, 0, 0);
        //gl.glScalef(1, 1.7f, 1);
        gl.glColor3f(0.643f, 0.647f, 0.662f);

        glut.glutSolidCylinder(1.3, 0.5, 10, 5);//thing

        gl.glPopMatrix();//----end 2

        gl.glPopMatrix();//end all

    }

    public void bulding(GLUT glut, GL gl) {

        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, -1.0f, 0.0f);
        //gl.glRotatef(40, 0, 1, 0);

        //bulding
        gl.glPushMatrix();

        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glTranslatef(0f, 0.0f, -2f);
        //gl.glRotatef(angle, x, y, z);
        gl.glScalef(2.2f, 1.03f, 1.5f);
        gl.glColor3f(241 / 255f, 239 / 255f, 240 / 255f);

        glut.glutSolidCube(2);//thing

        gl.glPopMatrix();//end 1

        //bulding roof1
        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.0f, -2f);
        //gl.glRotatef(angle, x, y, z);
        gl.glScalef(2.2f, 0.1f, 1.5f);
        gl.glColor3f(241 / 255f, 239 / 255f, 240 / 255f);

        glut.glutSolidCube(2.2f);//thing

        gl.glPopMatrix();//end 2

        //bulding roof2
        gl.glPushMatrix();
        gl.glTranslatef(0f, 1.2f, -2f);
        //gl.glRotatef(angle, x, y, z);
        gl.glScalef(2.2f, 0.1f, 1.5f);
        gl.glColor3f(98 / 255f, 195 / 255f, 237 / 255f);

        glut.glutSolidCube(2.0f);//thing

        gl.glPopMatrix();//end 4

        //-------------------------------------------------------
        //bulding column 1 
        gl.glPushMatrix();
        gl.glTranslatef(2.3f, -0.06f, -0.4f);
        //gl.glRotatef(angle, x, y, z);
        gl.glScalef(1.5f, 19f, 1.2f);
        gl.glColor3f(98 / 255f, 195 / 255f, 237 / 255f);

        glut.glutSolidCube(0.1f);//thing
        gl.glPopMatrix();//end column 1 
        //---------------------------------
        //bulding column 2 
        gl.glPushMatrix();
        gl.glTranslatef(-2.3f, -0.06f, -0.4f);
        //gl.glRotatef(angle, x, y, z);
        gl.glScalef(1.5f, 19f, 1.2f);
        gl.glColor3f(98 / 255f, 195 / 255f, 237 / 255f);

        glut.glutSolidCube(0.1f);//thing
        gl.glPopMatrix();//end column 2 
        //---------------------------------

        //bulding column 3 
        gl.glPushMatrix();
        gl.glTranslatef(2.3f, -0.06f, -3.6f);
        //gl.glRotatef(angle, x, y, z);
        gl.glScalef(1.5f, 19f, 1.2f);
        gl.glColor3f(98 / 255f, 195 / 255f, 237 / 255f);

        glut.glutSolidCube(0.1f);//thing
        gl.glPopMatrix();//end column 3 
        //---------------------------------

        //bulding column 4 
        gl.glPushMatrix();
        gl.glTranslatef(-2.3f, -0.06f, -3.6f);
        //gl.glRotatef(angle, x, y, z);
        gl.glScalef(1.5f, 19f, 1.2f);
        gl.glColor3f(98 / 255f, 195 / 255f, 237 / 255f);

        glut.glutSolidCube(0.1f);//thing
        gl.glPopMatrix();//end column 4 
        //---------------------------------

        //bulding column 1 side
        gl.glPushMatrix();
        gl.glTranslatef(3.55f, -0.1f, -1.8f);
        //gl.glRotatef(angle, x, y, z);
        gl.glScalef(1.5f, 19.2f, 1.2f);
        gl.glColor3f(98 / 255f, 195 / 255f, 237 / 255f);

        glut.glutSolidCube(0.1f);//thing
        gl.glPopMatrix();//end column 1 side 
        //---------------------------------
        //bulding column 1 side
        gl.glPushMatrix();
        gl.glTranslatef(3.55f, -0.1f, -3.4f);
        //gl.glRotatef(angle, x, y, z);
        gl.glScalef(1.5f, 19.2f, 1.2f);
        gl.glColor3f(98 / 255f, 195 / 255f, 237 / 255f);

        glut.glutSolidCube(0.1f);//thing
        gl.glPopMatrix();//end column 1 side 
        //----------------------------------

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -3.15f);
        //gl.glRotatef(-60, 0, 1, 1);
        //gl.glScalef(0.3f, 0.3f, 0f);
        // start all windowBigs behind

        //windowBig2
        gl.glPushMatrix();
        gl.glTranslatef(1.3f, 0.35f, -0.4f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.8f, 0.7f, 0f);
        windowBig(glut, gl);
        gl.glPopMatrix();
        //---------------------------

        //windowBig4
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.35f, -0.4f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.8f, 0.7f, 0f);
        windowBig(glut, gl);
        gl.glPopMatrix();
        //---------------------------

        //windowBig6
        gl.glPushMatrix();
        gl.glTranslatef(-1.3f, 0.35f, -0.4f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.8f, 0.7f, 0f);
        windowBig(glut, gl);
        gl.glPopMatrix();
        //---------------------------
        //windowBig2 frame
        gl.glPushMatrix();
        gl.glTranslatef(1.3f, 0.35f, -0.38f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.85f, 0.73f, 0f);
        windowBigFrame(glut, gl);
        gl.glPopMatrix();
        //---------------------------

        //windowBig4 frame
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.35f, -0.38f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.85f, 0.73f, 0f);
        windowBigFrame(glut, gl);
        gl.glPopMatrix();
        //---------------------------

        //windowBig6 frame
        gl.glPushMatrix();
        gl.glTranslatef(-1.3f, 0.35f, -0.38f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.85f, 0.73f, 0f);
        windowBigFrame(glut, gl);
        gl.glPopMatrix();
        //---------------------------

        gl.glPopMatrix();
        // end windowBigs behind -----------------------------------------

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, 0f);
        //gl.glRotatef(-60, 0, 1, 1);
        //gl.glScalef(0.3f, 0.3f, 0f);
        // start all windows infront

        //window1
        gl.glPushMatrix();
        gl.glTranslatef(1.5f, 0.5f, -0.4f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.3f, 0.3f, 0f);
        window(glut, gl);
        gl.glPopMatrix();
        //---------------------------
        //window1 frame
        gl.glPushMatrix();
        gl.glTranslatef(1.5f, 0.5f, -0.42f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.35f, 0.33f, 0f);
        windowframe(glut, gl);
        gl.glPopMatrix();
        //---------------------------

        //window3
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 0.5f, -0.4f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.3f, 0.3f, 0f);
        window(glut, gl);
        gl.glPopMatrix();
        //---------------------------
        //window3 frame
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 0.5f, -0.42f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.35f, 0.33f, 0f);
        windowframe(glut, gl);
        gl.glPopMatrix();
        //---------------------------

        //window5
        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 0.5f, -0.4f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.3f, 0.3f, 0f);
        window(glut, gl);
        gl.glPopMatrix();
        //---------------------------
        //window5 frame
        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 0.5f, -0.42f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.35f, 0.33f, 0f);
        windowframe(glut, gl);
        gl.glPopMatrix();
        //---------------------------

        //window7
        gl.glPushMatrix();
        gl.glTranslatef(-1.5f, 0.5f, -0.4f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.3f, 0.3f, 0f);
        window(glut, gl);
        gl.glPopMatrix();
        //---------------------------
        //window7 frame
        gl.glPushMatrix();
        gl.glTranslatef(-1.5f, 0.5f, -0.42f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.35f, 0.33f, 0f);
        windowframe(glut, gl);
        gl.glPopMatrix();
        //---------------------------
        gl.glPopMatrix();
        // end windows infront -----------------------------------------

        gl.glPushMatrix();
        gl.glTranslatef(-1.85f, -0.2f, -1.7f);
        gl.glRotatef(90, 0, 1, 0);
        //gl.glScalef(0.3f, 0.3f, 0f);
        // ------------------------ start all windows side1

        //window2 frame
        gl.glPushMatrix();
        gl.glTranslatef(1.0f, 0.5f, -0.4f);
//        gl.glColor3f(98/255f, 195/255f, 237/255f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.55f, 0.5f, 0f);
        windowBigFrame(glut, gl);
        gl.glPopMatrix();
        //---------------------------

        //window4 frame
        gl.glPushMatrix();
        gl.glTranslatef(-0.2f, 0.5f, -0.4f);
//        gl.glColor3f(98/255f, 195/255f, 237/255f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.55f, 0.5f, 0f);
        windowBigFrame(glut, gl);
        gl.glPopMatrix();
        //---------------------------

        //window2
        gl.glPushMatrix();
        gl.glTranslatef(1.0f, 0.5f, -0.42f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.5f, 0.45f, 0f);
        windowBig(glut, gl);
        gl.glPopMatrix();
        //---------------------------

        //window4
        gl.glPushMatrix();
        gl.glTranslatef(-0.2f, 0.5f, -0.42f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.5f, 0.45f, 0f);
        gl.glColor3f(98 / 255f, 195 / 255f, 237 / 255f);
        windowBig(glut, gl);
        gl.glColor3f(98 / 255f, 195 / 255f, 237 / 255f);
        gl.glPopMatrix();
        //---------------------------

        gl.glPopMatrix();
        // end windows side1 -----------------------------------------

        //entrance1
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -0.5f, -0.4f);
        //gl.glRotatef(angle, x, y, z);
        gl.glScalef(1.3f, 1f, 0.3f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        gl.glColor3f(0.643f, 0.647f, 0.662f);

        glut.glutSolidCube(1);//thing

        gl.glPopMatrix();//----end 2

        //entrance2
        gl.glPushMatrix();
        gl.glTranslatef(2.8f, -0.08f, -2.6f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(1.0f, 1.34f, 1f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        gl.glColor3f(241 / 255f, 239 / 255f, 240 / 255f);
        glut.glutSolidCube(1.45f);//thing

        gl.glPopMatrix();//----end 2

        gl.glColor3f(241 / 255f, 239 / 255f, 240 / 255f);

        //entrance  2 roof
        gl.glPushMatrix();
        gl.glTranslatef(2.8f, 0.95f, -2.6f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(1.0f, 0.11f, 1f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        gl.glColor3f(241 / 255f, 239 / 255f, 240 / 255f);
        glut.glutSolidCube(1.7f);//thing

        gl.glPopMatrix();//----end 2

        //door entrance
        gl.glPushMatrix();
        gl.glTranslatef(3.55f, 0.28f, -2.55f);
        gl.glRotatef(90, 0, 1, 0);
        gl.glScalef(1f, 0.75f, 1f);
        door(glut, gl);
        gl.glPopMatrix();
        //---------------------------

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

    public void Methana(GLUT glut, GL gl) {

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
        gl.glScalef(1, 1.7f, 1);
        gl.glColor3f(0.643f, 0.647f, 0.662f);

        glut.glutSolidCylinder(2.3, 1.5, 20, 10);//thing

        gl.glPopMatrix();//----end 4

        //cylinder
        gl.glPushMatrix();
        gl.glTranslatef(0f, -7.5f, -2.0f);
        gl.glRotatef(90, 1, 0, 0);
        //gl.glScalef(1, 1.7f, 1);
        gl.glColor3f(0.643f, 0.647f, 0.662f);

        glut.glutSolidCylinder(1.7, 13, 10, 5);//thing

        gl.glPopMatrix();//----end 5

        gl.glPopMatrix();//end cylinder

        gl.glPopMatrix();//end all

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

    public void windowframe(GLUT glut, GL gl) {

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
        gl.glColor3f(98 / 255f, 195 / 255f, 237 / 255f);
        glut.glutSolidSphere(0.6, 20, 10);//object
        gl.glPopMatrix();

        //-------------------------------------
        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, -0.8f, 0f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(1f, 1.1f, 0f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        //gl.glColor3f(0.643f, 0.647f, 0.662f);
        gl.glColor3f(98 / 255f, 195 / 255f, 237 / 255f);
        glut.glutSolidCube(1.2f);//object
        gl.glPopMatrix();

        //---------------------------------------
        //------------------------------------------------------
        gl.glPopMatrix();
    }

    public void windowBig(GLUT glut, GL gl) {

        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, 0f, 0f);
        //gl.glRotatef(-120, 0, 1, 0);
        //gl.glScalef(0.2f, 0.2f, 0.2f);
        //----------------------------------------------------------

        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, 0f, 0f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(1f, 1f, 0.1f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        //gl.glColor3f(0.643f, 0.647f, 0.662f);

        gl.glColor3f(2 / 255f, 56 / 255f, 80 / 255f);
        glut.glutSolidSphere(0.6, 20, 10);//object
        gl.glPopMatrix();

        //-------------------------------------
        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, -0.9f, 0f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(1f, 1.2f, 0.05f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        //gl.glColor3f(0.643f, 0.647f, 0.662f);
        gl.glColor3f(2 / 255f, 56 / 255f, 80 / 255f);
        glut.glutSolidCube(1.2f);//object
        gl.glPopMatrix();

        //---------------------------------------
        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, 0.2f, 0f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.5f, 0.5f, 0.07f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        //gl.glColor3f(0.643f, 0.647f, 0.662f);
        gl.glColor3f(2 / 255f, 56 / 255f, 80 / 255f);
        glut.glutSolidOctahedron();//object
        gl.glPopMatrix();

        //---------------------------------------
        //------------------------------------------------------
        gl.glPopMatrix();
    }

    public void windowBigFrame(GLUT glut, GL gl) {

        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, 0f, 0f);
        //gl.glRotatef(-120, 0, 1, 0);
        //gl.glScalef(0.2f, 0.2f, 0.2f);
        //----------------------------------------------------------

        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, 0f, 0f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(1f, 1f, 0.1f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        //gl.glColor3f(0.643f, 0.647f, 0.662f);

        gl.glColor3f(98 / 255f, 195 / 255f, 237 / 255f);
        glut.glutSolidSphere(0.6, 20, 10);//object
        gl.glPopMatrix();

        //-------------------------------------
        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, -0.9f, 0f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(1f, 1.2f, 0.05f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        //gl.glColor3f(0.643f, 0.647f, 0.662f);
        gl.glColor3f(98 / 255f, 195 / 255f, 237 / 255f);
        glut.glutSolidCube(1.2f);//object
        gl.glPopMatrix();

        //---------------------------------------
        gl.glPushMatrix();//start all
        gl.glTranslatef(0f, 0.2f, 0f);
        //gl.glRotatef(-60, 0, 1, 1);
        gl.glScalef(0.5f, 0.5f, 0.07f);
        //gl.glColor3f(0.901f, 0.901f, 0.909f);
        //gl.glColor3f(0.643f, 0.647f, 0.662f);
        gl.glColor3f(98 / 255f, 195 / 255f, 237 / 255f);
        glut.glutSolidOctahedron();//object
        gl.glPopMatrix();

        //---------------------------------------
        //------------------------------------------------------
        gl.glPopMatrix();
    }

    // Additional keyboard functions 
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
