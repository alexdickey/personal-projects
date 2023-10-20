from game_controller import GameController


def test_constructor():
    gc = GameController(1000, 1000, 8)
    assert gc.WIDTH == 1000
    assert gc.HEIGHT == 1000
    assert gc.TILES == 8
    assert gc.game_over is False
    assert gc.player == 0
    assert gc.p1_score == 0
    assert gc.ready is False
    assert gc.entered is False

def test_is_game_over():
    gc = GameController(1000, 1000, 8)
    gc.is_game_over(True, False, 10)
    assert gc.game_over is True
