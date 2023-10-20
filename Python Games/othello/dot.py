class Dot():
    def __init__(self, x, y, radius, color):
        self.x = x
        self.y = y
        self.radius = radius
        self.color = color

    def display(self):
        """draws a game piece"""
        fill(self.color)
        circle(self.x, self.y, self.radius)
