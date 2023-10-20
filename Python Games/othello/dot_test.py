from dot import Dot


def test_constructor():
    dot = Dot(10, 10, 5, 0)
    assert dot.x == 10
    assert dot.y == 10
    assert dot.radius == 5
    assert dot.color == 0
