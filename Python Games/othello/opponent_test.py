from opponent import Opponent
from tile import Tile
from game_controller import GameController
from scanner import Scanner


def test_constructor():
    gc = GameController(1000, 1000, 8)
    scan = Scanner(1000, 1000, 8, [])
    opp = Opponent(1000, 1000, 8, gc, [])

    assert opp.gc is gc
    assert opp.tiles_list == []
