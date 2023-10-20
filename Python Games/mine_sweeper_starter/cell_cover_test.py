from cell_cover import CellCover


def test_constructor():
    x = 0
    y = 1
    CELL_SIZE = 50
    cover = CellCover(x, y, CELL_SIZE)
    assert (cover.x == 0 and
            cover.y == 1 and
            cover.CELL_SIZE == CELL_SIZE)
