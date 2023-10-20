from cell_cover import CellCover


class MineFieldCell():
    def __init__(self, x, y, CELL_SIZE, numFont):
        BLUE = (31, 55, 185)
        GREEN = (42, 175, 99)
        RED = (250, 0, 0)
        PURPLE = (12, 27, 118)
        MAROON = (119, 41, 41)
        TURQUOI = (119, 41, 41)
        BLACK = (10, 10, 10)
        GRAY = (100, 100, 100)
        self.numFont = numFont
        self.number_text_colors = [BLUE, GREEN, RED, PURPLE,
                                   MAROON, TURQUOI, BLACK, GRAY]
        self.CELL_SIZE = CELL_SIZE
        self.near_bombs = 0
        self.bomb = False
        self.explode = False
        self.found = False
        self.covered = True
        self.x = x
        self.y = y
        self.cover = CellCover(self.x, self.y, self.CELL_SIZE)

    def update(self):
        """
        Carry out cell display and
        cover display as needed
        """
        self.display()
        if self.covered:
            self.cover.display()
    
    def reveal(self):
        self.covered = False

    def display(self):
        """
        Display cells
        """
        # display uses Processing function
        # calls, so we won't write tests for it
        STROKE_COLOR = 150
        STROKE_WEIGHT = 2
        RED_BG = (255, 0, 0)
        GRAY_BG = (200, 200, 200)

        stroke(STROKE_COLOR)
        strokeWeight(STROKE_WEIGHT)
        if self.explode:
            fill_color = RED_BG
        else:
            fill_color = GRAY_BG

        fill(*fill_color)
        rect(self.x, self.y, self.CELL_SIZE, self.CELL_SIZE)

        if self.bomb:
            # Bomb display constants and logic
            BOMB_SIZE = 0.6
            BOMB_COLOR = 0
            HIGHLIGHT_COLOR = 250
            HIGHLIGHT_FALLOFF = 3
            HIGHLIGHT_FALLOFF_COLOR = 200
            HIGHLIGHT_SIZE = 0.2
            HIGHLIGHT_X_OFFSET = 1.8
            HIGHLIGHT_Y_OFFSET = 2.5

            fill(BOMB_COLOR)
            noStroke()

            # Draw the spikes
            rectMode(CENTER)
            # Translate frame of reference to center
            # of cell so that we can rotate around that
            # point to draw the mine spikes
            translate(self.x+self.CELL_SIZE/2,
                      self.y+self.CELL_SIZE/2)

            # Offset spikes from center point
            SPIKE_OFFSET = 11
            # Set spike size relative to the bomb size
            SPIKE_SIZE_DENOM = 5

            for _ in range(8):
                rotate(radians(45))
                rect(SPIKE_OFFSET, 0,
                     self.CELL_SIZE*BOMB_SIZE/SPIKE_SIZE_DENOM,
                     self.CELL_SIZE*BOMB_SIZE/SPIKE_SIZE_DENOM
                     )

            # Reset translation to not mess up drawing other
            # elements
            translate(-(self.x+self.CELL_SIZE/2),
                      -(self.y+self.CELL_SIZE/2))
            # Put rectMode back to default to avoid messing up
            # other rectangles
            rectMode(CORNER)

            # Draw bomb sphere
            ellipse(self.x+self.CELL_SIZE/2, self.y+self.CELL_SIZE/2,
                    self.CELL_SIZE*BOMB_SIZE, self.CELL_SIZE*BOMB_SIZE)

            # Draw bomb highlight
            fill(HIGHLIGHT_COLOR)
            strokeWeight(HIGHLIGHT_FALLOFF)
            stroke(HIGHLIGHT_FALLOFF_COLOR)
            ellipse(
                    self.x+self.CELL_SIZE/HIGHLIGHT_X_OFFSET,
                    self.y+self.CELL_SIZE/HIGHLIGHT_Y_OFFSET,
                    self.CELL_SIZE*HIGHLIGHT_SIZE,
                    self.CELL_SIZE*HIGHLIGHT_SIZE
                    )

        # Display neighbor bombs count
        if self.near_bombs > 0:
            fill(*self.number_text_colors[self.near_bombs-1])
            textFont(self.numFont)
            textAlign(LEFT)
            text(
                self.near_bombs,
                self.x+self.CELL_SIZE*.1,
                self.y+self.CELL_SIZE*.9
                )
        fill_color = GRAY_BG
