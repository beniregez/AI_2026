import copy

class chessboard:
    def __init__(self, queens: list[int]):
        self.queens = queens
        self.size = len(queens)
        self.h = self.get_heuristic(self.queens)

    def print_board(self) -> None:
        for j in range(self.size):
            for i in range(self.size):
                if self.queens[i] == j:
                    print("[Q]", end="")
                else:
                    print("[ ]", end="")
            print()
    
    def get_heuristic(self, queens) -> int:
        h = 0
        for i in range(self.size):
            for j in range(i+1, self.size):
                # Check same row
                if queens[i] == queens[j]:
                    h += 1
                # Check same diagonal
                if abs(queens[i] - queens[j]) == abs(i-j):
                    h += 1
        return h

    def get_h_board(self) -> list[list[int]]:
        h_board = [[0 for _ in range(self.size)] for _ in range(self.size)]

        for col in range(self.size):
            col_queen = self.queens[col]
            for row in range(self.size):
                copy_queens = copy.deepcopy(self.queens)        
                copy_queens = copy.deepcopy(self.queens)
                copy_queens[col] = row
                h_board[col][row] = self.get_heuristic(copy_queens)

        return h_board

    def print_h_board(self) -> None:
        h_board = self.get_h_board()
        rows = ["" for _ in range(self.size)]

        for col_idx, col in enumerate(h_board):
            for row_idx, row in enumerate(col):
                if self.queens[col_idx] == row_idx:
                    rows[row_idx] += f"\033[34m[{row}]\033[0m"
                else:
                    if row > self.h:
                        # print(f"\033[31m[{row}]\033[0m", end="")
                        rows[row_idx] += f"\033[31m[{row}]\033[0m"
                    elif row < self.h:
                        # print(f"\033[32m[{row}]\033[0m", end="")
                        rows[row_idx] += f"\033[32m[{row}]\033[0m"
                    else:
                        rows[row_idx] += f"\033[33m[{row}]\033[0m"

        for row in rows:
            print(row)

if __name__ == "__main__":
    # 6.2 (a)
    # Initial candidate
    # board = chessboard([2, 3, 1, 2, 4])
    # board.print_h_board()
    # print()

    # Iteration 1
    # board = chessboard([0, 3, 1, 2, 4])
    # board.print_h_board()   # Iteration 1
    # print()

    # 6.2 (b)
    board = chessboard([2, 3, 1, 2, 4])
    board.print_h_board()
    print()

    # Iteration 1 (file 3, row 1 picked by algo)
    board = chessboard([2, 3, 0, 2, 4])
    board.print_h_board()
    print()

    # Iteration 2 (file 2, row 4 picked by algo = null move)
    board = chessboard([2, 3, 0, 2, 4])
    board.print_h_board()
    print()

    # By picking file 1, row 2, the algo finds a
    # solution and stops.
    board = chessboard([1, 3, 0, 2, 4])
    board.print_h_board()