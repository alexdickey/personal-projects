from tile import Tile
from dot import Dot


def test_constructor():
    tile = Tile(125, 125, 125)
    assert tile.x == 125
    assert tile.y == 125
    assert tile.length == 125
    assert tile.THICKNESS == 4
    assert tile.has_tile is False
    assert tile.DOT_X == (125 + (125/2))
    assert tile.DOT_Y == (125 + (125/2))
    assert tile.DOT_RADIUS == (125 / 2)
    assert tile.dots == [None]
