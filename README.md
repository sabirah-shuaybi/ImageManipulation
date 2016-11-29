# ImageManipulation

This program consists of three classes: __ImageManipulator__, __Button__ and __Picture__ (Picture class already supplied)

__ImageManipulator Class:__

The ImageManipulator class loads, displays a picture and constructs buttons from button class
that have contain methods attached to photoshopping methods like gray scale, mirror, blur and
(revert back to) original.

This class defines an instance variable Picture picture that holds the value of the mutated picture
throughout the program and thus allows the user to perform multiple effects on the picture.
For example, a user can click the button "Mirror" at which point the picture will become mirrored.
Then, directly afterwards, the user can press the button "Gray" and the mirrored picture will then become
grayed. Thus, the class allows for a layering of effects and similarly, blurrings of blurred picture.

__Button Class:__

The Button class is responsible for constructing a button object, dictating where the buttons will
be displayed on the canvas, perfectly centering the text within buttons, and finally defining a
contains method for the button object.

 
 __@author Sabirah Shuaybi__
 
 __@version 10/04/16__
