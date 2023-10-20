from tile import Tile
from game_controller import GameController
from scanner import Scanner


class Opponent():

    def __init__(self, WIDTH, HEIGHT, TILES, game_controller, tiles_list):
        self.gc = game_controller
        self.tiles_list = tiles_list
        self.scan = Scanner(WIDTH, HEIGHT, TILES, tiles_list)

    def make_move(self):
        """
        If able, finds the best move and makes it
        """
        tile = self.find_best_move(self.tiles_list)
        if tile is None:
            self.gc.change_player()
        else:
            self.place_dot(tile)
            self.flip_dots(tile)
            self.gc.change_player()

    def find_best_move(self, tile_list):
        """
        Creates a flip list and returns the tile that causes the most flips
        """
        most_flips = 0
        best_tile = None
        for tile in tile_list:
            if tile.dots[0] is None:
                self.place_dot(tile)
                for item in self.create_flip_list(tile):
                    if item == [] or item is None:
                        pass
                    else:
                        flips = len(item)
                        if flips > most_flips:
                            most_flips = flips
                            best_tile = tile
                    self.remove_dot(tile)
        return best_tile

    def create_flip_list(self, tile):
        """
        Creates a list stemming in each direction from the tile
        Scans each directional list for flips that should occur
        Adds all of the tiles that should flip to a flip list
        returns flip list
        """
        flip_list = []

        tile_column = self.scan.find_vert(tile)
        tile_row = self.scan.find_horiz(tile)
        tile_diag_NW_SE = self.scan.find_diag_NW_SE(tile)
        tile_diag_SW_NE = self.scan.find_diag_SW_NE(tile)

        flip_list.append(self.scan.horiz_flip(tile, tile_row, "E-W"))
        flip_list.append(self.scan.horiz_flip(tile, tile_row, "W-E"))

        flip_list.append(self.scan.vert_flip(tile, tile_column, "N-S"))
        flip_list.append(self.scan.vert_flip(tile, tile_column, "S-N"))

        flip_list.append(self.scan.diag_flip(tile, tile_diag_NW_SE, "S-E"))
        flip_list.append(self.scan.diag_flip(tile, tile_diag_NW_SE, "N-W"))

        flip_list.append(self.scan.diag_flip(tile, tile_diag_SW_NE, "N-E"))
        flip_list.append(self.scan.diag_flip(tile, tile_diag_SW_NE, "S-W"))

        return flip_list

    def find_num_flip(self, tile):
        """
        takes a list of tiles and returns an int (number of flips)
        """
        flip_list = self.create_flip_list(tile)
        already_flipped = set()
        flips = 0

        for item in flip_list:
            if item is [] or item is None:
                pass
            else:
                for tile in item:
                    if (tile not in already_flipped):
                        already_flipped.add(tile)
                        flips += 1

        return flips

    def flip_dots(self, tile):
        """
        flips the color of the dots from a list of tiles
        """
        flip_list = self.create_flip_list(tile)
        already_flipped = set()

        for item in flip_list:
            if item is [] or item is None:
                pass
            else:
                for tile in item:
                    if (tile not in already_flipped):
                        tile.switch_color()
                        already_flipped.add(tile)

    def place_dot(self, tile):
        """
        Places a white dot
        """
        tile.add_dot(1)

    def remove_dot(self, tile):
        """
        removes a dot
        """
        tile.remove_dot(tile)
