#  OpenGL-Neighborhood
This project illustrates an example of an old residential area and an enhanced residential area of Saudi Arabia using the OpenGL Java library. 
It uses most of the 3D and 2D primitive shapes of OpenGL like cubes to construct buildings of different designs, clouds, benches, solar panels and bins, cylinders to build satellites and water tanks, textures to simulate shopfronts and the drawings on walls, a sphere for the masjid dome, quads for cars and icosahedrons for plants and rocks, more detailed following.

#### Open Graphics Library (OpenGl) 
OpenGL is a cross-language, cross-platform application programming interface (API) that is used for rendering 2D and 3D vector graphics. This project uses  **Java OpenGL**, which is a wrapper library allows OpenGL to be used in the Java programming language.

#### Project Demo
...
##  Implementation 
The project follows an approach of encapsulating. Therefore, every object is written in a separate function for drawing it in order to simplify the integration of objects in every scene, control its position, and make sure it doesn’t overlap, specially that we deal with small little details that need to be allocated to precise places. The encapsulated functions looks like this:
```java
public static void market(GL gl, GLUT glut){...}
public static void clinic(GL gl, GLUT glut){...}
public static void park(GL gl, GLUT glut){...}
```

This is how objects in display() [main function] look like:
```java
// Market 
gl.glPushMatrix();
gl.glTranslatef(3f, 0f, 5f);
gl.glScalef(0.6f, 0.6f, 0.6f);
market(gl, glut);
gl.glPopMatrix();
```
### Objects 
| Object | Description |
|--|--|
|Houses| Solid cubes were used for the building, as far for the windows and doors the value of z is scaled to look like 2D quads. ![Modern-House](https://github.com/ANFALATAWI/OpenGL-Neighborhood/blob/master/Repo-materials/Gifs/Modern%20House.gif) |
|Market|Solid cubes were used and different 2d planes to construct the market.|
|Masjed|There are three main parts in the masjed, quba, the main building and the minaret.The main building was constructed by scaling cubes. The quba was built using a sphere and a cylinder. last part was the minaret the most complex part which used solid torus, cubes, sphere, solid and wired cylinders.|
|Cars|Quads are used to model the car, Triangles to model the side windows and solid torus with line strips to model the tires.|
|Clinics|Solid cubes were colored and scaled.|
|Parks|2D quad for the grass of the park was used. For the trees, a scaled cube (along y) for the trunk and a scaled dodecahedron for the top.|
|Satellites|Scaled solid was used cylinder to represent the “cycle”, also solid cube to represent column in the back and the one inside the cycle.|
|Water tanks|White cylinders only|
|Trash bins|For the old trash bin, solid cube was used and resized with glScalef(1,1,0.05f) and solid torus for the wheels. While for the new bin, it was modeled using Quads and texture.|
|Shopfronts|Textures on quads were used to simulate graphic shopfronts.|
|Drawings on walls|eTextures on quads were used to simulate abstract drawings.|
|Wind turbines|A scaled cylinder was used for the base and triangles for the turning top.|
|Solar panels|The solar consists of three parts, A plate, pipe and base. The pipe and base both was built using Solid Cylinder. The plate on the other hand consisted of the main plate that was built by scaling and rotating Cubes, and the grid witch used wired cylinder.|
|Court|Different 2D planes & cylinders were used to model the court and baskets.|
|Court|combination of scaled solid cylinder and solid cube to represent the playground and scaled solid cube for the basket .|

##  Requirements
- [NetBeans 8.1](https://netbeans.org/downloads/8.1/)
- [OpenGL 6.7](http://plugins.netbeans.org/plugin/3260/netbeans-opengl-pack) 
### Set up OpenGL Library
1. Launch NetBeans
2. Go to Tool > Plugins
3. Go to the Downloaded tab, then click on Add Plugins
4. Select all the plugins you downloaded from OpenGL site, then click Open
5. Check all the plugins except the **GLSL editor** module (It doesn't work in NetBeans 7.1 and above, so you will get errors when you install it)
6. Click on Install button

### Run
To run a project, simply open it in NetBeans (File > Open project), then choose Run > Run project (shortcut F6).

##  Contributers
-  [Anfal Alawati](https://github.com/ANFALATAWI)
- [Razan Sonbul](https://github.com/RazanSon)
- Nouf Alkedewi
- [Najwa Noorwali](https://github.com/najwaWali)
- Rahaf Almotery
- Bayader Alsahafi 
