from constants import Constants as uc


class Board():

    def __init__(self, width, height, tiles):
        self.WIDTH = width
        self.HEIGHT = height
        self.tiles = tiles
        self.tile_length = (width // tiles)
        self.checkers = []

    def display(self):
        self.draw_board()
        self.draw_checkers()

    def draw_board(self):
        stroke(255)
        strokeWeight(2)
        for row in range(self.tiles):
            x = (row * self.tile_length)
            for column in range(self.tiles):
                y = (column * self.tile_length)
                if ((row + column) % 2 == 0):
                    fill(*uc.tan)
                else:
                    fill(*uc.brown)
                square(x, y, self.tile_length)

    def draw_checkers(self):
        for checker in self.checkers:
            checker.display()
