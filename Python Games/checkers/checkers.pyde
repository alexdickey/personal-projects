from game_controller import GameController
from action_listener import ActionListener
from board import Board
from checker import Checker


###
# METRICS TO BE CHANGED
###
WIDTH = 1200
HEIGHT = 1200
TILES = 8

gc = GameController(WIDTH, HEIGHT, TILES)
listener = ActionListener(gc)


def setup():
    size(WIDTH, HEIGHT)
    colorMode(RGB, 255)
    gc.setup()


def draw():
    gc.update()

def mouseMoved():
    row = (mouseX) / (WIDTH // TILES)
    column = (mouseY) / (WIDTH // TILES)
    listener.mouseMoved(row, column, mouseX, mouseY)


def mousePressed():
    row = (mouseX) / (WIDTH // TILES)
    column = (mouseY) / (WIDTH // TILES)
    listener.mousePressed(row, column, mouseX, mouseY)

    # cur = gc.getChecker(row, column)
    # gc.displayChecker(row, column)
    # print(row, column)


def mouseDragged():
    row = (mouseX) / (WIDTH // TILES)
    column = (mouseY) / (WIDTH // TILES)
    listener.mouseDragged(row, column, mouseX, mouseY)


def mouseReleased():
    row = (mouseX) / (WIDTH // TILES)
    column = (mouseY) / (WIDTH // TILES)
    listener.mouseReleased(row, column, mouseX, mouseY)
