from board import Board
from tile import Tile
from game_controller import GameController
from opponent import Opponent
import threading
WIDTH = 1000
HEIGHT = 1000
TILES = 8
TILE_LENGTH = WIDTH / TILES

game_controller = GameController(WIDTH, HEIGHT, TILES)
board = Board(WIDTH, HEIGHT, TILES, game_controller)
tile = Tile(0, 0, TILE_LENGTH)


def setup():
    size(WIDTH, HEIGHT)
    colorMode(RGB, 1)
    board.initialize_dots()


def draw():
    background(.25, .65, .20)
    board.display()
    game_controller.update()


def mousePressed():
    """This is what happens when the mouse is pressed"""
    # Locates the tile that was pressed in
    tile = board.find_tile()
    if board.unoccupied(tile):
        board.place_dot(tile, 0)
        if board.flip_dots(tile) > 0 or board.p1_no_moves:
            game_controller.change_player()
            t = threading.Timer(1, board.opponent.make_move)
            t.start()
        else:
            board.remove_dot(tile)


def keyPressed():
    if game_controller.game_over:
        board.add_character(key)
