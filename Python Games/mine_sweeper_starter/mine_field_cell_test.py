from mine_field_cell import MineFieldCell


def test_constructor():
    x = 0
    y = 1
    CELL_SIZE = 50
    numFont = None  # dummy value for font
    cell = MineFieldCell(x, y, CELL_SIZE, numFont)
    assert (
            cell.x == 0
            and cell.y == 1
            and cell.CELL_SIZE == CELL_SIZE
            and cell.near_bombs == 0
            and not cell.bomb
            and not cell.explode
            and not cell.found
            and cell.covered
            )
