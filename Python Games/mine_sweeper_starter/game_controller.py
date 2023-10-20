import random


class GameController():
    """
    The Game Controller
    """
    def __init__(self, field, wordsFont):
        self.mines = set()
        self.NUMBER_OF_MINES = 30
        self.mine_field = field
        self.game_over = False
        self.wordsFont = wordsFont
        field.gc = self

    def plant_mines(self):
        """
        Generate random mine positions and
        plant them in the mine field
        """
        # Processing provides its own random number generator. However
        # using Processing functions rather than native Python
        # libaries creates problems for Pytest. In order
        # to make the program more testable, I will use the Python
        # random module instead. The Processing approach is commented
        # out.
        # randx = int(random(self.mine_field.FIELD_SIZE))
        # randy = int(random(self.mine_field.FIELD_SIZE))
        randx = random.randint(0, self.mine_field.FIELD_SIZE-1)
        randy = random.randint(0, self.mine_field.FIELD_SIZE-1)

        for _ in range(self.NUMBER_OF_MINES):
            # Loop in case we have inadvertently duplicated a
            # mine position
            while (randx, randy) in self.mines:
                # randx = int(random(self.mine_field.FIELD_SIZE))
                # randy = int(random(self.mine_field.FIELD_SIZE))
                randx = random.randint(0, self.mine_field.FIELD_SIZE-1)
                randy = random.randint(0, self.mine_field.FIELD_SIZE-1)
            self.mines.add((randx, randy))

        self.mine_field.plant_mines(self.mines)

    def reveal(self, x, y):
        """
        Reveal appropriate positions in the mine field
        """
        self.mine_field.reveal(x, y)

    def reveal_all(self):
        """
        Reveal all positions in the mine field
        """
        self.mine_field.reveal_all()

    def end_game(self, win_or_lose):
        """
        End the game and set win or lose condition
        """
        if not self.game_over:
            self.game_over = win_or_lose
            self.reveal_all()

    def update(self):
        """
        Carry out mine field update and
        check if game is over
        """
        self.mine_field.update()
        if self.game_over:
            self.display_end_text()

    def display_end_text(self):
        """
        Display end of game message
        """
        if self.game_over == 'lose':
            message = "You set off\n a mine!"
        elif self.game_over == 'win':
            message = "You cleared\nthe minefield!"
        center = self.mine_field.FIELD_SIZE*self.mine_field.CELL_SIZE/2
        offset = 3
        textAlign(CENTER)
        textFont(self.wordsFont)
        fill(0)
        text(message, center+offset, center+offset)
        fill(255)
        text(message, center, center)
