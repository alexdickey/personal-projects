class Scanner():
    """A class used to scan a tiles_list and return lists"""
    def __init__(self, WIDTH, HEIGHT, TILES, tiles_list):
        self.WIDTH = WIDTH
        self.HEIGHT = HEIGHT
        self.TILES = TILES
        self.tiles_list = tiles_list

    def find_horiz(self, tile):
        """creates a list of tiles in the same row
        Accepts: Tile object
        Returns: List of tile objects
        """
        horiz_row = []

        for h_tile in self.tiles_list:
            if (h_tile.y == tile.y):
                horiz_row.append(h_tile)
        return horiz_row

    def find_vert(self, tile):
        """creates a list of tiles in the same column
        Accepts: Tile object
        Returns: List of tile objects
        """
        vert_row = []

        for v_tile in self.tiles_list:
            if (v_tile.x == tile.x):
                vert_row.append(v_tile)
        return vert_row

    def find_diag_NW_SE(self, tile):
        """
        creates a list of tiles in the same diagonal starting NW traveling SE
        Accepts: Tile object
        Returns: List of tile objects
        """
        diag_row = []
        for d_tile in self.tiles_list:
            for i in range(self.TILES):
                i = i * (self.WIDTH/self.TILES)
                if (d_tile.x + i == tile.x) and (
                        d_tile.y + i == tile.y):
                    diag_row.append(d_tile)
                elif (d_tile.x - i == tile.x) and (
                        d_tile.y - i == tile.y):
                    diag_row.append(d_tile)
        return diag_row

    def find_diag_SW_NE(self, tile):
        """
        creates a list of tiles in the same diagonal starting SW traveling NE
        Accepts: Tile object
        Returns: List of tile objects
        """
        diag_row = []

        for d_tile in self.tiles_list:
            for i in range(self.TILES):
                i = i * (self.WIDTH/self.TILES)
                if (d_tile.x - i == tile.x) and (
                        d_tile.y + i == tile.y):
                    diag_row.append(d_tile)
                elif (d_tile.x + i == tile.x) and (
                        d_tile.y - i == tile.y):
                    diag_row.append(d_tile)
        return diag_row

    def horiz_flip(self, tile, h_row, direction):
        """
        Creates and returns a "flip list", or a list of tiles that should have
        their dots flip color
        """
        flip_list = []
        tile_start = False
        selected_start = False
        if direction == "W-E":  # scans the table from right to left
            h_row = reversed(h_row)

        for horiz_tile in h_row:
            if horiz_tile.dots[0]:
                if horiz_tile.dots[0] is None:
                    pass
                elif (horiz_tile.dots[0].color == tile.dots[0].color):
                    if (horiz_tile.y == tile.y) and (
                            horiz_tile.x == tile.x):
                        selected_start = True
                    else:
                        tile_start = True
                    if not selected_start and tile_start:
                        flip_list = []
                elif (horiz_tile.dots[0].color != tile.dots[0].color) and (
                        selected_start or tile_start) and not (
                            selected_start and tile_start):
                    flip_list.append(horiz_tile)
                if (selected_start and tile_start):
                    return flip_list
            if horiz_tile.dots[0] is None:
                flip_list = []
                tile_start = False
                selected_start = False

    def vert_flip(self, tile, v_column, direction):
        """
        Creates and returns a "flip list", or a list of tiles that should have
        their dots flip color
        """
        flip_list = []
        tile_start = False
        selected_start = False
        if direction == "S-N":  # scans the table bottom up
            v_column = reversed(v_column)

        for vert_tile in v_column:
            if vert_tile.dots[0]:
                if vert_tile.dots[0] is None:
                    pass
                elif (vert_tile.dots[0].color == tile.dots[0].color):
                    if (vert_tile.x == tile.x) and (
                            vert_tile.y == tile.y):
                        selected_start = True
                    else:
                        tile_start = True
                    if not selected_start and tile_start:
                        flip_list = []
                elif (vert_tile.dots[0].color != tile.dots[0].color) and (
                        selected_start or tile_start):
                    flip_list.append(vert_tile)
                if (selected_start and tile_start):
                    return flip_list
            if vert_tile.dots[0] is None:
                flip_list = []
                tile_start = False
                selected_start = False

    def diag_flip(self, tile, diag_row, direction):
        """
        Creates and returns a "flip list", or a list of tiles that should have
        their dots flip color
        """
        flip_list = []
        tile_start = False
        selected_start = False
        if direction == "N-W" or direction == "S-W":
            diag_row = reversed(diag_row)
        for diag_tile in diag_row:
            if diag_tile.dots[0]:
                if diag_tile.dots[0] is None:
                    pass
                elif (diag_tile.dots[0].color == tile.dots[0].color):
                    if (diag_tile.x == tile.x) and (
                            diag_tile.y == tile.y):
                        selected_start = True
                    else:
                        tile_start = True
                    if not selected_start and tile_start:
                        flip_list = []
                elif (diag_tile.dots[0].color != tile.dots[0].color) and (
                        selected_start or tile_start):
                    flip_list.append(diag_tile)
                if (selected_start and tile_start):
                    return flip_list
            if diag_tile.dots[0] is None:
                flip_list = []
                tile_start = False
                selected_start = False
