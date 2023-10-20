from eye import Eye


def test_constructor():
    e = Eye()
    assert e.direction == (0, 0)


def test_look():
    e = Eye()
    e.look((1, 1))
    assert e.direction == (1, 1)
