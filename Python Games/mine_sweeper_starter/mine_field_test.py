from mine_field import MineField


def test_constructor():
    CELL_SIZE = 30
    FIELD_SIZE = 10
    numFont = None  # dummy value for font
    field = MineField(CELL_SIZE, FIELD_SIZE, numFont)
    assert(
        field.CELL_SIZE == CELL_SIZE
        and len(field.cells) == field.FIELD_SIZE
        and len(field.cells[0]) == field.FIELD_SIZE
    )


def test_plant_mines():
    CELL_SIZE = 30
    FIELD_SIZE = 10
    numFont = None  # dummy value for font
    field = MineField(CELL_SIZE, FIELD_SIZE, numFont)
    clear = not any([field.cells[x][y].bomb
                    for x in range(FIELD_SIZE)
                    for y in range(FIELD_SIZE)])
    assert(clear)
    mines = [(0, 0), (5, 5)]
    field.plant_mines(mines)
    assert(
        field.cells[0][0].bomb
        and field.cells[5][5].bomb
        and not field.cells[1][1].bomb
        )
