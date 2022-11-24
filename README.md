The following specifications are copied from professor Kevin Lin's course website.
# Image processing
Seam-carving is a content-aware image resizing technique where the size of an image is reduced by one pixel in height (by removing a horizontal seam) or width (by removing a vertical seam) at a time. Unlike cropping pixels from the edges or scaling the entire image, seam carving attempts to identify and preserve the most important content in an image.
#### Horizontal seam
A path of adjacent or diagonally-adjacent pixels from the left edge of an image to its right edge.
#### Vertical seam
The same as a horizontal seam but from the top edge of an image to its bottom edge.
