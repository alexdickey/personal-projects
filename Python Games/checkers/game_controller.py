from board import Board
from checker import Checker


class GameController():
    def __init__(self, width, height, tiles):
        self.WIDTH = width
        self.HEIGHT = height
        self.tile_num = tiles
        self.board = Board(width, height, tiles)
        self.tile_size = (self.WIDTH // self.tile_num)
        # self.checker_size = (self.tile_size * .8)
        self.players_turn = 'black'
        self.curr_checker = None
        self.can_jump = set()
        # self.curr_checker_index = None

    def update(self):
        self.board.display()

    def setup(self):
        self.resetCheckers()
        self.players_turn = 'black'

    def resetCheckers(self):
        # self.board.checkers.clear()
        for x in range(self.tile_num):
            for y in range(self.tile_num):
                if ((y < 3) and (x+y) % 2 == 1):
                    checker = Checker(self.tile_size, 'red', (x, y))
                    self.board.checkers.append(checker)
                elif ((y > 4) and (x+y) % 2 == 1):
                    checker = Checker(self.tile_size, 'black', (x, y))
                    self.board.checkers.append(checker)

    def mousePressed(self, row, column, mouseX, mouseY):
        """Responsible for picking up a checker piece"""

        # print(row, column)

        selected = Checker(self.tile_size, self.players_turn, [row, column])
        # print("King - " + str(selected.is_king))
        if (self.board.checkers.__contains__(selected)):
            index = self.board.checkers.index(selected)
            # print(self.board.checkers[index].pos)
            self.curr_checker = index
        else:
            print("checker not found")

    def mouseReleased(self, row, column, mouseX, mouseY):
        if (self.curr_checker is not None):
            orig_x = self.board.checkers[self.curr_checker].pos[0]
            orig_y = self.board.checkers[self.curr_checker].pos[1]
            if (self.isValidJump(self.board.checkers[self.curr_checker],
                                 (row, column))):
                self.makeJump((row, column))
            elif (self.isValidMove(self.board.checkers[self.curr_checker],
                                   (row, column))):
                self.makeMove((row, column))
            else:
                self.board.checkers[self.curr_checker].update_position(orig_x,
                                                                       orig_y)
            # self.board.checkers.remove(self.board.checkers[self.curr_checker])
        self.curr_checker = None

    def mouseDragged(self, row, column, mouseX, mouseY):
        if (self.curr_checker is not None):
            self.board.checkers[self.curr_checker].center = (mouseX, mouseY)

    def mouseMoved(self, row, column):
        for checker in self.board.checkers:
            if ((row, column) == checker.pos and
                    checker.color == self.players_turn and
                    self.can_jump.__contains__(checker)):
                checker.is_highlighted = True
            elif (len(self.can_jump) == 0 and
                    (row, column) == checker.pos and
                    checker.color == self.players_turn and
                    self.canMove(checker)):
                checker.is_highlighted = True
            else:
                checker.is_highlighted = False

    def canMove(self, checker):
        cur_pos_x = checker.pos[0]
        cur_pos_y = checker.pos[1]

        directions = self.validDirections(checker)

        for move in directions:
            if self.isValidMove(checker, (cur_pos_x + move[0],
                                          cur_pos_y + move[1])):
                return True
        return False

    def isValidMove(self, checker, loc):
        # This method will take a checker and a location.
        #         It will determine if the move is valid.

        left_up, right_up = [-1, -1], [1, -1]  # black movement
        left_dn, right_dn = [-1, 1], [1, 1]    # red movement
        cur_pos = checker.pos                  # tuple of current checker (x,y)
        directions = self.validDirections(checker)

        if (self.inBounds(loc)):
            if (self.isEmpty(loc)):
                for move in directions:
                    if (cur_pos[0] + move[0] == loc[0] and
                            cur_pos[1] + move[1] == loc[1]):
                        return True
        return False

    def makeMove(self, loc):
        self.board.checkers[self.curr_checker].update_position(loc[0], loc[1])
        self.checkKing(self.board.checkers[self.curr_checker])
        self.changeTurns()
        for checker in self.can_jump:
            print(checker)

    def canJump(self, checker):
        cur_pos_x = checker.pos[0]
        cur_pos_y = checker.pos[1]
        directions = self.validDirections(checker)
        for move in directions:
            if self.isValidJump(checker, (cur_pos_x + move[0] + move[0],
                                          cur_pos_y + move[1] + move[1])):
                return True
        return False

    def isValidJump(self, checker, loc):
        # get reference to the current checker
        c = checker
        # make sure it is in bounds
        if self.inBounds(loc):
            # check if the loc is a pos of any checkers
            if self.isEmpty(loc):
                directions = self.validDirections(c)  # get the list of directions it is able to be moved in
                for move in directions:  # for each of the moves, if the move taken results at the location
                    if (c.pos[0] + move[0] + move[0] == loc[0] and
                            c.pos[1] + move[1] + move[1] == loc[1]):
                        for checker in self.board.checkers:
                            if (c.pos[0] + move[0] == checker.pos[0] and
                                    c.pos[1] + move[1] == checker.pos[1] and
                                    c.color != checker.color):
                                return True
        return False

    def makeJump(self, loc):
        jumped_checker = self.getJumped(loc)

        self.board.checkers[self.curr_checker].update_position(loc[0], loc[1])
        self.board.checkers.remove(jumped_checker)
        # if (self.canJump(self.board.checkers[self.curr_checker])):
        #     n_loc = self.getJumpLoc()
        #     self.makeJump(n_loc)
        self.checkKing(self.board.checkers[self.curr_checker])
        self.changeTurns()
        for checker in self.can_jump:
            print(checker)

    def getJumped(self, loc):
        c = self.board.checkers[self.curr_checker]
        start_x, start_y = c.pos[0], c.pos[1]

        jumped_x = (start_x + loc[0]) / 2
        jumped_y = (start_y + loc[1]) / 2
        for checker in self.board.checkers:
            if ((checker.pos[0] == jumped_x) and
                    (checker.pos[1] == jumped_y)):
                return checker  # may have to return the index instead

    def validDirections(self, checker):
        left_up, right_up = [-1, -1], [1, -1]  # black movement
        left_dn, right_dn = [-1, 1], [1, 1]    # red movement

        if (checker.is_king):
            directions = [left_up, right_up,
                          left_dn, right_dn]
            return directions
        elif (checker.color == 'red'):
            directions = [left_dn, right_dn]
            return directions
        elif (checker.color == 'black'):
            directions = [left_up, right_up]
            return directions

    def inBounds(self, loc):
        if ((loc[0] < self.WIDTH // self.tile_size) and
                (loc[0] >= 0) and
                (loc[1] < self.HEIGHT // self.tile_size) and
                (loc[1] >= 0)):
            return True
        return False

    def isEmpty(self, loc):
        for checker in self.board.checkers:
            if checker.pos == loc:
                return False
        return True

    def checkKing(self, c):
        if (c.color == 'black'):
            if (c.pos[1] == 0):
                self.board.checkers[self.curr_checker].become_king()
        elif (c.color == 'red'):
            if (c.pos[1] == 7):
                self.board.checkers[self.curr_checker].become_king()

    def changeTurns(self):
        self.switchPlayer()
        self.can_jump.clear()
        self.getJumps()

    def switchPlayer(self):
        if (self.players_turn == 'black'):
            self.players_turn = 'red'
            print("Reds turn")
        else:
            self.players_turn = 'black'
            print("Blacks turn")

    def getJumps(self):
        for checker in self.board.checkers:
            if (checker.color == self.players_turn and
                    self.canJump(checker)):
                self.can_jump.add(checker)

    def getJumpLoc(self):
        c = self.board.checkers[self.curr_checker]
        cur_pos_x = c.pos[0]
        cur_pos_y = c.pos[1]
        directions = self.validDirections(c)
        for move in directions:
            if self.isValidJump(c, (cur_pos_x + move[0] + move[0],
                                    cur_pos_y + move[1] + move[1])):
                return move
        return False
        
