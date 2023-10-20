from scanner import Scanner


def test_constructor():
    scan = Scanner(1000, 1000, 8, [])

    assert scan.WIDTH == 1000
    assert scan.HEIGHT == 1000
    assert scan.TILES == 8
    assert scan.tiles_list == []
