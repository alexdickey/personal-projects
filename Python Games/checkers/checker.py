from constants import Constants as uc


class Checker():

    def __init__(self, tile_size, color, pos):
        self.is_king = False
        self.is_highlighted = False
        self.color = color                  # 'red or 'black'
        self.fill_color = self.get_fill()
        self.tile_size = tile_size
        self.big_radius = tile_size * .8            # 80% of tile size
        self.small_radius = self.big_radius * .7
        self.pos = pos                      # This needs to be the grid
        self.center = ((self.pos[0] * tile_size) + .5 * tile_size,
                       (self.pos[1] * tile_size) + .5 * tile_size)             # This neesd to be where it is drawn

    def __eq__(self, o):
        return (self.pos[0] == o.pos[0] and self.pos[1] == o.pos[1] and
                self.color == o.color)

    def __hash__(self):
        return hash((self.pos[0], self.pos[1]))

    def __str__(self):
        return self.color + " at " + str(self.pos)

    def display(self):
        self.display_outer()
        self.display_inner()

    def display_outer(self):
        fill(*self.fill_color)
        stroke(255)
        if (self.is_highlighted):
            strokeWeight(10)
        else:
            strokeWeight(2)
        circle(self.center[0], self.center[1], self.big_radius)

    def display_inner(self):
        fill(*self.fill_color)
        stroke(255)
        strokeWeight(2)
        circle(self.center[0], self.center[1], self.small_radius)

    def get_fill(self):       
        if (self.color == 'black'):
            self.fill_color = uc.black
            return uc.black
        elif (self.color == 'red'):
            self.fill_color = uc.red
            return uc.red

    def update_position(self, row, column):
        self.pos = (row, column)
        self.center = ((self.pos[0] * self.tile_size) + .5 * self.tile_size,
                       (self.pos[1] * self.tile_size) + .5 * self.tile_size)

    def become_king(self):
        self.is_king = True
        self.fill_color = (0, 255, 0)

    def highlight(self):
        self.is_highlighted = True
