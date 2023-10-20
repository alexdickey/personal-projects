class ActionListener():

    def __init__(self, gc):
        self.game_controller = gc

    def mouseMoved(self, row, column, mouseX, mouseY):
        self.game_controller.mouseMoved(row, column)

    def mousePressed(self, row, column, mouseX, mouseY):
        self.game_controller.mousePressed(row, column, mouseX, mouseY)

    def mouseDragged(self, row, column, mouseX, mouseY):
        self.game_controller.mouseDragged(row, column, mouseX, mouseY)

    def mouseReleased(self, row, column, mouseX, mouseY):
        self.game_controller.mouseReleased(row, column, mouseX, mouseY)
        
