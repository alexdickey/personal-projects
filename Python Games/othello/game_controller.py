import re


class GameController():

    def __init__(self, WIDTH, HEIGHT, TILES):
        """This is the game controller"""
        self.WIDTH = WIDTH
        self.HEIGHT = HEIGHT
        self.TILES = TILES
        self.game_over = False
        self.player = 0
        self.p1_score = 0
        self.ready = False
        self.entered = False

    def update(self):
        if self.game_over:
            self.display_game_over()
            self.record_score(self.p1_score)

    def change_player(self):
        """
        if called, it will switch between the players
        """
        if self.player == 1:
            self.player = 0
        elif self.player == 0:
            self.player = 1

    def is_game_over(self, board_full, no_moves_left, p1_score):
        """
        Checks if the game is over based on if the board is full or
        if there are any available moves
        """
        if board_full or no_moves_left:
            self.game_over = True
            self.p1_score = p1_score
            return True

    def display_game_over(self):
        """
        Displays black rectangle
        Displays GAME OVER
        """
        rect_x, rect_y, rect_dx, rect_dy = 210, 300, 600, 130
        text_x, text_y, text_size = 220, 400, self.WIDTH/10
        black, white = 0, 1

        fill(black)
        rect((rect_x), (rect_y), (rect_dx), (rect_dy))

        fill(white)
        textSize(text_size)
        text("GAME OVER", text_x, text_y)

    def get_player_name(self):
        """
        prompts the user for their name
        returns the name
        """
        answer = self.input('enter your name')
        if answer:
            print('hi ' + answer)
        elif answer == '':
            print('[empty string]')
        else:
            print(answer)  # Canceled dialog will print None
        return answer

    def get_index(self, p1):
        """
        given a players score, this finds where it should be placed
        within the scores.txt file

        returns int
        """

        p1_score = p1
        index = 0

        f = open("scores.txt", "r")
        scores = f.readlines()

        for player in scores:
            score = int("".join(re.findall(r"[0-9]*", player)))
            if score > p1_score:
                index += 1
            elif score <= p1_score:
                f.close()
                return index
            else:
                f.close()
                return index

    def record_score(self, p1):
        """
        Adds the players name and score to the scores.txt file
        """
        if not self.ready:
            self.ready = True
        elif not self.entered:
            name = self.get_player_name()
            index = self.get_index(p1)
            p1_score = p1

            f = open("scores.txt", "r+")
            scores = f.readlines()

            scores.insert(index, name + " " + str(p1_score) + "\n")
            scores = "".join(scores)
            f.seek(0)
            f.truncate(0)
            f.write(scores + "\n")
            self.entered = True
        elif self.entered:
            exit()

    def input(self, message=''):
        from javax.swing import JOptionPane
        return JOptionPane.showInputDialog(frame, message)
