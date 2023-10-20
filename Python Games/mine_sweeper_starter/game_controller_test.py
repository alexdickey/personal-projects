from game_controller import GameController
from mine_field import MineField


def test_constructor():
    CELL_SIZE = 30
    FIELD_SIZE = 10
    numFont = None  # dummy value for font
    textFont = None  # dummy value for font
    field = MineField(CELL_SIZE, FIELD_SIZE, numFont)
    game_controller = GameController(field, textFont)
    assert (not game_controller.game_over and
            game_controller.mine_field is field)


def test_plant_mines():
    CELL_SIZE = 30
    FIELD_SIZE = 10
    numFont = None  # dummy value for font
    textFont = None  # dummy value for font
    field = MineField(CELL_SIZE, FIELD_SIZE, numFont)
    game_controller = GameController(field, textFont)
    assert(len(game_controller.mines) == 0)
    game_controller.plant_mines()
    assert(len(game_controller.mines) == game_controller.NUMBER_OF_MINES)


def test_end_game():
    CELL_SIZE = 30
    FIELD_SIZE = 10
    numFont = None  # dummy value for font
    textFont = None  # dummy value for font
    field = MineField(CELL_SIZE, FIELD_SIZE, numFont)
    game_controller = GameController(field, textFont)
    assert(not game_controller.game_over)
    game_controller.end_game('win')
    assert(game_controller.game_over == 'win')
