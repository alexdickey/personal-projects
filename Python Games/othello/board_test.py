from board import Board
from game_controller import GameController
from tile import Tile
from opponent import Opponent
from scanner import Scanner


def test_constructor():
    gc = GameController(1000, 1000, 8)
    b = Board(1000, 1000, 8, gc)
    s = Scanner(1000, 1000, 8, b.tiles_list)
    o = Opponent(1000, 1000, 8, gc, b.tiles_list)
    assert b.WIDTH == 1000
    assert b.HEIGHT == 1000
    assert b.TILES == 8
    assert b.LENGTH == 1000/8
    assert b.board_full is False
    assert b.no_moves is False
    assert b.p1_score == 2
    assert b.p2_score == 2
    assert b.p1_no_moves is False
    assert b.p2_no_moves is False
    assert b.gc is gc
    assert len(b.tiles_list) == b.TILES * b.TILES


def test_find_winner():
    gc = GameController(1000, 1000, 8)
    b = Board(1000, 1000, 8, gc)
    b.p1_score = 40
    b.p2_score = 20
    assert b.find_winner() == "Black Wins!"


def test_unoccupied():
    gc = GameController(1000, 1000, 8)
    b = Board(1000, 1000, 8, gc)
    tile = Tile(250, 250, 125)
    assert b.unoccupied(tile) is True
