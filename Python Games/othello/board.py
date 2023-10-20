from tile import Tile
from game_controller import GameController
from scanner import Scanner
from opponent import Opponent
import re


class Board():
    def __init__(self, WIDTH, HEIGHT, TILES, game_controller):
        """Board constructor"""
        self.WIDTH = WIDTH
        self.HEIGHT = HEIGHT
        self.TILES = TILES
        self.LENGTH = WIDTH/TILES
        self.board_full = False
        self.no_moves = False
        self.p1_score = 2
        self.p2_score = 2
        self.p1_no_moves = False
        self.p2_no_moves = False
        self.gc = game_controller

        self.tiles_list = [Tile(self.LENGTH * i, self.LENGTH * j, self.LENGTH)
                           for i in range(self.TILES)
                           for j in range(self.TILES)]

        self.scan = Scanner(WIDTH, HEIGHT,
                            TILES, self.tiles_list)

        self.opponent = Opponent(WIDTH, HEIGHT, TILES,
                                 game_controller, self.tiles_list)

        # positional values of the initial dots
        self.INITIAL_DOT_W1 = (TILES * (TILES/2)) - (TILES/2)
        self.INITIAL_DOT_W2 = (TILES * (TILES/2)) - (TILES/2) + TILES - 1
        self.INITIAL_DOT_B1 = (TILES * (TILES/2)) - (TILES/2) + TILES
        self.INITIAL_DOT_B2 = (TILES * (TILES/2)) - (TILES/2) - 1

    def initialize_dots(self):
        # Initialize the original 4 dots in the center
        self.tiles_list[self.INITIAL_DOT_W1].add_dot(0)
        self.tiles_list[self.INITIAL_DOT_W2].add_dot(0)
        self.tiles_list[self.INITIAL_DOT_B1].add_dot(1)
        self.tiles_list[self.INITIAL_DOT_B2].add_dot(1)

    def update(self):
        """Checks to see if the game is won"""
        self.is_board_full()
        self.no_moves_left()
        self.gc.is_game_over(self.board_full, self.no_moves, self.p1_score)

    def display(self):
        """call each tiles display method"""
        for tile in self.tiles_list:
            tile.display()
        self.update()

    def is_move_available(self, player):
        """
        Determines if there is a move available on the board for a player
        Looks through all of the empty tiles and creates a list of tiles that
        would be flipped
        if a dot was placed on them
        If a tile exists so that a placed dot will allow a flip, the method
        returns True
        If no tile exists, or no flips can be made, returns false
        """

        for tile in self.tiles_list:
            if tile.dots[0] is None:
                self.place_dot(tile, self.gc.player)
                for item in self.create_flip_list(tile):
                    if item == [] or item is None:
                        pass
                    else:
                        self.remove_dot(tile)
                        if player == 0:
                            self.p1_no_moves = False
                        elif player == 1:
                            self.p2_no_moves = False
                        return True
                else:
                    tile.remove_dot(tile)
        if player == 0:
            self.p1_no_moves = True
            # print("Player 1 cannot move")
            self.gc.change_player()
        elif player == 1:
            self.p2_no_moves = True
            # print("Player 2 cannot move")
            self.gc.change_player()
        return False

    def place_dot(self, tile, color):
        """
        Places a dot
        """
        tile.add_dot(color)

    def remove_dot(self, tile):
        """
        removes a dot
        """
        tile.remove_dot(tile)

    def create_flip_list(self, tile):
        """
        Creates a list stemming in each direction from the tile
        Scans each directional list for flips that should occur
        Adds all of the tiles that should flip to a flip list
        returns flip list
        """
        flip_list = []

        tile_row = self.scan.find_horiz(tile)
        tile_column = self.scan.find_vert(tile)
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
        # print(flip_list)
        return flip_list

    def flip_dots(self, tile):
        flip_list = self.create_flip_list(tile)
        already_flipped = set()
        flips = 0

        for item in flip_list:
            if item is [] or item is None:
                pass
            else:
                for tile in item:
                    if (tile not in already_flipped):
                        tile.switch_color()
                        already_flipped.add(tile)
                        flips += 1

        # print("flips: " + str(flips))
        return flips

    def is_board_full(self):
        "Checks to see if the board is full based on dots"
        "Returns a boolean value"
        self.add_score()
        if (self.p1_score + self.p2_score) == (
             self.TILES * self.TILES):
            self.board_full = True
            self.display_winner()
            self.display_score()
        return self.board_full

    def no_moves_left(self):
        """
        Checks to see if there are any moves available for the given player
        returns boolean
        """
        self.is_move_available(self.gc.player)
        if (self.p1_no_moves) and (self.p2_no_moves):
            self.no_moves = True
            self.display_winner()
            self.display_score()
        return self.no_moves

    def find_winner(self):
        """
        Evaluates the scores of p1 and p2
        Returns:
            A string
        """
        if self.p1_score > self.p2_score:
            return "Black Wins!"
        elif self.p1_score < self.p2_score:
            return "White Wins!"
        elif self.p1_score == self.p2_score:
            return "It's a tie!"

    def display_winner(self):
        """
        Displays a black rectangle
        Displays the text of who won
        """
        winner_text = self.find_winner()
        black, white = 0, 1
        rect_x, rect_y, rect_dx, rect_dy = 300, 430, 400, 60
        text_x, text_y = 420, 465

        fill(black)
        rect((rect_x), (rect_y), (rect_dx), (rect_dy))
        fill(white)
        textSize(40)
        text(winner_text, text_x, text_y)

    def display_score(self):
        """
        Displays a black rectangle
        Displays the final score
        """
        self.add_score()
        black, white = 0, 1
        rect_x, rect_y, rect_dx, rect_dy = 260, 480, 480, 65
        text_x, text_y = 320, 525

        fill(black)
        rect((rect_x), (rect_y), (rect_dx), (rect_dy))

        fill(white)
        textSize(40)
        text("Black: " + str(self.p1_score) +
             "  White: " + str(self.p2_score),
             text_x, text_y)

    def add_score(self):
        """
        Adds the total number of tiles with certain colored dots to find score
        """
        self.p1_score = 0
        self.p2_score = 0

        for tile in self.tiles_list:
            if tile.dots[0] is None:
                pass
            elif tile.dots[0].color == 0:
                self.p1_score += 1
            elif tile.dots[0].color == 1:
                self.p2_score += 1

    def unoccupied(self, tile):
        """
        Accepts a tile object
        Returns a boolean
        True = Unoccupied
        False = Occupied
        """
        if tile.dots[0] is None:
            return True

    def find_tile(self):
        """
        Finds the tile the user clicked on
        returns the tile
        """
        for tile in self.tiles_list:
            if mouseX >= tile.x and mouseX < (tile.x + tile.length) and (
                    mouseY >= tile.y and mouseY < (tile.y + tile.length)
                    ):
                selected = tile
        return selected
