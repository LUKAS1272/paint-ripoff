# User Manual for Drawing Application

Welcome to the Drawing Application! This user manual will guide you through how to use this application and its features. This app allows you to create, modify, and erase different shapes like lines, circles, rectangles, polygons, and more.

---

## Table of Contents

1. Main Features
    - Drawing Tools
    - Editing Shapes
    - Erasing Shapes
    - Filling Areas
2. Interface Overview
3. Advanced Features
    - Line Styles
    - Shape Customization

---

## Main Features

### Drawing Tools

You can draw different shapes and lines using the following tools:

1. **Line Tool**:
    - Select the "Line" tool and click on the canvas to set the starting point.
    - Drag the mouse to the end point to draw a line.

2. **Circle Tool**:
    - Select the "Circle" tool and click on the canvas to create a starting point of the circle.
    - Drag to adjust the radius of the circle.
      - Radius is calculated based on the difference between first and second point.

3. **Rectangle Tool**:
    - Select the "Rectangle" tool and click on the canvas to create the first corner.
    - Drag to define the size and shape of the rectangle.

4. **Polygon Tool**:
    - Select the "Polygon" tool and click on the canvas to place vertices.
    - Continue clicking to add more vertices.
    - Once finished drawing given polygon, choose a different tool.

5. **Eraser Tool**:
    - Select the "Eraser" tool and click or drag over existing shapes to remove them.

6. **Hand Tool**:
    - Select the "Hand" tool to move objects across the canvas.

7. **Fill Tool**:
    - Select the "Fill" tool and click on a closed area to fill it with the current color.

### Editing Shapes

To edit an existing shape:

1. **Select the Edit Tool**:
    - Select the "Edit" tool from the toolbar.

2. **Choose the Shape to Edit**:
    - Click and hold on a shape to select it for editing.

3. **Modify the Shape**:
    - For lines and polygons, you can move its points.
    - Other objects can be selected and modified by clicking near to any of their pixel. 

4. **Apply Changes**:
    - Once you finish editing, release the mouse and the changes will automatically be applied.

### Erasing Shapes

1. **Select the Eraser Tool**:
    - Choose the "Eraser" tool from the toolbar.

2. **Erase Shapes**:
    - Click or drag the eraser over the shapes you want to remove.
    - The eraser will clear the pixels in its radius.

### Filling Areas

1. **Select the Fill Tool**:
    - Choose the "Fill" tool from the toolbar.

2. **Click on an Area to Fill**:
    - Click inside any closed shape (e.g., a polygon or a closed path) to fill it with the current color.

---

## Interface Overview

The main interface of the application consists of:

1. **Drawing Canvas**:
   - The large area where you can draw and interact with shapes.

2. **Sidebar**:
   - Located on the right side of the screen, this sidebar contains the following options, arranged in order: color selection, drawing tools, thickness adjustment (buttons for increasing or decreasing), line type, alignment, and a clear button for clearing the canvas.

3. **Selected Option**:
   - The currently selected option is a button with a yellow background, distinguishing it from other buttons, which have a white background.

---

## Advanced Features

### Line Styles

You can choose different line styles for your drawings:

1. **Solid Lines**: Default line style.
2. **Dashed Lines**: Lines with dashed segments.
3. **Dotted Lines**: Lines made up of dots.

### Shape Customization

For each shape, you can customize the following properties:

1. **Color**: Change the color of the shape.
2. **Thickness**: Adjust the thickness of the lines used to draw shapes.
3. **Alignment**: For rectangles, choose whether to draw a square or a non-square rectangle. For lines, choose whether to snap to 45° angles or not.

---

## Special rules

There are a few rules, that may seem as bugs at first. However, they are intended behavior to make using the application easier.

1. **Eraser:** Once an eraser touches any object, you cannot neither edit it, not move it.
2. **Filler:** Fillers are not allowed to paint over objects. If you have an object whose border and infill are the same, clicking on the infill will only change its infill's color, not the border's.
3. **Thickness:** Thickness values are limited to the range of `<2; 10>`.
4. **Objects' properties**: When editing an object, its color, line type (except for circles) and alignment for rectangles is adapted to currently chosen values.

---

## Conclusion

This application provides all the essential tools to create and modify drawings in an intuitive way. Whether you're creating simple lines or complex polygons, the app offers flexibility and customization options to suit your needs. Enjoy drawing!