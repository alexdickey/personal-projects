class CellCover():
    def __init__(self, x, y, CELL_SIZE):
        self.x = x
        self.y = y
        self.CELL_SIZE = CELL_SIZE

    def display(self):
        """
        Display cell cover
        """
        BEVEL_SHADOW = 80
        BEVEL_HIGHLIGHT = 250
        MAIN_CELL_COVER = 200
        BEVEL = 0.1
        # Change ALPHA to ~100 for debugging
        ALPHA = 255

        noStroke()
        fill(BEVEL_SHADOW, ALPHA)
        rect(self.x, self.y,
             self.CELL_SIZE, self.CELL_SIZE)

        fill(BEVEL_HIGHLIGHT, ALPHA)
        rect(self.x, self.y,
             self.CELL_SIZE*(1-BEVEL), self.CELL_SIZE*(1-BEVEL))

        fill(MAIN_CELL_COVER, ALPHA)
        rect(self.x+self.CELL_SIZE*BEVEL, self.y+self.CELL_SIZE*BEVEL,
             self.CELL_SIZE*(1-BEVEL*2), self.CELL_SIZE*(1-BEVEL*2))
