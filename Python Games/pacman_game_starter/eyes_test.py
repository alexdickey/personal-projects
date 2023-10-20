from eyes import Eyes


def test_constructor():
    es = Eyes()
    assert es.left_eye.direction == es.right_eye.direction == (0, 0)
