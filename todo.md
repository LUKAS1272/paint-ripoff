# TODO

## Action: Eraser

- add attribute 'editable' to all objects
- radius
- if there is an object in buffer in given radius (detected by similar algo to the one used in circle rasterizing)
- change attribute editable to false
- clear all buffer pixels within the radius (first set the attribute to false, though)

## Action: Edit

- if edit object's point is within the range of edit tool
  - Line: points
  - Polygon: points
  - Rectangle: any pixel of the object
  - Circle: any pixel of the object
- if the (closest) object within range is editable

### Rectangle

- **unrender**
- calculate the furthest corner
  - set it as the "first point"
- set the current cursor position as the "second point"
- **render**