# Project Documentation Overview

This project is a drawing application where users can interact with a canvas using different tools such as lines, circles, polygons, and erasers. The structure of the code is organized into multiple packages that handle various aspects of the program.

## Key Components

1. **Adapters:**
    - **Mouse.java**: This class is a singleton that listens to mouse events on the drawing panel. It performs actions based on the current selected action (e.g., drawing lines, creating polygons, filling areas, etc.).

2. **Controllers:**
    - These classes (e.g., `LineController`, `CircleController`, `PolygonController`, etc.) manage the logic for creating, editing, and modifying various shapes on the canvas. They interact with the models to create new shapes or update existing ones.
    - **CircleController.java**: Controls the creation, modification, and properties of circles.
    - **LineController.java**: Manages the creation and manipulation of lines.
    - **PolygonController.java**: Responsible for handling polygons.
    - **EraserController.java**: Handles erasing actions on the canvas.
    - **HandController.java**: Manages object movement by tracking the "hand" tool's movement on the canvas.
    - **EditController.java**: Manages editing of existing objects on the canvas.

3. **Enums:**
    - **ActionType.java**: Enum that defines various drawing actions like Line, Polygon, Fill, Rectangle, Circle, Eraser, Hand, and Edit.
    - **DrawColor.java**: Defines available drawing colors.
    - **LineType.java**: Specifies different line styles (e.g., solid, dashed, dotted).
    - **Alignment.java**: Defines alignment options, primarily for lines and rectangles.

4. **Fillers:**
    - **BasicFiller.java**: Implements the flood fill algorithm to fill areas on the canvas. It uses a stack-based approach to propagate the filling action.

5. **Models:**
    - **Canvases (CircleCanvas, LineCanvas, PolygonCanvas, etc.)**: These classes manage collections of the respective shapes (circles, lines, polygons) on the canvas.
    - **Shapes (Circle, Line, Polygon, Rectangle)**: Represent the drawable objects on the canvas, each with properties like position, size, color, and thickness.
    - **Point.java**: Represents a 2D point in space with x and y coordinates.

6. **Rasterizers:**
    - These classes are responsible for rendering shapes on the canvas. They handle how different types of lines (dashed, dotted, or solid) are drawn.
    - **RasterBuffer.java**: Stores the state of the canvas, keeping track of which objects are on which pixels.
    - **Raster.java**: Defines an interface for canvas rendering, allowing pixel manipulation and clearing.

7. **Stores:**
    - **EnumStore.java**: A singleton that holds the current settings for the application, like the active color, line type, and the current drawing tool.
    - **StateStore.java**: Holds application state like the current thickness of drawn objects.

8. **Utilities:**
    - **Frame.java**: The main window for the application, including the drawing panel and GUI.
    - **HelperFunctions.java**: Provides utility methods used in more classes like calculating distances between points or altering points.
    - **Renderer.java**: Responsible for rendering the entire canvas after any updates are made.
    - **GUI.java**: Contains user interface components and interactions.

9. **App.java**: The entry point of the application, responsible for setting up the environment and initializing the canvas and user interface.

# Overview of Functionality

The application provides a graphical interface where users can:
- **Draw**: Using various tools like lines, circles, rectangles, and polygons.
- **Edit**: Modify existing shapes.
- **Erase**: Remove shapes from the canvas.
- **Fill**: Use a flood-fill algorithm to color areas on the canvas.

## Example Workflow
1. **Mouse Interaction**:
    - The `Mouse` class listens to mouse events (click, drag, release) and triggers corresponding actions (drawing, erasing, moving objects).
    - Based on the action selected in the `EnumStore` (e.g., Line, Circle, Eraser), the appropriate controller (e.g., `LineController`, `CircleController`) is invoked to handle the action.

2. **Drawing Objects**:
    - Each drawing tool (line, circle, polygon, etc.) is managed by its respective controller (e.g., `CircleController`).
    - These controllers create or update shapes on the canvas (stored in the respective canvas classes like `CircleCanvas` or `LineCanvas`).

3. **Rendering**:
    - Shapes are rendered and managed by `RasterBuffer`, which tracks the shapes' state and ensures they are redrawn correctly when modified.

4. **Editing**:
    - The `EditController` allows for selecting and editing existing shapes, such as resizing or changing properties (color, line type, etc.).

5. **Erasing**:
    - The `EraserController` allows users to erase shapes by dragging the mouse over them, removing the pixels from the canvas.

# Conclusion

This project is a flexible drawing application designed to manage different types of shapes on a canvas, providing tools for creating, modifying, and erasing objects. It utilizes a combination of MVC architecture (with controllers handling the logic) and uses singleton patterns for shared resources like the `Mouse` class and the `EnumStore` configuration. The drawing tools are rendered and manipulated using rasterization techniques to handle different line styles and the filling of areas.