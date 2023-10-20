# Lets create a list of tiles on a "board"
from dot import Dot


class Tile():
    """This is a tile"""
    def __init__(self, x, y, length):
        self.x = x
        self.y = y
        self.length = length
        self.THICKNESS = 4
        self.has_tile = False
        self.DOT_X = (self.x + (self.length/2))
        self.DOT_Y = (self.y + (self.length/2))
        self.DOT_RADIUS = (self.length / 2)
        self.dots = [None]
    
    def __str__(self):
        return (str(self.x) + ", " + str(self.y))

    def display(self):
        "Draws the tile"
        stroke(0)
        strokeWeight(self.THICKNESS)
        noFill()
        square(self.x, self.y, self.length)
        if self.dots[0] is not None:
            self.dots[0].display()

    def add_dot(self, color):
        """adds a dot to a tile"""
        if self.dots[0] is None:
            self.dots[0] = (Dot(self.DOT_X, self.DOT_Y,
                                self.DOT_RADIUS, color))

    def remove_dot(self, tile):
        """removes a dot from a tile"""
        if self.dots[0]:
            self.dots[0] = None

    def switch_color(self):
        if self.dots[0].color == 1:
            self.dots[0].color = 0
        elif self.dots[0].color == 0:
            self.dots[0].color = 1
